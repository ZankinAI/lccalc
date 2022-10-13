package com.cpp.lccalc.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;
    private String projectName, description;
    private Long  budget;
    private int status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDateFormat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate finishDateFormat;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate createDate;




    @ManyToOne
    @JoinColumn(name="customer_id", nullable=true)
    private Customer customer;


    @ManyToOne
    @JoinColumn(name="pm_id", nullable=true)
    private ProjectManager projectManager;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true )
    private Set<Task> tasks;


    public Project() {
    }

    public Project(String projectName, String description, Long budget, int status, LocalDate startDateFormat, LocalDate finishDateFormat) {
        this.projectName = projectName;
        this.description = description;
        this.budget = budget;
        this.status = status;
        this.startDateFormat = startDateFormat;
        this.finishDateFormat = finishDateFormat;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    public void setProjetManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public LocalDate getStartDateFormat() {
        return startDateFormat;
    }

    public void setStartDateFormat(LocalDate startDateFormat) {
        this.startDateFormat = startDateFormat;
    }

    public LocalDate getFinishDateFormat() {
        return finishDateFormat;
    }

    public void setFinishDateFormat(LocalDate finishDateFormat) {
        this.finishDateFormat = finishDateFormat;
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate = createDate;
    }


    public Set<Task> getTasks() {
        return tasks;
    }

    public void setTasks(Set<Task> tasks) {
        this.tasks = tasks;
    }


}
