package com.cpp.lccalc.classes;

import com.cpp.lccalc.models.HumanResources;

public class HumanResourcesDTO {
    private boolean checked;

    private Long tariff;
    private Long amount;
    private String name;

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public Long getTariff() {
        return tariff;
    }

    public void setTariff(Long tariff) {
        this.tariff = tariff;
    }

    public HumanResourcesDTO() {
    }

    public HumanResourcesDTO(boolean checked, Long tariff, Long amount, String name) {
        this.amount = amount;
        this.checked = false;
        this.tariff = tariff;
        this.name = name;
    }

    public HumanResourcesDTO(HumanResources humanResource) {
        this.amount = humanResource.getAmount();
        this.checked = false;
        this.tariff = humanResource.getTariff();
        this.name = humanResource.getName();
        this.id = humanResource.getHumanResourceId();
    }
}
