package com.cpp.lccalc.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long subTaskId;

    private String subTaskIndex;

    private String name;

    private String progress;

    private Long duration;

    private Long laboriousness;

    private String previousIndex;

    private double budget;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ManyToOne
    @JoinColumn(name="task_id", nullable=true)
    private Task task;

    @OneToMany(mappedBy = "subTask", cascade = CascadeType.ALL)

    private Set<HumanResourcesSubTask> humanResourcesSubTasks;

    @OneToMany(mappedBy = "subTask", cascade = CascadeType.ALL)
    private Set<MaterialResourcesSubTask> materialResourcesSubTasks;

    public SubTask() {

    }

    public SubTask(String subTaskIndex, String name, String progress, Long laboriousness, String previousIndex, LocalDate startDate, Task task/*, Long laboriousness*/) {
        this.subTaskIndex = subTaskIndex;
        this.name = name;
        this.progress = progress;
        this.duration = duration;
        this.previousIndex = previousIndex;
        this.startDate = startDate;
        this.laboriousness = laboriousness;
        this.task = task;
    }


    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }

    public Set<MaterialResourcesSubTask> getMaterialResourcesSubTasks() {
        return materialResourcesSubTasks;
    }

    public void setMaterialResourcesSubTasks(Set<MaterialResourcesSubTask> materialResourcesSubTasks) {
        this.materialResourcesSubTasks = materialResourcesSubTasks;
    }

    public Long getSubTaskId() {
        return subTaskId;
    }

    public Set<HumanResourcesSubTask> getHumanResourcesSubTasks() {
        return humanResourcesSubTasks;
    }

    public void setHumanResourcesSubTasks(Set<HumanResourcesSubTask> humanResourcesSubTasks) {
        this.humanResourcesSubTasks = humanResourcesSubTasks;
    }

    public void setSubTaskId(Long subTaskId) {
        this.subTaskId = subTaskId;
    }

    public String getSubTaskIndex() {
        return subTaskIndex;
    }

    public void setSubTaskIndex(String subTaskIndex) {
        this.subTaskIndex = subTaskIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getPreviousIndex() {
        return previousIndex;
    }

    public void setPreviousIndex(String previousIndex) {
        this.previousIndex = previousIndex;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Long getLaboriousness() {
        return laboriousness;
    }

    public void setLaboriousness(Long laboriousness) {
        this.laboriousness = laboriousness;
    }

    public static Comparator SubTaskIndexComparator = new Comparator<SubTask>() {
        @Override
        public int compare(SubTask subTask1, SubTask subTask2) {
            String subTaskIndex1 = subTask1.getSubTaskIndex();
            String subTaskIndex2 = subTask2.getSubTaskIndex();
            return subTaskIndex1.compareTo(subTaskIndex2);
        }
    };

    public HumanResourcesSubTask findResourceById(Long id){


        for (HumanResourcesSubTask humanResourcesSubTask1: this.humanResourcesSubTasks) {
            if (humanResourcesSubTask1.getHumanResource().getHumanResourceId().equals(id)) return humanResourcesSubTask1;
        }

        return null;
    }

    public MaterialResourcesSubTask findMaterialResourceById(Long id){


        for (MaterialResourcesSubTask materialResourcesSubTask1: this.materialResourcesSubTasks) {
            if (materialResourcesSubTask1.getMaterialResource().getMaterialResourceId().equals(id)) return materialResourcesSubTask1;
        }

        return null;
    }

    public void findDuration(){

        int amountSum = 0;

        if (this.laboriousness == null) {
            this.laboriousness = 0L;
            this.duration = 0L;
            return;

        }

        if (this.humanResourcesSubTasks==null) {this.duration = this.laboriousness / 8L; return;}
        for (HumanResourcesSubTask humanResourcesSubTask: this.humanResourcesSubTasks) {
            amountSum+=humanResourcesSubTask.getAmount();
        }

        if (amountSum!=0){
            this.duration = this.laboriousness / amountSum / 8L;
        }
        else this.duration = this.laboriousness / 8L;

    }

    public void addHumanResource(HumanResourcesSubTask humanResourcesSubTask){
        if (this.humanResourcesSubTasks==null)
            this.humanResourcesSubTasks = new HashSet<>();
        this.humanResourcesSubTasks.add(humanResourcesSubTask);
    }
    public void deleteHumanResource(Long humanResourceSubTaskId){
        for (HumanResourcesSubTask humanResourcesSubTask:this.humanResourcesSubTasks) {
            if (humanResourcesSubTask.getHumanResourceSubTaskId().equals(humanResourceSubTaskId)){
                this.humanResourcesSubTasks.remove(humanResourcesSubTask);
            }
        }
    }

    public void addMaterialResource(MaterialResourcesSubTask materialResourcesSubTask){
        if (this.materialResourcesSubTasks==null)
            this.materialResourcesSubTasks = new HashSet<>();
        this.materialResourcesSubTasks.add(materialResourcesSubTask);
    }
    public void deleteMaterialResource(Long materialResourceSubTaskId){
        for (MaterialResourcesSubTask materialResourcesSubTask:this.materialResourcesSubTasks) {
            if (materialResourcesSubTask.getMaterialResourceSubTaskId().equals(materialResourceSubTaskId)){
                this.materialResourcesSubTasks.remove(materialResourcesSubTask);
            }
        }
    }

    public void findBudget(){
        this.budget = 0;
        if (this.humanResourcesSubTasks!=null){
            for (HumanResourcesSubTask humanResourcesSubTask: this.humanResourcesSubTasks ) {
                this.budget += this.duration * 8 * humanResourcesSubTask.getHumanResource().getTariff() * humanResourcesSubTask.getAmount();
            }
        }

        if (this.materialResourcesSubTasks != null){
            for (MaterialResourcesSubTask materialResourcesSubTask: this.materialResourcesSubTasks) {
                this.budget += this.laboriousness * materialResourcesSubTask.getAmount()*materialResourcesSubTask.getMaterialResource().getRate();
            }
        }


    }

}
