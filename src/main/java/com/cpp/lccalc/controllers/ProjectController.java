package com.cpp.lccalc.controllers;

import com.cpp.lccalc.models.Customer;
import com.cpp.lccalc.models.Project;
import com.cpp.lccalc.models.ProjectManager;
import com.cpp.lccalc.models.Task;
import com.cpp.lccalc.repo.CustomerRepository;
import com.cpp.lccalc.repo.ProjectManagerRepository;
import com.cpp.lccalc.repo.ProjectRopository;
import com.cpp.lccalc.repo.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

@Controller
public class ProjectController {

    @Autowired
    private ProjectRopository projectRopository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProjectManagerRepository projectManagerRepository;

    @Autowired
    private TaskRepository taskRepository;


    //Открытие страницы с проектом
    @GetMapping("/project/{id}")
    public String project(@PathVariable(value = "id") long id,  Model model) {

        Project project = projectRopository.findById(id).orElseThrow();


        Iterable<Project> projects = projectRopository.findAll();

        model.addAttribute("project", project);
        model.addAttribute("projects", projects);

        return "project";
    }

    //Выбор проекта на сранице с проектом
    @PostMapping(path = "/project/{id}", params = "projectId")
    public String projectPostSelect(@RequestParam String projectId,  Model model){

        Project project = projectRopository.findById(Long.valueOf(projectId)).orElseThrow();
        Iterable<Project> projects = projectRopository.findAll();
        model.addAttribute("project", project);
        model.addAttribute("projects", projects);
        return "project";
    }


    //Открытие сраницы с пректами
    @GetMapping("/projects")
    public String projects(Model model) {
        Iterable<Project> projectsStatus1 = projectRopository.findByStatus(1);
        model.addAttribute("projects1", projectsStatus1);

        return "projects";
    }

    //Отекрытие страницы создания проекта
    @GetMapping("/project/add")
    public String projectAdd(Model model) {

        Iterable<Customer> customers = customerRepository.findAll();
        Iterable<ProjectManager> projectManagers = projectManagerRepository.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("projectManagers", projectManagers);

        return "project-add";
    }

    //Добавление заказчика проекта во вкладке добавления проекта
    @PostMapping(path = "/project/add", params = "customerName")
    public String projectAddCustomer(@RequestParam String customerName,  @RequestParam String director,
                                     @RequestParam String industry,  @RequestParam String telephone,
                                     @RequestParam String site, @RequestParam String address,
                                     Model model){
        Customer addCustomer = new Customer(customerName, industry, telephone, site, address, director);
        customerRepository.save(addCustomer);
        Iterable<Customer> customers = customerRepository.findAll();
        Iterable<ProjectManager> projectManagers = projectManagerRepository.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("projectManagers", projectManagers);

        return "project-add";
    }

    //Добавление ответственного по проекту во вкладке добавления проекта
    @PostMapping(path = "/project/add", params = "fio")
    public String projectAddManager(@RequestParam String fio,  @RequestParam String post,
                                    @RequestParam String telephone, @RequestParam String email,
                                     Model model){

        ProjectManager addPm = new ProjectManager(fio, telephone, email, post);
        projectManagerRepository.save(addPm);
        Iterable<Customer> customers = customerRepository.findAll();
        Iterable<ProjectManager> projectManagers = projectManagerRepository.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("projectManagers", projectManagers);

        return "project-add";
    }

    //Добавление проекта
    @PostMapping(path = "/project/add", params = "projectName")
    public String projectAddProject(@RequestParam String projectName,  @RequestParam String description,
                                    @RequestParam Long budget,  @RequestParam String startDate,
                                    @RequestParam String finishDate, @RequestParam Long customerId,
                                    @RequestParam Long pmId, Model model){


        LocalDate localStartDate = LocalDate.parse(startDate);
        LocalDate localFinishDate = LocalDate.parse(finishDate);
        Project addProject = new Project(projectName, description, budget, 1, localStartDate, localFinishDate);

        Customer customer = customerRepository.findById(customerId).orElseThrow();
        addProject.setCustomer(customer);

        ProjectManager projectManager = projectManagerRepository.findById(pmId).orElseThrow();
        addProject.setProjectManager(projectManager);

        addProject.setCreateDate(LocalDate.now());
        projectRopository.save(addProject);

        Iterable<Project> projectsStatus1 = projectRopository.findByStatus(1);
        model.addAttribute("projects1", projectsStatus1);
        return "redirect:/projects";
    }


    //Открытие страницы редактирования проекта
    @GetMapping("/project/{id}/edit")
    public String projectEdit(@PathVariable(value = "id") long id,  Model model) {
        Optional<Project> projectOptional =  projectRopository.findById(id);
        ArrayList<Project> projectRes = new ArrayList<>();
        projectOptional.ifPresent(projectRes::add);
        Project project = projectRes.get(0);

        model.addAttribute("project", project);

        Iterable<Customer> customers = customerRepository.findAll();
        Iterable<ProjectManager> projectManagers = projectManagerRepository.findAll();

        model.addAttribute("customers", customers);
        model.addAttribute("projectManagers", projectManagers);


        return "project-edit";
    }

    //Добавление заказчика проекта во вкладке редактирования проекта
    @PostMapping(path = "/project/{id}/edit", params = "customerName")
    public String projectAddCustomerInEditPage(@PathVariable(value = "id") long id, @RequestParam String customerName,  @RequestParam String director,
                                     @RequestParam String industry,  @RequestParam String telephone,
                                     @RequestParam String site, @RequestParam String address,
                                     Model model){
        Customer addCustomer = new Customer(customerName, industry, telephone, site, address, director);
        customerRepository.save(addCustomer);
        Iterable<Customer> customers = customerRepository.findAll();
        Iterable<ProjectManager> projectManagers = projectManagerRepository.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("projectManagers", projectManagers);

        Optional<Project> projectOptional =  projectRopository.findById(id);
        ArrayList<Project> projectRes = new ArrayList<>();
        projectOptional.ifPresent(projectRes::add);
        Project project = projectRes.get(0);

        Iterable<Project> projects = projectRopository.findAll();

        model.addAttribute("project", project);

        return "project-edit";
    }

    //Добавление ответственного по проекту во вкладке редактирования проекта
    @PostMapping(path = "/project/{id}/edit", params = "fio")
    public String projectAddManagerInEditPage(@PathVariable(value = "id") long id, @RequestParam String fio,  @RequestParam String post,
                                    @RequestParam String telephone, @RequestParam String email,
                                    Model model){

        ProjectManager addPm = new ProjectManager(fio, telephone, email, post);
        projectManagerRepository.save(addPm);
        Iterable<Customer> customers = customerRepository.findAll();
        Iterable<ProjectManager> projectManagers = projectManagerRepository.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("projectManagers", projectManagers);

        Optional<Project> projectOptional =  projectRopository.findById(id);
        ArrayList<Project> projectRes = new ArrayList<>();
        projectOptional.ifPresent(projectRes::add);
        Project project = projectRes.get(0);

        Iterable<Project> projects = projectRopository.findAll();

        model.addAttribute("project", project);

        return "project-edit";
    }

    //Добавление ответственного по проекту во вкладке редактирования проекта
    @PostMapping("/project/{id}/edit/taskAdd")
    public String taskAdd(@PathVariable(value = "id") long id, @RequestParam String name,
                          @RequestParam String description, Model model) {


        Project project = projectRopository.findById(id).orElseThrow();
        Task task = new Task(name, description);
        task.setProject(project);
        task.setState("Не начата");
        taskRepository.save(task);
        project = projectRopository.findById(id).orElseThrow();

        model.addAttribute("project", project);
        Iterable<Customer> customers = customerRepository.findAll();
        Iterable<ProjectManager> projectManagers = projectManagerRepository.findAll();
        model.addAttribute("customers", customers);
        model.addAttribute("projectManagers", projectManagers);

        return "project-edit";
    }

    //Обновление проекта
    @PostMapping(path = "/project/{id}/edit", params = "projectName")
    public String projectEdit(@PathVariable(value = "id") long id, @RequestParam String projectName,  @RequestParam String description,
                                    @RequestParam Long budget,  @RequestParam String startDate,
                                    @RequestParam String finishDate, @RequestParam Long customerId,
                                    @RequestParam Long pmId, Model model){


        LocalDate localStartDate = LocalDate.parse(startDate);
        LocalDate localFinishDate = LocalDate.parse(finishDate);

        Optional<Project> projectOptional =  projectRopository.findById(id);
        ArrayList<Project> projectRes = new ArrayList<>();
        projectOptional.ifPresent(projectRes::add);
        Project projectEdit = projectRes.get(0);

        projectEdit.setProjectName(projectName);
        projectEdit.setDescription(description);
        projectEdit.setBudget(budget);
        projectEdit.setStartDateFormat(localStartDate);
        projectEdit.setFinishDateFormat(localFinishDate);;

        Optional<Customer> customerOptional =  customerRepository.findById(customerId);
        ArrayList<Customer> customerRes = new ArrayList<>();
        customerOptional.ifPresent(customerRes::add);
        Customer customer = customerRes.get(0);
        projectEdit.setCustomer(customer);

        Optional<ProjectManager> managerOptional =  projectManagerRepository.findById(pmId);
        ArrayList<ProjectManager> pmRes = new ArrayList<>();
        managerOptional.ifPresent(pmRes::add);
        ProjectManager projectManager = pmRes.get(0);
        projectEdit.setProjectManager(projectManager);

        projectRopository.save(projectEdit);

        Iterable<Project> projectsStatus1 = projectRopository.findByStatus(1);
        model.addAttribute("projects1", projectsStatus1);
        return "redirect:/projects";
    }


    //Удаление проекта
    @PostMapping(path = "/project/{id}/remove")
    public String projectRemove(@PathVariable(value = "id") long id, Model model){

        Project project = projectRopository.findById(id).orElseThrow();
        projectRopository.delete(project);

        return "redirect:/projects";
    }



}