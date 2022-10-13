package com.cpp.lccalc.models;

import com.cpp.lccalc.repo.TaskRepository;

import javax.persistence.*;
@Entity
public class CommercialOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long coId;

    private Long duration;

    private Long budget;

    private String status;

    @ManyToOne
    @JoinColumn(name="task_id", nullable=true)
    private Task task;


    @ManyToOne
    @JoinColumn(name="performer_id", nullable=true)
    private Performer performer;

    public Long getCoId() {
        return coId;
    }

    public void setCoId(Long coId) {
        this.coId = coId;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Performer getPerformer() {
        return performer;
    }

    public void setPerformer(Performer performer) {
        this.performer = performer;
    }
}
