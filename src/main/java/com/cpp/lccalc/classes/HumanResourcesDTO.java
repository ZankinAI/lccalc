package com.cpp.lccalc.classes;

public class HumanResourcesDTO {
    private boolean checked;

    private Long tariff;
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
        this.checked = checked;
        this.tariff = tariff;
        this.name = name;
    }
}
