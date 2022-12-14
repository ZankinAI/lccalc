package com.cpp.lccalc.classes;

import com.cpp.lccalc.models.SubTask;
import com.cpp.lccalc.models.Task;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TaskCalculation {
    private String id;
    private String name;

    private String progressValue;

    private Long duration;

    private Long earlyStart;

    private Long lateStart;

    private String prevIndex;

    private LocalDate startDate;


    private List<TaskCalculation> children;

    public String getPrevIndex() {
        return prevIndex;
    }

    public void setPrevIndex(String prevIndex) {
        this.prevIndex = prevIndex;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(String progressValue) {
        this.progressValue = progressValue;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getEarlyStart() {
        return earlyStart;
    }

    public void setEarlyStart(Long earlyStart) {
        this.earlyStart = earlyStart;
    }

    public Long getLateStart() {
        return lateStart;
    }

    public void setLateStart(Long lateStart) {
        this.lateStart = lateStart;
    }

    public List<TaskCalculation> getChildren() {
        return children;
    }

    public void setChildren(List<TaskCalculation> children) {
        this.children = children;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public TaskCalculation(Task task) {
        this.id = task.getTaskIndex();
        this.name = task.getName();
        this.duration = task.getDuration();
        task.sortSubTasks();
        this.children = new ArrayList<>();
        List<SubTask> subTasks = new ArrayList<>(task.getSubTasks());

        this.startDate = task.getStartDate();

        for (SubTask subtask : subTasks) {
            if (subtask.getSubTaskIndex().matches(task.getTaskIndex() + "(\\.\\d){1}")) {
                this.addChild(new TaskCalculation(subtask));
            }
        }
        for (int i = 0; i < this.children.size(); i++) {
            this.children.get(i).setEarlyStart(this.children.get(i).findEarlyStart(this.children));
        }
        Long duration = 0L;

        for (int i = 0; i < this.children.size(); i++) {
            if ((this.children.get(i).getDuration() + this.children.get(i).getEarlyStart()) > duration) {
                duration = this.children.get(i).getDuration() + this.children.get(i).getEarlyStart();
            }
        }

        this.duration = duration;

        if (this.getStartDate() != null) {
            for (int i = 0; i < this.children.size(); i++) {
                this.children.get(i).setStartDate(this.children.get(i).findStartDate(this.children, this.startDate));
            }
        }

        double sumDuration = 0;
        double sumProgressDuration = 0;

        for (int i = 0; i < this.children.size(); i++) {
            sumDuration += this.children.get(i).getDuration();
            sumProgressDuration += this.children.get(i).getDuration() * Double.valueOf(this.children.get(i).getProgressValue())/100.0;
        }



        this.progressValue = String.valueOf((int)(sumProgressDuration / sumDuration * 100.0));



        //this.findDuration(this.children);
    }

    public TaskCalculation findChildById(String id) {

        for (TaskCalculation taskCalculationChild : this.children) {
            if (taskCalculationChild.getId().equals(id)) return taskCalculationChild;
        }
        return null;
    }


    public TaskCalculation(SubTask subTask) {
        this.id = subTask.getSubTaskIndex();
        this.name = subTask.getName();
        this.progressValue = subTask.getProgress();
        this.prevIndex = subTask.getPreviousIndex();
        this.duration = subTask.getDuration();


    }

    public void findDuration(List<TaskCalculation> taskOneRang) {

        for (TaskCalculation taskCalculation : taskOneRang) {
            for (TaskCalculation prevTaskCalc : taskOneRang) {
                if (taskCalculation.getPrevIndex().equals(prevTaskCalc.getId())) {
                    if (prevTaskCalc.getEarlyStart() != null)
                        this.setEarlyStart(prevTaskCalc.getEarlyStart() + prevTaskCalc.getDuration());
                    else this.setEarlyStart(prevTaskCalc.getDuration() + prevTaskCalc.findEarlyStart(taskOneRang));
                }

            }

        }

    }

    public Long findEarlyStart(List<TaskCalculation> taskOneRang) {
        Long earlyStart = 0L;

        for (TaskCalculation taskCalculation : taskOneRang) {
            if ((taskCalculation.getId().equals(this.prevIndex)))
                if (taskCalculation.getEarlyStart() != null)
                    earlyStart = taskCalculation.getEarlyStart() + taskCalculation.getDuration();
                else {
                    earlyStart = taskCalculation.findEarlyStart(taskOneRang) + taskCalculation.getDuration();
                    return earlyStart;
                }
        }
        return earlyStart;
    }

    public LocalDate findStartDate(List<TaskCalculation> tasksOneRang, LocalDate taskStartDate) {
        LocalDate startDate = taskStartDate;

        for (TaskCalculation taskCalculation : tasksOneRang) {
            if ((taskCalculation.getId().equals(this.prevIndex)))
                if (taskCalculation.getStartDate() != null)
                    startDate = taskCalculation.getStartDate().plusDays(taskCalculation.getDuration());
                else {
                    startDate = taskCalculation.findStartDate(tasksOneRang, taskStartDate).plusDays(taskCalculation.getDuration());
                    return startDate;
                }
        }

        return startDate;
    }


    public void addChild(TaskCalculation taskCalculation) {
        if (this.children.isEmpty()) {
            this.children = new ArrayList<>();
            this.children.add(taskCalculation);
        } else this.children.add(taskCalculation);
    }
}
