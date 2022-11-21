package com.cpp.lccalc.models;

import com.cpp.lccalc.classes.Utils;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.lang.Nullable;

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
    private String performerName;

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }


    private double budget;

    private double progress;

    private Long duration;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate factStartDate;

    private String state;

    @ManyToOne
    @JoinColumn(name="project_id", nullable=true)
    private Project project;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Set<CommercialOffer> commercialOffers;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private Set<SubTask> subTasks;


    public Task() {

    }

    public Task(String name, String description) {

        this.name = name;
        this.description = description;
    }

    public String getPerformerName() {
        return performerName;
    }

    public void setPerformerName(String performerName) {
        this.performerName = performerName;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
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

    public double findBudgetBySubTasks(){
        double budget = 0;

        if (this.subTasks!=null){
            for (SubTask subtask:
                 this.subTasks) {
                 budget += subtask.getBudget();
            }
        }
        return budget;

    }

    public String findPrevIndexOfSubtask(String subtaskIndex){

        for (SubTask subtask: this.subTasks) {
            if (subtask.getSubTaskIndex().equals(subtaskIndex))
                return subtask.getPreviousIndex();
        }
        return null;

    }

    public String findNextSubTask(String subTaskIndex){
        for (SubTask subtask: this.subTasks) {
            if (subtask.getPreviousIndex().equals(subTaskIndex))
                return subtask.getSubTaskIndex();
        }
        return null;

    }

    public SubTask getSubTaskByIndex(String subTaskIndex){
        for (SubTask subtask: this.subTasks) {
            if (subtask.getSubTaskIndex().equals(subTaskIndex))
                return subtask;
        }
        return null;
    }

    public String findLastIndexOfSubTasks(){

        if (this.subTasks==null) return this.getTaskIndex()+".0";
        if (this.subTasks.isEmpty()) return this.getTaskIndex()+".0";
        String lastIndex = null;
        this.sortSubTasks();
        final Iterator<SubTask> itr = this.subTasks.iterator();
        SubTask lastTask = null;
        while(itr.hasNext()){
            lastTask = itr.next();
        }
        assert lastTask != null;


        return lastTask.getSubTaskIndex();
    }





}
