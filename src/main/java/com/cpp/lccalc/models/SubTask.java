package com.cpp.lccalc.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Comparator;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @ManyToOne
    @JoinColumn(name="task_id", nullable=true)
    private Task task;

    @OneToMany(mappedBy = "subTask", cascade = CascadeType.ALL, orphanRemoval = true )
    private Set<HumanResourcesSubTask> humanResourcesSubTasks;

    public SubTask() {
    }

    public SubTask(String subTaskIndex, String name, String progress, Long duration, String previousIndex, LocalDate startDate, Task task/*, Long laboriousness*/) {
        this.subTaskIndex = subTaskIndex;
        this.name = name;
        this.progress = progress;
        this.duration = duration;
        this.previousIndex = previousIndex;
        this.startDate = startDate;
        //this.laboriousness = laboriousness;
        this.task = task;
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

}
