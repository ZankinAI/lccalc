package com.cpp.lccalc.controllers;

import com.cpp.lccalc.classes.*;
import com.cpp.lccalc.models.*;
import com.cpp.lccalc.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TaskController {


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

    @Autowired SubTaskRepository subTaskRepository;




    //Открытие страницы с задачей
    @GetMapping("/task/{id}")
    public String project(@PathVariable(value = "id") long id,  Model model) {

        Task task = taskRepository.findById(id).orElseThrow();
        Iterable<Project> projects = projectRopository.findAll();

        model.addAttribute("project", task.getProject());
        model.addAttribute("projects", projects);
        model.addAttribute("task", task);

        return "task";
    }

    //Страница редактирования подзадачи
    @GetMapping("/subtask/{id}/edit")
    public String subTaskEdit(@PathVariable(value = "id") long id,  Model model) {
        SubTask subTask = subTaskRepository.findById(id).orElseThrow();
        Task task = subTask.getTask();
        model.addAttribute("task", task);
        model.addAttribute("subtask", subTask);

        MaterialResourcesListDTO materialResourcesList = new MaterialResourcesListDTO();

        materialResourcesList.addResource(new MaterialResourcesDTO(true, 10L, "name 1"));
        materialResourcesList.addResource(new MaterialResourcesDTO(false, 11L, "name 2"));
        materialResourcesList.addResource(new MaterialResourcesDTO(false, 110L, "name 3"));
        materialResourcesList.addResource(new MaterialResourcesDTO(true, 140L, "name 4"));
        materialResourcesList.addResource(new MaterialResourcesDTO(true, 1L, "name 5"));

        model.addAttribute("materialResourcesList", materialResourcesList);

        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        humanResourcesList.addResource(new HumanResourcesDTO(true, 10L, 10L, "name 1"));
        humanResourcesList.addResource(new HumanResourcesDTO(false,10L, 11L, "name 2"));
        humanResourcesList.addResource(new HumanResourcesDTO(false, 10L,110L, "name 3"));
        humanResourcesList.addResource(new HumanResourcesDTO(true, 10L,140L, "name 4"));
        humanResourcesList.addResource(new HumanResourcesDTO(true, 10L,1L, "name 5"));

        model.addAttribute("humanResourcesList", humanResourcesList);

        return "subtask-edit";
    }



    //Страница редактирования задачи
    @GetMapping("/task/{id}/edit")
    public String taskEdit(@PathVariable(value = "id") long id,  Model model) {

        Task task = taskRepository.findById(id).orElseThrow();
        CommercialOffer selectedCommercialOffer = new CommercialOffer();
        for (CommercialOffer co:task.getCommercialOffers()) {
            if (co.getStatus().equals("В работе")) selectedCommercialOffer = co;

        }
        model.addAttribute("selectedCo",selectedCommercialOffer );
        task.sortSubTasks();
        TaskCalculation taskCalculation = new TaskCalculation(task);
        model.addAttribute("task", task);
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);

        Long idCO = Utils.getOptimalCommercialOfferId(task.getCommercialOffers());

        MaterialResourcesListDTO resourcesList = new MaterialResourcesListDTO();
        resourcesList.addResource(new MaterialResourcesDTO(true, 10L, "name 1"));
        resourcesList.addResource(new MaterialResourcesDTO(false, 11L, "name 2"));
        resourcesList.addResource(new MaterialResourcesDTO(false, 110L, "name 3"));
        resourcesList.addResource(new MaterialResourcesDTO(true, 140L, "name 4"));
        resourcesList.addResource(new MaterialResourcesDTO(true, 1L, "name 5"));
        model.addAttribute("resourcesList", resourcesList);

        return "task-edit";
    }

    //Тестирование чекбокса
    @PostMapping("/task/{id}/add_resources")
    public String test(@PathVariable(value = "id") long id, @ModelAttribute MaterialResourcesListDTO resourcesList, Model model){
        Task task = taskRepository.findById(id).orElseThrow();
        task.sortSubTasks();
        model.addAttribute("task", task);
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);

        model.addAttribute("resourcesList", resourcesList);

        return "task-edit";
    }

    //Обновление задачи
    @PostMapping("/task/{id}/edit")
    public String taskEditPost(@PathVariable(value = "id") long id,
                               @RequestParam String name,
                               @RequestParam String description, @RequestParam String taskIndex,
                               @RequestParam(defaultValue = "0") Long coId,
                               @RequestParam String startDate, Model model) {

        Task task = taskRepository.findById(id).orElseThrow();


        for (CommercialOffer co: task.getCommercialOffers()) {
            if (co.getCoId().equals(coId)){
                if (!co.getStatus().equals("В работе")){
                    co.setStatus("В работе");
                    commercialOfferRepository.save(co);
                }
            }
            else if (!co.getStatus().equals("Отклонено")){
                co.setStatus("Отклонено");
                commercialOfferRepository.save(co);
            }
        }
        task.setName(name);
        task.setDescription(description);
        task.setTaskIndex(taskIndex);

        if (!startDate.isEmpty()) task.setStartDate(LocalDate.parse(startDate));
        taskRepository.save(task);


        task = taskRepository.findById(id).orElseThrow();
        CommercialOffer selectedCommercialOffer = null;
        for (CommercialOffer co:task.getCommercialOffers()) {
            if (co.getStatus().equals("В работе")) selectedCommercialOffer = co;

        }
        if (selectedCommercialOffer!=null){
            task.setBudget(selectedCommercialOffer.getBudget());
            task.setDuration(selectedCommercialOffer.getDuration());
            task.setPerformerName(selectedCommercialOffer.getPerformer().getName());
        }

        taskRepository.save(task);
        model.addAttribute("selectedCo",selectedCommercialOffer );
        model.addAttribute("task", task);
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);

        Project project = task.getProject();
        project.sortTasks();
        for (Task taskOfProject: project.getTasks()) {
            taskOfProject.sortSubTasks();
        }

        model.addAttribute("project", project);

        Iterable<Customer> customers = customerRepository.findAll();
        Iterable<ProjectManager> projectManagers = projectManagerRepository.findAll();

        model.addAttribute("customers", customers);
        model.addAttribute("projectManagers", projectManagers);

        return "project-edit";
    }

    //Добавление подзадачи из страницы редактирвоания задачи
    @PostMapping("/task/{id}/add_subtask")
    public String AddSubTaskFromTask(@PathVariable(value = "id") long id,
                                //Параметры для исполнителя
                                @RequestParam String name,
                                @RequestParam String subTaskIndex, @RequestParam(defaultValue="2022-05-20") String startDate,
                                @RequestParam(defaultValue="0") Long duration, @RequestParam String progress,
                                @RequestParam(defaultValue="") String previousIndex,
                                Model model){

        Task task = taskRepository.findById(id).orElseThrow();

        SubTask subTask = new SubTask(subTaskIndex, name, progress, duration, previousIndex, LocalDate.parse(startDate), task);

        subTaskRepository.save(subTask);

        task = taskRepository.findById(id).orElseThrow();
        task.sortSubTasks();

        CommercialOffer selectedCommercialOffer = null;
        for (CommercialOffer co:task.getCommercialOffers()) {
            if (co.getStatus().equals("В работе")) selectedCommercialOffer = co;

        }

        TaskCalculation taskCalculation = new TaskCalculation(task);

        model.addAttribute("selectedCo",selectedCommercialOffer );

        model.addAttribute("task", task);
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);

        MaterialResourcesListDTO materialResourcesList = new MaterialResourcesListDTO();
        materialResourcesList.addResource(new MaterialResourcesDTO(true, 10L, "name 1"));
        materialResourcesList.addResource(new MaterialResourcesDTO(false, 11L, "name 2"));
        materialResourcesList.addResource(new MaterialResourcesDTO(false, 110L, "name 3"));
        materialResourcesList.addResource(new MaterialResourcesDTO(true, 140L, "name 4"));
        materialResourcesList.addResource(new MaterialResourcesDTO(true, 1L, "name 5"));
        model.addAttribute("materialResourcesList", materialResourcesList);
        return "task-edit";
    }

    //Добавление коммерческого предложения из страницы редактирвоания задачи
    @PostMapping("/co/{id}/add_from_task")
    public String coAddFromTask(@PathVariable(value = "id") long id,
                                //Параметры для исполнителя
                                @RequestParam Long performerId,
                                @RequestParam String name,
                                @RequestParam String description, @RequestParam String director,
                                @RequestParam String industry, @RequestParam String telephone,
                                @RequestParam String address, @RequestParam String site,
                                //Параметры для КП
                                @RequestParam Long duration, @RequestParam Long budget,
                                      Model model){

        Performer performer;
        Task task = taskRepository.findById(id).orElseThrow();
        if (performerId!=0)
        {
            performer = performerRepository.findById(performerId).orElseThrow();
        }
        else  if (!name.isEmpty()){
            performer = new Performer(name,description,industry,telephone,site,address,director);
            performerRepository.save(performer);
        }
        else return "redirect:task-edit";

        CommercialOffer commercialOffer = new CommercialOffer(duration,budget,"На рассмотрении");
        commercialOffer.setPerformer(performer);
        commercialOffer.setTask(task);
        commercialOfferRepository.save(commercialOffer);
        task = taskRepository.findById(id).orElseThrow();

        Long idOptimalCO = Utils.getOptimalCommercialOfferId(task.getCommercialOffers());

        for (CommercialOffer co:task.getCommercialOffers()) {
            if (co.getCoId().equals(idOptimalCO))
                if (!co.isOptimal())
                {
                    co.setOptimal(true);
                    commercialOfferRepository.save(co);
                }
            else if (co.isOptimal()){
                    co.setOptimal(false);
                    commercialOfferRepository.save(co);
                }
        }
        task = taskRepository.findById(id).orElseThrow();

        CommercialOffer selectedCommercialOffer = new CommercialOffer();
        for (CommercialOffer co:task.getCommercialOffers()) {
            if (co.getStatus().equals("В работе")) selectedCommercialOffer = co;

        }
        model.addAttribute("selectedCo",selectedCommercialOffer );

        model.addAttribute("task", task);
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);
        MaterialResourcesListDTO resourcesList = new MaterialResourcesListDTO();
        resourcesList.addResource(new MaterialResourcesDTO(true, 10L, "name 1"));
        resourcesList.addResource(new MaterialResourcesDTO(false, 11L, "name 2"));
        resourcesList.addResource(new MaterialResourcesDTO(false, 110L, "name 3"));
        resourcesList.addResource(new MaterialResourcesDTO(true, 140L, "name 4"));
        resourcesList.addResource(new MaterialResourcesDTO(true, 1L, "name 5"));
        model.addAttribute("resourcesList", resourcesList);
        return "task-edit";
    }

    @GetMapping(value = "/categories/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Category> getCategories(@PathVariable(value = "id") long id){
        List<Category> categories = new ArrayList<>();

        Category category = new Category(LocalDate.of(2014, 8, 1), LocalDate.of(2014, 8, 31), "Август 2014");
        categories.add(category);

        if (id == 1)
            category = new Category(LocalDate.of(2014, 9, 1), LocalDate.of(2014, 9, 30), "сработало 2014");
        else category = new Category(LocalDate.of(2014, 9, 1), LocalDate.of(2014, 9, 30), "Сентябрь 2014");
        categories.add(category);

        category = new Category(LocalDate.of(2014, 10, 1), LocalDate.of(2014, 10, 31), "Октябрь 2014");
        categories.add(category);

        category = new Category(LocalDate.of(2014, 11, 1), LocalDate.of(2014, 11, 30), "Ноябрь 2014");
        categories.add(category);

        category = new Category(LocalDate.of(2014, 12, 1), LocalDate.of(2014, 12, 31), "Декабрь 2014");
        categories.add(category);

        category = new Category(LocalDate.of(2015, 1, 1), LocalDate.of(2015, 1, 31), "янаврь 2015");
        categories.add(category);

        category = new Category(LocalDate.of(2015, 2, 1), LocalDate.of(2015, 2, 28), "февраль 2015");
        categories.add(category);

        category = new Category(LocalDate.of(2015, 3, 1), LocalDate.of(2015, 3, 31), "март 2015");
        categories.add(category);

        return categories;
    }

    @GetMapping(value = "/gantt/{id}")
        public String getGantt(@PathVariable(value = "id") long id,Model model){
        model.addAttribute("id", id);
        return "/gantt";
    }

    @GetMapping(value = "/gantt_project/{id}")
    public String getGanttProject(@PathVariable(value = "id") long id,Model model){
        model.addAttribute("id", id);
        return "gantt-project";
    }

}
