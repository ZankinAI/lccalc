package com.cpp.lccalc.models;

import javax.persistence.*;

@Entity
public class Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long riskId;

    private String name;

    private int likelihood;

    private String likelihoodTitle;

    private int consequence;

    private String consequenceTitle;

    private String color;

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

    public int getConsequence() {
        return consequence;
    }

    public void setConsequence(int consequence) {
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

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Risk(){

    }

    public Risk(String name, int likelihood, int consequence) {
        this.name = name;
        this.likelihood = likelihood;
        this.consequence = consequence;
        this.color = getColor(likelihood,consequence);
        this.likelihoodTitle = likelihoodToTitle(likelihood);
        this.consequenceTitle = consequenceToTitle(consequence);
    }

    public String likelihoodToTitle(int likelihood) {
        if (likelihood < 20) return "Крайне маловероятно";
        else if (likelihood < 40) return "Маловероятно";
        else if (likelihood < 60) return "Возможно";
        else if (likelihood < 80) return "Вероятно";
        else if (likelihood <= 100) return "Крайне вероятно";
        return null;
    }

    public String consequenceToTitle(int consequence){
        switch (consequence){
            case (1):
                return "Пренебрежимое";
            case (2):
                return "Небольшое";
            case (3):
                return "Умеренное";
            case (4):
                return "Значительное";
            case (5):
                return "Существенное";
        }
        return null;
    }

    public String getColor(int likelihood, int consequence){

        if (likelihood < 20) {

            switch (consequence) {
                case (1):
                    return "background-color:#00B050";
                case (2):
                    return "background-color:#00B050";
                case (3):
                    return "background-color:#92D050";
                case (4):
                    return "background-color:yellow";
                case (5):
                    return "background-color:yellow";
            }
        }
        else if (likelihood < 40) {

            switch (consequence) {
                case (1):
                    return "background-color:#00B050";
                case (2):
                    return "background-color:#92D050";
                case (3):
                    return "background-color:#92D050";
                case (4):
                    return "background-color:yellow";
                case (5):
                    return "background-color:#FFC000";
            }
        }
        else if (likelihood < 60) {

            switch (consequence) {
                case (1):
                    return "background-color:#00B050";
                case (2):
                    return "background-color:#92D050";
                case (3):
                    return "background-color:yellow";
                case (4):
                    return "background-color:#FFC000";
                case (5):
                    return "background-color:#FFC000";
            }
        }
        else if (likelihood < 80) {

            switch (consequence) {
                case (1):
                    return "background-color:#00B050";
                case (2):
                    return "background-color:#92D050";
                case (3):
                    return "background-color:yellow";
                case (4):
                    return "background-color:#FFC000";
                case (5):
                    return "background-color:red";
            }

        }
        else if (likelihood <= 100) {

            switch (consequence) {
                case (1):
                    return "background-color:#92D050";
                case (2):
                    return "background-color:yellow";
                case (3):
                    return "background-color:#FFC000";
                case (4):
                    return "background-color:red";
                case (5):
                    return "background-color:red";
            }
        }
        return null;
    }
}
