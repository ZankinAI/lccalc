package com.cpp.lccalc.classes;

public class MaterialResourcesDTO {
    private boolean checked;
    private Long amount;
    private String name;

    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MaterialResourcesDTO() {
    }

    public MaterialResourcesDTO(boolean checked, Long amount, String name) {
        this.amount = amount;
        this.checked = checked;
        this.name = name;
    }

    /*public MaterialResourcesDTO(MaterialResources materialResources) {
        this.amount = materialResources.getAmount;
        this.checked = false;
        this.name = materialResources.getName();;
        this.id = humanResource.getMaterialResourceId();
    }*/
}
