package com.cpp.lccalc.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class MaterialResources {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long materialResourceId;

    private Long amount;

    private double rate;

    private String name;

    @OneToMany(mappedBy = "materialResource", cascade = CascadeType.ALL)
    private Set<MaterialResourcesSubTask> materialResourcesSubTasks;

    public Long getMaterialResourceId() {
        return materialResourceId;
    }

    public void setMaterialResourceId(Long materialResourceId) {
        this.materialResourceId = materialResourceId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(Long rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MaterialResourcesSubTask> getMaterialResourcesSubTasks() {
        return materialResourcesSubTasks;
    }

    public void setMaterialResourcesSubTasks(Set<MaterialResourcesSubTask> materialResourcesSubTasks) {
        this.materialResourcesSubTasks = materialResourcesSubTasks;
    }

    public MaterialResources() {
    }

    public MaterialResources(Long amount, double rate, String name) {
        this.amount = amount;
        this.rate = rate;
        this.name = name;
    }
}
