package com.cpp.lccalc.models;

import javax.persistence.*;

@Entity
public class HumanResourcesSubTask {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long humanResourceSubTaskId;

    private Long amount;

    @ManyToOne
    @JoinColumn(name="human_resource_id", nullable=true)
    private HumanResources humanResource;

    @ManyToOne
    @JoinColumn(name="sub_task_id", nullable=true)
    private SubTask subTask;

    public HumanResourcesSubTask() {
    }

    public HumanResourcesSubTask(Long amount, HumanResources humanResource, SubTask subTask) {
        this.amount = amount;
        this.humanResource = humanResource;
        this.subTask = subTask;
    }

    public Long getHumanResourceSubTaskId() {
        return humanResourceSubTaskId;
    }

    public void setHumanResourceSubTaskId(Long humanResourceSubTaskId) {
        this.humanResourceSubTaskId = humanResourceSubTaskId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public HumanResources getHumanResource() {
        return humanResource;
    }

    public void setHumanResource(HumanResources humanResource) {
        this.humanResource = humanResource;
    }

    public SubTask getSubTask() {
        return subTask;
    }

    public void setSubTask(SubTask subTask) {
        this.subTask = subTask;
    }
}
