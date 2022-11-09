package com.cpp.lccalc.models;

import javax.persistence.*;

@Entity
public class RiskSolution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long riskSolutionId;

    private String name;

    private double probability;

    private Long cost;

    private double reduction;

    private double expected;

    @ManyToOne
    @JoinColumn(name="risk_id", nullable=true)
    private Risk risk;

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

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }

    public double getReduction() {
        return reduction;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    public double getExpected() {
        return expected;
    }

    public void setExpected(double expected) {
        this.expected = expected;
    }

    public Risk getRisk() {
        return risk;
    }

    public void setRisk(Risk risk) {
        this.risk = risk;
        double consequence = this.risk.getConsequence();
        this.expected = this.probability / 100 * (this.cost + consequence * (100 - this.reduction) / 100) + (100 - this.probability) / 100 * (consequence + this.cost);
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
