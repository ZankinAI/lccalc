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

    @Autowired
    private RiskRepository riskRepository;

    @Autowired
    private RiskSolutionRepository riskSolutionRepository;

    //Открытие страницы с задачей
    @GetMapping("/task/{id}")
    public String task(@PathVariable(value = "id") long id,  Model model) {

        Task task = taskRepository.findById(id).orElseThrow();
        Iterable<Project> projects = projectRopository.findAll();

        task.sortSubTasks();
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
                               @RequestParam String startDate, @RequestParam String status, Model model) {

        Task task = taskRepository.findById(id).orElseThrow();


        task.setName(name);
        task.setDescription(description);
        task.setTaskIndex(taskIndex);
        task.setState(status);

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

        if (!startDate.isEmpty()){
            task.setStartDate(LocalDate.parse(startDate));
            taskCalculation = new TaskCalculation(task);
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
            if (co.getCoId().equals(idOptimalCO)){
                if (!co.isOptimal())
                {
                    co.setOptimal(true);
                    commercialOfferRepository.save(co);
                }
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

    //Открыть страницу с рисками
    @GetMapping(value = "/project/{id}/risks")
    public String risks(@PathVariable(value = "id") Long id, Model model){

        Project project = projectRopository.findById(id).orElseThrow();
        Iterable<Project> projects = projectRopository.findAll();

        project.sortRisks();

        Iterable<Risk> risks = riskRepository.findAll();
        model.addAttribute("risks", risks);
        model.addAttribute("project", project);
        model.addAttribute("projects", projects);
        return "risks";
    }

    @GetMapping(value = "/project/0/risks")
    public String risks(Model model){

        Project project = new Project();
        Iterable<Project> projects = projectRopository.findAll();
        model.addAttribute("project", project);
        model.addAttribute("projects", projects);
        return "risks";
    }

    //Выбор проекта на сранице с рисками
    @PostMapping(path = "/project/{id}/risks", params = "projectId")
    public String projectPostSelectRisks(@RequestParam String projectId,  Model model){

        Project project = projectRopository.findById(Long.valueOf(projectId)).orElseThrow();
        Iterable<Project> projects = projectRopository.findAll();
        project.sortRisks();

        Iterable<Risk> risks = riskRepository.findAll();
        model.addAttribute("risks", risks);
        model.addAttribute("project", project);
        model.addAttribute("projects", projects);
        return "risks";
    }

    //Страница редактирования риска
    @GetMapping("/risk/{id}/edit")
    public String riskEdit(@PathVariable(value = "id") Long id,  Model model) {
        Risk risk = riskRepository.findById(id).orElseThrow();
        Iterable<RiskSolution> riskSolution = riskSolutionRepository.findAll();
        //Risk risk = new Risk("risk1", 1, 1);
        RiskSolution selectedSolution = new RiskSolution();
        if (risk.getSelectedRiskSolutionID() != null)
            if (risk.getSelectedRiskSolutionID() == 0L)
                risk.setSelectedRiskSolutionID(null);
            else
                selectedSolution = riskSolutionRepository.findById(risk.getSelectedRiskSolutionID()).orElseThrow();
        model.addAttribute("selectedSolution", selectedSolution);
        model.addAttribute("risk", risk);
        model.addAttribute("riskSolution", riskSolution);
        return "risk-edit";
    }

    //Обновление риска
    @PostMapping("/risk/{id}/edit" )
    public String riskEditPost(@PathVariable(value = "id") Long id, Model model,
                               @RequestParam String name,
                               @RequestParam double likelihood,
                               @RequestParam double consequence,
                               @RequestParam(defaultValue = "0") Long solutionId){

        RiskSolution selectedRiskSolution = new RiskSolution();
        if (solutionId != 0L)
            selectedRiskSolution = riskSolutionRepository.findById(solutionId).orElseThrow();
        else solutionId = null;
        Risk risk = riskRepository.findById(id).orElseThrow();
        Project project = risk.getProject();
        project.sortRisks();
        risk.setName(name);
        risk.setLikelihood(likelihood);
        risk.setConsequence(consequence);
        risk.update();
        risk.setSelectedRiskSolutionID(solutionId);
        riskRepository.save(risk);

        Iterable<RiskSolution> riskSolution = riskSolutionRepository.findAll();

        Iterable<Risk> risks = riskRepository.findAll();
        model.addAttribute("risks", risks);
        model.addAttribute("project", project);
        model.addAttribute("risk", risk);
        model.addAttribute("riskSolution", riskSolution);
        return "risks";
    }


    //Добавление риска
    @PostMapping("/project/{id}/add_risk")
    public String AddRisk(         @PathVariable Long id,  //Параметры для исполнителя
                                     @RequestParam String name,
                                     @RequestParam int likelihood,
                                     @RequestParam int consequence,
                                     Model model){
        Project project = projectRopository.findById(id).orElseThrow();

        Risk risk = new Risk(name,likelihood,consequence);
        risk.setProject(project);
        riskRepository.save(risk);
        project = projectRopository.findById(id).orElseThrow();
        project.sortRisks();


        RiskSolution selectedSolution = new RiskSolution();
        if (risk.getSelectedRiskSolutionID() != null)
            selectedSolution = riskSolutionRepository.findById(risk.getSelectedRiskSolutionID()).orElseThrow();

        //Iterable<Risk> risks = riskRepository.findAll();
        //model.addAttribute("risks", risks);

        model.addAttribute("selectedSolution", selectedSolution);
        model.addAttribute("project", project);
        return "/risks";
    }

    //Добавление обработки риска
    @PostMapping("/risk/{id}/add_solution")
    public String AddSolution (@PathVariable(value = "id") Long id, Model model, @RequestParam String name, @RequestParam int probability, @RequestParam Long cost, @RequestParam int reduction){
        Risk risk = riskRepository.findById(id).orElseThrow();

        RiskSolution riskSolution = new RiskSolution(name, probability, cost, reduction);
        riskSolution.setRisk(risk);
        riskSolutionRepository.save(riskSolution);

        risk = riskRepository.findById(id).orElseThrow();
        model.addAttribute("risk", risk);

        RiskSolution selectedSolution = new RiskSolution();
        if (risk.getSelectedRiskSolutionID() != null)
            selectedSolution = riskSolutionRepository.findById(risk.getSelectedRiskSolutionID()).orElseThrow();
        model.addAttribute("selectedSolution", selectedSolution);
        Iterable<RiskSolution> riskSolution1 = riskSolutionRepository.findAll();
        model.addAttribute("riskSolution", riskSolution1);
        return "risk-edit";
    }


    @GetMapping(value = "/gantt/{id}")
        public String getGantt(@PathVariable(value = "id") long id,Model model){
        model.addAttribute("id", id);
        return "/gantt";
    }

    @GetMapping(value = "/gantt_project/{id}")
    public String getGanttProject(@PathVariable(value = "id") long id,Model model){
        model.addAttribute("id", id);
        Project project = projectRopository.findById(id).orElseThrow();
        Iterable <Project> projects = projectRopository.findAll();
        model.addAttribute("project", project);
        model.addAttribute("projects", projects);
        return "gantt-project";
    }

    @GetMapping(value = "/gantt_project/0")
    public String getGanttProject(Model model){
        Iterable <Project> projects = projectRopository.findAll();
        Project project = new Project();
        model.addAttribute("projects", projects);
        model.addAttribute("project", project);
        return "gantt-project";
    }
    //Выбор проекта на сранице с план-графиком
    @PostMapping(path = "/gantt_project/{id}", params = "projectId")
    public String projectPostSelectGantt(@RequestParam String projectId,  Model model){
        Project project = projectRopository.findById(Long.valueOf(projectId)).orElseThrow();
        Iterable<Project> projects = projectRopository.findAll();
        model.addAttribute("id", projectId);
        model.addAttribute("project", project);
        model.addAttribute("projects", projects);
        return "gantt-project";
    }
}
