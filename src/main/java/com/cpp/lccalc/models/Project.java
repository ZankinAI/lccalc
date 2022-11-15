package com.cpp.lccalc.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.*;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;
    private String projectName, description;
    private double  budget;

    private double progress, earnedBudget, percentEarnedBudget;
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

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL )
    private Set<Task> tasks;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL )
    private Set<Risk> risks;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL )
    private Set<BreakEven> breakEvens;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL )
    private Set<Characteristic> characteristics;


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

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    public double getEarnedBudget() {
        return earnedBudget;
    }

    public void setEarnedBudget(double earnedBudget) {
        this.earnedBudget = earnedBudget;
    }

    public double getPercentEarnedBudget() {
        return percentEarnedBudget;
    }

    public void setPercentEarnedBudget(double percentEarnedBudget) {
        this.percentEarnedBudget = percentEarnedBudget;
    }

    public Set<BreakEven> getBreakEvens() {
        return breakEvens;
    }

    public void setBreakEvens(Set<BreakEven> breakEvens) {
        this.breakEvens = breakEvens;
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

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
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

    public Set<Risk> getRisks() {
        return risks;
    }

    public Set<Characteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(Set<Characteristic> characteristics) {
        this.characteristics = characteristics;
    }

    public void setRisks(Set<Risk> risks) {
        this.risks = risks;
    }

    public void sortTasks(){
        List<Task> tasksList = new ArrayList<Task>(this.tasks);
        Collections.sort(tasksList, Task.TaskIndexComparator);
        this.tasks = new LinkedHashSet<Task>(tasksList);
    }

    public void findBudget(){
        this.budget = 0;
        this.earnedBudget = 0;
        this.progress = 0;
        this.percentEarnedBudget = 0;

        double duration = 0;
        double durationComplete = 0;

        if (this.tasks!=null){
            for (Task task:
                 this.tasks) {
                this.budget += task.getBudget();
                this.earnedBudget += task.getBudget() * (task.getProgress() / 100.0);
                if (task.getDuration()!=null){
                    duration+=task.getDuration();
                    durationComplete += task.getDuration() * task.getProgress() / 100.0;
                }

            }
        }
        else return;
        double scale = Math.pow(10, 2);

        this.progress = durationComplete / duration * 100;
        this.progress = Math.ceil(this.progress * scale) / scale;
        this.percentEarnedBudget = this.earnedBudget / this.budget * 100;


    }

    public BreakEven getFirstBreakEven(){
        BreakEven breakEven = null;
        breakEven = this.breakEvens.iterator().next();

        return breakEven;
    }


    public void sortRisks(){
        List<Risk> riskList = new ArrayList<Risk>(this.risks);
        Collections.sort(riskList, Risk.RiskComparator);
        this.risks = new LinkedHashSet<Risk>(riskList);
    }





}
