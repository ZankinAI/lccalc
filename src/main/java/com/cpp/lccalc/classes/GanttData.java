package com.cpp.lccalc.classes;

import com.cpp.lccalc.models.Task;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GanttData {
    private String id;
    private String name;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate actualStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate actualEnd;

    private String progressValue;

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
        this.actualEnd = task.getStartDate();
        this.children = new ArrayList<>();
        this.progressValue = "1";

    }

    public void addChild(GanttData ganttData){
        if (this.children.isEmpty()) {
            this.children = new ArrayList<>();
            this.children.add(ganttData);
        }
        else this.children.add(ganttData);
    }

}
