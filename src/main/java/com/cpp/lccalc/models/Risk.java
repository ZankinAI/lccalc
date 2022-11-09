package com.cpp.lccalc.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long riskId;

    private String name;

    private int likelihood;

    private String likelihoodTitle;

    private double consequence;

    private String consequenceTitle;

    private String color;

    @OneToMany(mappedBy = "risk", cascade = CascadeType.ALL )
    private Set<RiskSolution> riskSolutions;

    @ManyToOne
    @JoinColumn(name="project_id", nullable=true)
    private Project project;

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

    public int getLikelihood() {
        return likelihood;
    }

    public void setLikelihood(int likelihood) {
        this.likelihood = likelihood;
    }

    public double getConsequence() {
        return consequence;
    }

    public void setConsequence(double consequence) {
        this.consequence = consequence;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLikelihoodTitle() {
        return likelihoodTitle;
    }

    public void setLikelihoodTitle(String likelihoodTitle) {
        this.likelihoodTitle = likelihoodTitle;
    }

    public String getConsequenceTitle() {
        return consequenceTitle;
    }

    public void setConsequenceTitle(String consequenceTitle) {
        this.consequenceTitle = consequenceTitle;
    }

    public Set<RiskSolution> getRiskSolutions() {
        return riskSolutions;
    }

    public void setRiskSolutions(Set<RiskSolution> riskSolutions) {
        this.riskSolutions = riskSolutions;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
        this.consequenceTitle = consequenceToTitle(consequence);
        this.color = getColor(likelihood,consequence);
    }

    public Risk(){

    }

    public Risk(String name, int likelihood, int consequence) {
        this.name = name;
        this.likelihood = likelihood;
        this.consequence = consequence;
        this.likelihoodTitle = likelihoodToTitle(likelihood);
    }

    public String likelihoodToTitle(int likelihood) {
        if (likelihood < 20) return "Крайне маловероятно";
        else if (likelihood < 40) return "Маловероятно";
        else if (likelihood < 60) return "Возможно";
        else if (likelihood < 80) return "Вероятно";
        else if (likelihood <= 100) return "Крайне вероятно";
        return null;
    }

    public String consequenceToTitle(double consequence){
        double riskCost = consequence / getProject().getBudget();
        if (riskCost < 0.01)
            return "Пренебрежимое";
        else if (riskCost < 0.05)
            return "Небольшое";
        else if (riskCost < 0.1)
            return "Умеренное";
        else if (riskCost < 0.2)
            return "Значительное";
        else
            return "Существенное";
    }

    public String getColor(int likelihood, double consequence){

        double riskCost = consequence / getProject().getBudget();

        if (likelihood < 20) {
            if (riskCost < 0.01)
                return "background-color:#00B050";
            else if (riskCost < 0.05)
                return "background-color:#00B050";
            else if (riskCost < 0.1)
                return "background-color:#92D050";
            else if (riskCost < 0.2)
                return "background-color:yellow";
            else
                return "background-color:yellow";
        }
        else if (likelihood < 40) {
            if (riskCost < 0.01)
                return "background-color:#00B050";
            else if (riskCost < 0.05)
                return "background-color:#92D050";
            else if (riskCost < 0.1)
                return "background-color:#92D050";
            else if (riskCost < 0.2)
                return "background-color:yellow";
            else
                return "background-color:#FFC000";
        }
        else if (likelihood < 60) {
            if (riskCost < 0.01)
                return "background-color:#00B050";
            else if (riskCost < 0.05)
                return "background-color:#92D050";
            else if (riskCost < 0.1)
                return "background-color:yellow";
            else if (riskCost < 0.2)
                return "background-color:#FFC000";
            else
                return "background-color:#FFC000";
        }
        else if (likelihood < 80) {
            if (riskCost < 0.01)
                return "background-color:#00B050";
            else if (riskCost < 0.05)
                return "background-color:#92D050";
            else if (riskCost < 0.1)
                return "background-color:yellow";
            else if (riskCost < 0.2)
                return "background-color:#FFC000";
            else
                return "background-color:red";

        }
        else if (likelihood <= 100) {

            if (riskCost < 0.01)
                return "background-color:#92D050";
            else if (riskCost < 0.05)
                return "background-color:yellow";
            else if (riskCost < 0.1)
                return "background-color:#FFC000";
            else if (riskCost < 0.2)
                return "background-color:red";
            else
                return "background-color:red";

        }
        return null;
    }
}
