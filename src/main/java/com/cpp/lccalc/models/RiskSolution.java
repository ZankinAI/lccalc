package com.cpp.lccalc.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class RiskSolution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long riskSolutionId;

    private String name;

    private int probability;

    private Long cost;

    private int reduction;

    public Long getRiskSolutionId() {
        return riskSolutionId;
    }

    public void setRiskSolutionId(Long riskSolutionId) {
        this.riskSolutionId = riskSolutionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProbability() {
        return probability;
    }

    public void setProbability(int probability) {
        this.probability = probability;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public int getReduction() {
        return reduction;
    }

    public void setReduction(int reduction) {
        this.reduction = reduction;
    }

    public RiskSolution(){

    }

    public RiskSolution(String name, int probability, Long cost, int reduction){
        this.name = name;
        this.probability = probability;
        this.cost = cost;
        this.reduction = reduction;
    }
}
