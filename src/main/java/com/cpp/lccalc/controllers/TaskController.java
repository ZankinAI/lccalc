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

    @Autowired
    private HumanResourceRepository humanResourceRepository;

    @Autowired
    private HumanResourceSubTaskRepository humanResourceSubTaskRepository;




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

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        for (HumanResources humanResource: humanResources) {
            humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
        }
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
        task.setDuration(taskCalculation.getDuration());
        taskRepository.save(task);
        model.addAttribute("task", task);
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);

        Long idCO = Utils.getOptimalCommercialOfferId(task.getCommercialOffers());

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        for (HumanResources humanResource: humanResources) {
            humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
        }
        model.addAttribute("humanResourcesList", humanResourcesList);

        return "task-edit";
    }

/*    //Обновление материальных ресурсов в подзадаче
    @PostMapping("/subtask/{id}/add_material_resources")
    public String test(@PathVariable(value = "id") long id, @ModelAttribute MaterialResourcesListDTO resourcesList, Model model){
        SubTask subTask = subTaskRepository.findById(id).orElseThrow();

        MaterialResourcesSubTask materialResourcesSubTask = null;
        for (MaterialResourcesDTO materialresourcesDTO: resourcesList.getMaterialResources()) {
            materialResourcesSubTask = null;
            if (materialresourcesDTO.isChecked()){
                materialResourcesSubTask = subTask.findResourceById(materialresourcesDTO.getId());
                if (materialResourcesSubTask == null){
                    MaterialResourcesSubTask materialResourcesSubTask1 = new MaterialResourcesSubTask(materialresourcesDTO.getAmount(), materialResourceRepository.findById(materialresourcesDTO.getId()).orElseThrow(), subTask);
                    materialResourceSubTaskRepository.save(materialResourcesSubTask1);
                }
                else {
                    MaterialResourcesSubTask materialResourcesSubTaskEdit = materialResourceSubTaskRepository.findById(materialResourcesSubTask.getMaterialResourceSubTaskId()).orElseThrow();
                    materialResourcesSubTaskEdit.setAmount(materialresourcesDTO.getAmount());
                    materialResourceSubTaskRepository.save(materialResourcesSubTaskEdit);
                }

            }

        }

        subTask = subTaskRepository.findById(id).orElseThrow();
        subTask.findDuration();
        subTaskRepository.save(subTask);

        model.addAttribute("subtask", subTask);

        model.addAttribute("task", subTask.getTask());
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);

        model.addAttribute("resourcesList", resourcesList);

        Iterable<MaterialResources> materialResources = materialResourceRepository.findAll();
        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        for (HumanResources humanResource: humanResources) {
            humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
        }
        model.addAttribute("humanResourcesList", humanResourcesList);

        return "subtask-edit";
    }*/

    //Обновление человеческих ресурсов в подзадаче
    @PostMapping("/subtask/{id}/add_human_resources")
    public String test(@PathVariable(value = "id") long id, @ModelAttribute HumanResourcesListDTO resourcesList, Model model){
        SubTask subTask = subTaskRepository.findById(id).orElseThrow();

        HumanResourcesSubTask humanResourcesSubTask = null;
        for (HumanResourcesDTO humanresourcesDTO: resourcesList.getHumanResources()) {
            humanResourcesSubTask = null;
            if (humanresourcesDTO.isChecked()){
                humanResourcesSubTask = subTask.findResourceById(humanresourcesDTO.getId());
                if (humanResourcesSubTask == null){
                    HumanResourcesSubTask humanResourcesSubTask1 = new HumanResourcesSubTask(humanresourcesDTO.getAmount(), humanResourceRepository.findById(humanresourcesDTO.getId()).orElseThrow(), subTask);
                    humanResourceSubTaskRepository.save(humanResourcesSubTask1);
                }
                else {
                    HumanResourcesSubTask humanResourcesSubTaskEdit = humanResourceSubTaskRepository.findById(humanResourcesSubTask.getHumanResourceSubTaskId()).orElseThrow();
                    humanResourcesSubTaskEdit.setAmount(humanresourcesDTO.getAmount());
                    humanResourceSubTaskRepository.save(humanResourcesSubTaskEdit);
                }

            }

        }

        subTask = subTaskRepository.findById(id).orElseThrow();
        subTask.findDuration();
        subTaskRepository.save(subTask);

        model.addAttribute("subtask", subTask);

        model.addAttribute("task", subTask.getTask());
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);

        model.addAttribute("resourcesList", resourcesList);

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        for (HumanResources humanResource: humanResources) {
            humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
        }
        model.addAttribute("humanResourcesList", humanResourcesList);

        return "subtask-edit";
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
                                @RequestParam(defaultValue="0") Long laboriousness,
                                @RequestParam(defaultValue="") String previousIndex,
                                Model model){

        Task task = taskRepository.findById(id).orElseThrow();

        SubTask subTask = new SubTask(subTaskIndex, name, progress, laboriousness, previousIndex, LocalDate.parse(startDate), task);

        subTask.findDuration();

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

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        for (HumanResources humanResource: humanResources) {
            humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
        }
        model.addAttribute("humanResourcesList", humanResourcesList);

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

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        for (HumanResources humanResource: humanResources) {
            humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
        }
        model.addAttribute("humanResourcesList", humanResourcesList);

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

    //Открыть страницу с ресурсами
    @GetMapping(value = "/resources")
    public String resources(Model model){

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        model.addAttribute("humanResources", humanResources);
        return "resources";
    }

    //Открыть страницу с рисками
    @GetMapping(value = "/risks")
    public String risks(Model model){

        Risk risk1 = new Risk("risk1", "1", "1");
        Risk risk2 = new Risk("risk2", "2", "2");
        Risk[] risks = {risk1, risk2};
        //Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        model.addAttribute("risks", risks);
        return "risks";
    }

    //Добавление риска
    @PostMapping("/risks/add_risk")
    public String AddRisk(           //Параметры для исполнителя
                                     @RequestParam String name,
                                     @RequestParam String likelihood,
                                     @RequestParam String consequence,
                                     Model model){

        model.addAttribute("name", name);
        model.addAttribute("likelihood", likelihood);
        model.addAttribute("consequence", consequence);
        return "risks";
    }

    @PostMapping(value = "/resources/add_human_resource")
    public String addHumanResource(@RequestParam String name, @RequestParam Long tariff,
                                   @RequestParam Long amount,
                                   Model model){

        HumanResources humanResource = new HumanResources(tariff, amount, name);
        humanResourceRepository.save(humanResource);
        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        model.addAttribute("humanResources", humanResources);

        return "resources";
    }
}
