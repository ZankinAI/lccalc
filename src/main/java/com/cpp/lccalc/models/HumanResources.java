package com.cpp.lccalc.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class HumanResources {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long humanResourceId;

    private Long tariff;
    private Long amount;
    private String name;

    @OneToMany(mappedBy = "humanResource", cascade = CascadeType.ALL)
    private Set<HumanResourcesSubTask> humanResourcesSubTasks;



    public Set<HumanResourcesSubTask> getHumanResourcesSubTasks() {
        return humanResourcesSubTasks;
    }

    public void setHumanResourcesSubTasks(Set<HumanResourcesSubTask> humanResourcesSubTasks) {
        this.humanResourcesSubTasks = humanResourcesSubTasks;
    }

    public Long getHumanResourceId() {
        return humanResourceId;
    }

    public void setHumanResourceId(Long humanResourceId) {
        this.humanResourceId = humanResourceId;
    }

    public Long getTariff() {
        return tariff;
    }

    public void setTariff(Long tariff) {
        this.tariff = tariff;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HumanResources() {
    }

    public HumanResources(Long tariff, Long amount, String name) {
        this.tariff = tariff;
        this.amount = amount;
        this.name = name;
    }
}
