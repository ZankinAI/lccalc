package com.cpp.lccalc.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
public class Task{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long taskId;

    private String taskIndex;

    private String name, description;

    public int getBudget() {
        return budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    private int budget;

    private int progress;

    private Long duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate factStartDate;

    private String state;

    @ManyToOne
    @JoinColumn(name="project_id", nullable=true)
    private Project project;

    @OneToMany(mappedBy = "task")
    private Set<CommercialOffer> commercialOffers;

    @OneToMany(mappedBy = "task")
    private Set<SubTask> subTasks;


    public Task() {

    }

    public Task(String name, String description) {

        this.name = name;
        this.description = description;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public Set<SubTask> getSubTasks() {
        return subTasks;
    }

    public void setSubTasks(Set<SubTask> subTasks) {
        this.subTasks = subTasks;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getFactStartDate() {
        return factStartDate;
    }

    public void setFactStartDate(LocalDate factStartDate) {
        this.factStartDate = factStartDate;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTaskIndex() {
        return taskIndex;
    }

    public void setTaskIndex(String taskIndex) {
        this.taskIndex = taskIndex;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<CommercialOffer> getCommercialOffers() {
        return commercialOffers;
    }

    public void setCommercialOffers(Set<CommercialOffer> commercialOffers) {
        this.commercialOffers = commercialOffers;
    }

    public static Comparator TaskIndexComparator = new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            String taskIndex1 = task1.getTaskIndex();
            if (taskIndex1==null) taskIndex1 = "";
            String taskIndex2 = task2.getTaskIndex();
            if (taskIndex2==null) taskIndex2 = "";
            return taskIndex1.compareTo(taskIndex2);
        }
    };

    public void sortSubTasks(){
        List<SubTask> subTasksList = new ArrayList<SubTask>(this.subTasks);
        Collections.sort(subTasksList, SubTask.SubTaskIndexComparator);

        this.subTasks = new LinkedHashSet<SubTask>(subTasksList);
    }



}
