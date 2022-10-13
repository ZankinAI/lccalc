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

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class TaskController {


    @Autowired
    private ProjectRopository projectRopository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProjectManagerRepository projectManagerRepository;

    @Autowired
    private TaskRepository taskRepository;



    //Открытие страницы с проектом
    @GetMapping("/task/{id}")
    public String project(@PathVariable(value = "id") long id,  Model model) {

        Task task = taskRepository.findById(id).orElseThrow();
        Iterable<Project> projects = projectRopository.findAll();

        model.addAttribute("project", task.getProject());
        model.addAttribute("projects", projects);
        model.addAttribute("task", task);

        return "task";
    }

    @PostMapping("/task/{id}/add")
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

}
