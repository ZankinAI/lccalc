package com.cpp.lccalc.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class MaterialResourcesSubTask {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long materialResourceSubTaskId;

    private Long amount;

    @ManyToOne
    @JoinColumn(name="material_resource_id", nullable=true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private MaterialResources materialResource;

    @ManyToOne
    @JoinColumn(name="sub_task_id", nullable=true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SubTask subTask;

    public MaterialResourcesSubTask() {
    }

    public MaterialResourcesSubTask(Long amount, MaterialResources materialResource, SubTask subTask) {
        this.amount = amount;
        this.materialResource = materialResource;
        this.subTask = subTask;
    }

    public Long getMaterialResourceSubTaskId() {
        return materialResourceSubTaskId;
    }

    public void setMaterialResourceSubTaskId(Long materialResourceSubTaskId) {
        this.materialResourceSubTaskId = materialResourceSubTaskId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public MaterialResources getMaterialResource() {
        return materialResource;
    }

    public void setMaterialResource(MaterialResources materialResource) {
        this.materialResource = materialResource;
    }

    public SubTask getSubTask() {
        return subTask;
    }

    public void setSubTask(SubTask subTask) {
        this.subTask = subTask;
    }
}
