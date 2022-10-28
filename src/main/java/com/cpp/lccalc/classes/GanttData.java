package com.cpp.lccalc.classes;

import com.cpp.lccalc.models.SubTask;
import com.cpp.lccalc.models.Task;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class GanttData {
    private String id;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate actualStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate actualEnd;

    private String progressValue;

    private String connectTo;

    private List<GanttData> children;

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

    public String getConnectTo() {
        return connectTo;
    }

    public void setConnectTo(String connectTo) {
        this.connectTo = connectTo;
    }

    public LocalDate getActualStart() {
        return actualStart;
    }



    public void setActualStart(LocalDate actualStart) {
        this.actualStart = actualStart;
    }

    public LocalDate getActualEnd() {
        return actualEnd;
    }

    public void setActualEnd(LocalDate actualEnd) {
        this.actualEnd = actualEnd;
    }

    public List<GanttData> getChildren() {
        return children;
    }

    public void setChildren(List<GanttData> children) {
        this.children = children;
    }

    public String getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(String progressValue) {
        this.progressValue = progressValue;
    }

    public GanttData(String id, String name, LocalDate actualStart, LocalDate actualEnd, String progress) {
        this.id = id;
        this.name = name;
        this.actualStart = actualStart;
        this.actualEnd = actualEnd;
        this.children = new ArrayList<>();
        this.progressValue = progress;
    }

    public GanttData(Task task){
        this.id = task.getTaskIndex();
        this.name = task.getName();
        this.actualStart = task.getStartDate();

        if (task.getDuration()==null) this.actualEnd = task.getStartDate();
        else this.actualEnd = task.getStartDate().plusDays(task.getDuration());
        this.children = new ArrayList<>();
        this.progressValue = "1";

        task.sortSubTasks();

        this.children = new ArrayList<>();

        List<SubTask> subTasks = new ArrayList<>(task.getSubTasks());

        for (SubTask subtask: subTasks) {
            if (subtask.getSubTaskIndex().matches(task.getTaskIndex()+"(\\.\\d){1}"))
            {
                this.addChild(new GanttData(subtask,subTasks));
            }
        }



        for (GanttData ganttData: this.children){
            SubTask subTaskFindPrev = subTasks.stream().filter(prevSubTask->(prevSubTask.getPreviousIndex().equals(ganttData.getId()))).findFirst().orElse(null);
            if (subTaskFindPrev!=null)
                ganttData.setConnectTo(subTaskFindPrev.getSubTaskIndex());

        }



    }

    public GanttData(SubTask subTask, List<SubTask> subTasks){
        this.id = subTask.getSubTaskIndex();
        this.name = subTask.getName();
        this.actualStart = subTask.getStartDate();
        if (subTask.getDuration()==null) this.actualEnd = subTask.getStartDate();
        else this.actualEnd = subTask.getStartDate().plusDays(subTask.getDuration());
        this.children = new ArrayList<>();
        this.progressValue = subTask.getProgress();

        for (SubTask subTaskFromList: subTasks) {
           if (subTaskFromList.getSubTaskIndex().matches(subTask.getSubTaskIndex()+"(\\.\\d){1}"))
           {
               //subTasks.remove(subTaskFromList);
               this.addChild(new GanttData(subTaskFromList,subTasks));
           }

        }

        for (GanttData ganttData: this.children){
            SubTask subTaskFindPrev = subTasks.stream().filter(prevSubTask->(prevSubTask.getPreviousIndex().equals(ganttData.getId()))).findFirst().orElse(null);
            if (subTaskFindPrev!=null)
                ganttData.setConnectTo(subTaskFindPrev.getSubTaskIndex());
        }
    }

    public void addChild(GanttData ganttData){
        if (this.children.isEmpty()) {
            this.children = new ArrayList<>();
            this.children.add(ganttData);
        }
        else this.children.add(ganttData);
    }

}
