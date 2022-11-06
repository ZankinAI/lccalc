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

    @Autowired
    private MaterialResourcesRepository materialResourcesRepository;

    @Autowired
    private MaterialResourcesSubTaskRepository materialResourcesSubTaskRepository;




    //Открытие страницы с задачей
    @GetMapping("/task/{id}")
    public String task(@PathVariable(value = "id") long id,  Model model) {

        Task task = taskRepository.findById(id).orElseThrow();
        Iterable<Project> projects = projectRopository.findAll();

        model.addAttribute("project", task.getProject());
        model.addAttribute("projects", projects);
        model.addAttribute("task", task);

        return "task";
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

        TaskCalculation taskCalculation = new TaskCalculation(task);
        //task.setDuration(taskCalculation.getDuration());

        /*if (task.getStartDate()!=null){
            for (SubTask subTask: task.getSubTasks()) {
                subTask.setStartDate(taskCalculation.findChildById(subTask.getSubTaskIndex()).getStartDate());
                subTaskRepository.save(subTask);
            }
        }*/

        //taskRepository.save(task);
        task.sortSubTasks();
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



    //Обновление задачи
    @PostMapping("/task/{id}/edit")
    public String taskEditPost(@PathVariable(value = "id") long id,
                               @RequestParam String name,
                               @RequestParam String description, @RequestParam String taskIndex,
                               @RequestParam(defaultValue = "0") Long coId,
                               @RequestParam String startDate, Model model) {

        Task task = taskRepository.findById(id).orElseThrow();
        TaskCalculation taskCalculation = new TaskCalculation(task);
        if (coId.equals(0L)){
            task.setDuration(taskCalculation.getDuration());
            task.setBudget(task.findBudgetBySubTasks());
            task.setPerformerName("Своя компания");
            for (CommercialOffer co: task.getCommercialOffers()){
                if (!co.getStatus().equals("Отклонено")){
                    co.setStatus("Отклонено");
                    commercialOfferRepository.save(co);
                }
            }

        } else {
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
        }


        task.setName(name);
        task.setDescription(description);
        task.setTaskIndex(taskIndex);

        if (!startDate.isEmpty()){
            task.setStartDate(LocalDate.parse(startDate));
            for (SubTask subTask: task.getSubTasks()) {
                subTask.setStartDate(taskCalculation.findChildById(subTask.getSubTaskIndex()).getStartDate());
                subTaskRepository.save(subTask);
            }
        }
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

        for (Task taskOfProject: project.getTasks()) {
            taskOfProject.sortSubTasks();
        }
        project.findBudget();
        projectRopository.save(project);
        project.sortTasks();
        model.addAttribute("project", project);

        Iterable<Customer> customers = customerRepository.findAll();
        Iterable<ProjectManager> projectManagers = projectManagerRepository.findAll();

        model.addAttribute("customers", customers);
        model.addAttribute("projectManagers", projectManagers);

        return "project-edit";
    }

    //Удаление задачи
    @GetMapping(path = "/task/{id}/remove")
    public String taskRemove(@PathVariable(value = "id") long id, Model model){

        Task task = taskRepository.findById(id).orElseThrow();


        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);

        taskRepository.delete(task);
        Project project = task.getProject();
        project.sortTasks();
        for (Task taskOfProject: project.getTasks()) {
            taskOfProject.sortSubTasks();
        }
        project.findBudget();
        projectRopository.save(project);

        model.addAttribute("project", project);

        Iterable<Customer> customers = customerRepository.findAll();
        Iterable<ProjectManager> projectManagers = projectManagerRepository.findAll();

        model.addAttribute("customers", customers);
        model.addAttribute("projectManagers", projectManagers);
        return "project-edit";
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



    //Открыть страницу с рисками
    @GetMapping(value = "/risks")
    public String risks(Model model){

        Risk risk1 = new Risk("risk1", 1, 1);
        Risk risk2 = new Risk("risk2", 1, 2);
        Risk risk3 = new Risk("risk2", 1, 3);
        Risk risk4 = new Risk("risk2", 1, 4);
        Risk risk5 = new Risk("risk2", 1, 5);
        Risk risk6 = new Risk("risk1", 2, 1);
        Risk risk7 = new Risk("risk2", 2, 2);
        Risk risk8 = new Risk("risk2", 2, 3);
        Risk risk9 = new Risk("risk2", 2, 4);
        Risk risk10 = new Risk("risk2", 2, 5);
        Risk risk11 = new Risk("risk1", 3, 1);
        Risk risk12 = new Risk("risk2", 3, 2);
        Risk risk13 = new Risk("risk2", 3, 3);
        Risk risk14 = new Risk("risk2", 3, 4);
        Risk risk15 = new Risk("risk2", 3, 5);
        Risk risk16 = new Risk("risk1", 4, 1);
        Risk risk17 = new Risk("risk2", 4, 2);
        Risk risk18 = new Risk("risk2", 4, 3);
        Risk risk19 = new Risk("risk2", 4, 4);
        Risk risk20 = new Risk("risk2", 4, 5);
        Risk risk21 = new Risk("risk1", 5, 1);
        Risk risk22 = new Risk("risk2", 5, 2);
        Risk risk23 = new Risk("risk2", 5, 3);
        Risk risk24 = new Risk("risk2", 5, 4);
        Risk risk25 = new Risk("risk2", 5, 5);
        Risk[] risks = {risk1, risk2, risk3, risk4, risk5, risk6, risk7,risk8,risk9,risk10,risk11,risk12,risk13,risk14,risk15,risk16,risk17,risk18,risk19,risk20,risk21,risk22,risk23,risk24,risk25};
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

}
