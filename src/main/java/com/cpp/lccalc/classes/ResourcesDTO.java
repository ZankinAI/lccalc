package com.cpp.lccalc.classes;

public class ResourcesDTO {
    private boolean checked;
    private Long amount;
    private String name;

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

    public ResourcesDTO() {
    }

    public ResourcesDTO(boolean checked, Long amount, String name) {
        this.amount = amount;
        this.checked = checked;
        this.name = name;
    }
}
