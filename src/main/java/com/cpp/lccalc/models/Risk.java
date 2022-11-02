package com.cpp.lccalc.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long riskId;

    private String name;

    private String likelihood;

    private String consequence;

    public Long getRiskId() {
        return riskId;
    }

    public void setRiskId(Long riskId) {
        this.riskId = riskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLikelihood() {
        return likelihood;
    }

    public void setLikelihood(String likelihood) {
        this.likelihood = likelihood;
    }

    public String getConsequence() {
        return consequence;
    }

    public void setConsequence(String consequence) {
        this.consequence = consequence;
    }

    public Risk(String name, String likelihood, String consequence) {
        this.name = name;
        this.likelihood = likelihood;
        this.consequence = consequence;
    }
}
