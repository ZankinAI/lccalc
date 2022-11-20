package com.cpp.lccalc.controllers;

import com.cpp.lccalc.classes.*;
import com.cpp.lccalc.models.*;
import com.cpp.lccalc.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.DecimalFormat;
import java.time.LocalDate;

@Controller
public class SubTaskController {

    @Autowired
    private ProjectRopository projectRopository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProjectManagerRepository projectManagerRepository;

    @Autowired
    private PerformerRepository performerRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CommercialOfferRepository commercialOfferRepository;

    @Autowired
    SubTaskRepository subTaskRepository;

    @Autowired
    private HumanResourceRepository humanResourceRepository;

    @Autowired
    private HumanResourceSubTaskRepository humanResourceSubTaskRepository;

    @Autowired
    private MaterialResourcesRepository materialResourcesRepository;

    @Autowired
    private MaterialResourcesSubTaskRepository materialResourcesSubTaskRepository;

    //Страница редактирования подзадачи
    @GetMapping("/subtask/{id}/edit")
    public String subTaskEdit(@PathVariable(value = "id") long id, Model model) {
        SubTask subTask = subTaskRepository.findById(id).get();
        Task task = subTask.getTask();
        model.addAttribute("task", task);
        model.addAttribute("subtask", subTask);

        MaterialResourcesListDTO materialResourcesList = new MaterialResourcesListDTO();

        Iterable<MaterialResources> materialResources = materialResourcesRepository.findAll();

        for (MaterialResources materialResource : materialResources) {
            materialResourcesList.addResource(new MaterialResourcesDTO(materialResource));
        }

        model.addAttribute("materialResourcesList", materialResourcesList);

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        for (HumanResources humanResource : humanResources) {
            humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
        }
        model.addAttribute("humanResourcesList", humanResourcesList);

        model.addAttribute("isCorrectEditSubTask", true);
        return "subtask-edit";
    }

    //Обновление подзадачи
    @PostMapping("/subtask/{id}/edit")
    public String subTaskUpdate(@PathVariable(value = "id") long id,
                                @RequestParam String name,
                                @RequestParam String subTaskIndex,
                                @RequestParam(defaultValue = "0") Long laboriousness,
                                @RequestParam(defaultValue = "") String previousIndex,
                                @RequestParam String progress,
                                Model model) {
        SubTask subTask = subTaskRepository.findById(id).get();
        Task task = subTask.getTask();
        String prevIndex = previousIndex;
        int k=0;
        while(prevIndex!=null){

            if (prevIndex.equals(subTaskIndex)){

                model.addAttribute("task", task);
                model.addAttribute("subtask", subTask);

                MaterialResourcesListDTO materialResourcesList = new MaterialResourcesListDTO();

                Iterable<MaterialResources> materialResources = materialResourcesRepository.findAll();

                for (MaterialResources materialResource : materialResources) {
                    materialResourcesList.addResource(new MaterialResourcesDTO(materialResource));
                }

                model.addAttribute("materialResourcesList", materialResourcesList);

                Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
                HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

                for (HumanResources humanResource : humanResources) {
                    humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
                }
                model.addAttribute("humanResourcesList", humanResourcesList);
                model.addAttribute("isCorrectEditSubTask", false);
                return "subtask-edit";
            }

            prevIndex = task.findPrevIndexOfSubtask(prevIndex);
            if (prevIndex==null) break;


        };

        subTask.setName(name);
        subTask.setSubTaskIndex(subTaskIndex);
        subTask.setLaboriousness(laboriousness);
        subTask.setPreviousIndex(previousIndex);
        subTask.setProgress(progress);
        subTask.findDuration();
        subTask.findBudget();

        subTaskRepository.save(subTask);

        model.addAttribute("subtask", subTask);

        task.sortSubTasks();

        CommercialOffer selectedCommercialOffer = null;
        for (CommercialOffer co : task.getCommercialOffers()) {
            if (co.getStatus().equals("В работе")) selectedCommercialOffer = co;

        }

        TaskCalculation taskCalculation = new TaskCalculation(task);

        if (task.getPerformerName()!=null){
            if (task.getPerformerName().equals("Своя компания")){
                task.setProgress(Double.valueOf(taskCalculation.getProgressValue()));
            }
            else if (task.getPerformerName()!=null){
                if (task.getState().equals("Завершена")){
                    task.setProgress(100.0);
                }
                else task.setProgress(0.0);
            }
            else task.setProgress(0.0);
        }


        taskRepository.save(task);
        task.sortSubTasks();

        model.addAttribute("selectedCo", selectedCommercialOffer);

        model.addAttribute("task", task);
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);
        MaterialResourcesListDTO materialResourcesList = new MaterialResourcesListDTO();

        Iterable<MaterialResources> materialResources = materialResourcesRepository.findAll();

        for (MaterialResources materialResource : materialResources) {
            materialResourcesList.addResource(new MaterialResourcesDTO(materialResource));
        }

        model.addAttribute("materialResourcesList", materialResourcesList);

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        for (HumanResources humanResource : humanResources) {
            humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
        }
        model.addAttribute("humanResourcesList", humanResourcesList);
        int newSubTaskSubIndex = Utils.getIndex(task.findLastIndexOfSubTasks());
        String newSubTaskIndex = task.getTaskIndex() + "."+ String.valueOf(newSubTaskSubIndex+1);
        model.addAttribute("newSubTaskIndex",newSubTaskIndex);
        return "task-edit";
    }

    @GetMapping("/subtask/{id}/remove")
    public String subTaskDelete(@PathVariable(value = "id") long id,
                                Model model) {
        SubTask subTask = subTaskRepository.findById(id).get();



        subTaskRepository.delete(subTask);

        Task task = subTask.getTask();
        for (SubTask subTaskChangeIndex: task.getSubTasks()) {
            if (Utils.getIndex(subTaskChangeIndex.getSubTaskIndex())>Utils.getIndex(subTask.getSubTaskIndex())){
                subTaskChangeIndex.setSubTaskIndex(
                        task.getTaskIndex() +"." + String.valueOf(Utils.getIndex(subTaskChangeIndex.getSubTaskIndex())-1)
                );

            }
        }


        task.sortSubTasks();

        CommercialOffer selectedCommercialOffer = null;
        for (CommercialOffer co : task.getCommercialOffers()) {
            if (co.getStatus().equals("В работе")) selectedCommercialOffer = co;

        }

        TaskCalculation taskCalculation = new TaskCalculation(task);

        if (task.getPerformerName()!=null){
            if (task.getPerformerName().equals("Своя компания")){
                task.setProgress(Double.valueOf(taskCalculation.getProgressValue()));
            }
            else if (task.getPerformerName()!=null){
                if (task.getState().equals("Завершена")){
                    task.setProgress(100.0);
                }
                else task.setProgress(0.0);
            }
            else task.setProgress(0.0);
        }
        taskRepository.save(task);

        model.addAttribute("selectedCo", selectedCommercialOffer);

        model.addAttribute("task", task);
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);
        MaterialResourcesListDTO materialResourcesList = new MaterialResourcesListDTO();

        Iterable<MaterialResources> materialResources = materialResourcesRepository.findAll();

        for (MaterialResources materialResource : materialResources) {
            materialResourcesList.addResource(new MaterialResourcesDTO(materialResource));
        }

        model.addAttribute("materialResourcesList", materialResourcesList);

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        for (HumanResources humanResource : humanResources) {
            humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
        }
        model.addAttribute("humanResourcesList", humanResourcesList);
        int newSubTaskSubIndex = Utils.getIndex(task.findLastIndexOfSubTasks());
        String newSubTaskIndex = task.getTaskIndex() + "."+ String.valueOf(newSubTaskSubIndex+1);
        model.addAttribute("newSubTaskIndex",newSubTaskIndex);
        return "task-edit";


    }


    //Добавление подзадачи из страницы редактирвоания задачи
    @PostMapping("/task/{id}/add_subtask")
    public String AddSubTaskFromTask(@PathVariable(value = "id") long id,
                                     //Параметры для исполнителя
                                     @RequestParam String name,
                                     @RequestParam String subTaskIndex, @RequestParam(defaultValue = "2022-05-20") String startDate,
                                     @RequestParam(defaultValue = "0") Long duration, @RequestParam String progress,
                                     @RequestParam(defaultValue = "0") Long laboriousness,
                                     @RequestParam(defaultValue = "") String previousIndex,
                                     Model model) {

        Task task = taskRepository.findById(id).get();

        SubTask subTask = new SubTask(subTaskIndex, name, progress, laboriousness, previousIndex, LocalDate.parse(startDate), task);

        subTask.findDuration();

        subTaskRepository.save(subTask);

        task = taskRepository.findById(id).get();
        task.sortSubTasks();

        CommercialOffer selectedCommercialOffer = null;
        for (CommercialOffer co : task.getCommercialOffers()) {
            if (co.getStatus().equals("В работе")) selectedCommercialOffer = co;

        }

        TaskCalculation taskCalculation = new TaskCalculation(task);

        if (task.getPerformerName()!=null){
            if (task.getPerformerName().equals("Своя компания")){
                task.setProgress(Double.valueOf(taskCalculation.getProgressValue()));
            }
            else if (task.getPerformerName()!=null){
                if (task.getState().equals("Завершена")){
                    task.setProgress(100.0);
                }
                else task.setProgress(0.0);
            }
            else task.setProgress(0.0);
        }
        taskRepository.save(task);

        model.addAttribute("selectedCo", selectedCommercialOffer);

        model.addAttribute("task", task);
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);
        MaterialResourcesListDTO materialResourcesList = new MaterialResourcesListDTO();

        Iterable<MaterialResources> materialResources = materialResourcesRepository.findAll();

        for (MaterialResources materialResource : materialResources) {
            materialResourcesList.addResource(new MaterialResourcesDTO(materialResource));
        }

        model.addAttribute("materialResourcesList", materialResourcesList);

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        for (HumanResources humanResource : humanResources) {
            humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
        }
        model.addAttribute("humanResourcesList", humanResourcesList);
        int newSubTaskSubIndex = Utils.getIndex(task.findLastIndexOfSubTasks());
        String newSubTaskIndex = task.getTaskIndex() + "."+ String.valueOf(newSubTaskSubIndex+1);
        model.addAttribute("newSubTaskIndex",newSubTaskIndex);
        return "task-edit";
    }

}
