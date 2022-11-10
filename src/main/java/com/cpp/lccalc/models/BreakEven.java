package com.cpp.lccalc.models;

import javax.persistence.*;

@Entity
public class BreakEven {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long breakEvenId;

    //Отчисления
    private double contribution;

    //Накладные расходы
    private double overheads;
    //Себестоимость изделия
    private double other;

    //ожидаемая прибыль
    private double expectedProfit;

    //Цена продукта
    private double price;

    @ManyToOne
    @JoinColumn(name="project_id", nullable=true)
    private Project project;

    public Long getBreakEvenId() {
        return breakEvenId;
    }

    public void setBreakEvenId(Long breakEvenId) {
        this.breakEvenId = breakEvenId;
    }

    public double getContribution() {
        return contribution;
    }

    public void setContribution(double contribution) {
        this.contribution = contribution;
    }

    public double getOverheads() {
        return overheads;
    }

    public void setOverheads(double overheads) {
        this.overheads = overheads;
    }

    public double getOther() {
        return other;
    }

    public void setOther(double other) {
        this.other = other;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public double getExpectedProfit() {
        return expectedProfit;
    }

    public void setExpectedProfit(double expectedProfit) {
        this.expectedProfit = expectedProfit;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public BreakEven() {
    }

    public BreakEven(double contribution, double overheads, double other, Project project) {
        this.contribution = contribution;
        this.overheads = overheads;
        this.other = other;
        this.project = project;
    }


}
