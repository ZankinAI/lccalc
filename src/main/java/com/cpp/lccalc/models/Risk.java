package com.cpp.lccalc.models;

import javax.persistence.*;
import java.util.*;

@Entity
public class Risk {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long riskId;

    private String name;

    private double likelihood;

    private String likelihoodTitle;

    private double consequence;

    private String consequenceTitle;

    private String color;

    public short colorNumber;

    private double expected;

    private Long selectedRiskSolutionID;

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

    public double getLikelihood() {
        return likelihood;
    }

    public void setLikelihood(double likelihood) {
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

    public double getExpected() {
        return expected;
    }

    public void setExpected(double expected) {
        this.expected = expected;
    }

    public Long getSelectedRiskSolutionID() {
        return selectedRiskSolutionID;
    }

    public void setSelectedRiskSolutionID(Long selectedRiskSolutionID) {
        this.selectedRiskSolutionID = selectedRiskSolutionID;
    }

    public Set<RiskSolution> getRiskSolutions() {
        return riskSolutions;
    }

    public void setRiskSolutions(Set<RiskSolution> riskSolutions) {
        this.riskSolutions = riskSolutions;
    }

    public short getColorNumber() {
        return colorNumber;
    }

    public void setColorNumber(short colorNumber) {
        this.colorNumber = colorNumber;
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

    public Risk(String name, double likelihood, double consequence) {
        this.name = name;
        this.likelihood = likelihood;
        this.consequence = consequence;
        this.expected = likelihood / 100 * consequence;
        this.likelihoodTitle = likelihoodToTitle(likelihood);
    }

    public void update(){
        expected = likelihood / 100 * consequence;
        likelihoodTitle = likelihoodToTitle(likelihood);
        consequenceTitle = consequenceToTitle(consequence);
        color = getColor(likelihood,consequence);
    }

    public String likelihoodToTitle(double likelihood) {
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

    public String getColor(double likelihood, double consequence){

        double riskCost = consequence / getProject().getBudget();

        if (likelihood < 20) {
            if (riskCost < 0.01)
                { this.colorNumber = 1; return "color:#00B050";}
            else if (riskCost < 0.05)
                { this.colorNumber = 1; return "color:#00B050";}
            else if (riskCost < 0.1)
                { this.colorNumber = 2; return "color:#92D050";}
            else if (riskCost < 0.2)
                { this.colorNumber = 3; return "color:yellow";}
            else
                { this.colorNumber = 3; return "color:yellow";}
        }
        else if (likelihood < 40) {
            if (riskCost < 0.01)
                { this.colorNumber = 1; return "color:#00B050";}
            else if (riskCost < 0.05)
                { this.colorNumber = 2; return "color:#92D050";}
            else if (riskCost < 0.1)
                { this.colorNumber = 2; return "color:#92D050";}
            else if (riskCost < 0.2)
                { this.colorNumber = 3; return "color:yellow";}
            else
                { this.colorNumber = 4; return "color:#FFC000";}
        }
        else if (likelihood < 60) {
            if (riskCost < 0.01)
                { this.colorNumber = 1; return "color:#00B050";}
            else if (riskCost < 0.05)
                { this.colorNumber = 2; return "color:#92D050";}
            else if (riskCost < 0.1)
                { this.colorNumber = 3; return "color:yellow";}
            else if (riskCost < 0.2)
                { this.colorNumber = 4; return "color:#FFC000";}
            else
                { this.colorNumber = 4; return "color:#FFC000";}
        }
        else if (likelihood < 80) {
            if (riskCost < 0.01)
            { this.colorNumber = 1; return "color:#00B050";}
            else if (riskCost < 0.05)
                { this.colorNumber = 2; return "color:#92D050";}
            else if (riskCost < 0.1)
                { this.colorNumber = 3; return "color:yellow";}
            else if (riskCost < 0.2)
                { this.colorNumber = 4; return "color:#FFC000";}
            else
                { this.colorNumber = 5; return "color:red";}

        }
        else if (likelihood <= 100) {

            if (riskCost < 0.01)
                { this.colorNumber = 2; return "color:#92D050";}
            else if (riskCost < 0.05)
                { this.colorNumber = 3; return "color:yellow";}
            else if (riskCost < 0.1)
                { this.colorNumber = 4; return "color:#FFC000";}
            else if (riskCost < 0.2)
                { this.colorNumber = 5; return "color:red";}
            else
                { this.colorNumber = 5; return "color:red";}

        }
        return null;
    }

    public static Comparator RiskComparator = new Comparator<Risk>(){
        @Override
        public int compare(Risk o1, Risk o2) {
            Short riskColor1 = o1.getColorNumber();
            Short riskColor2 = o2.getColorNumber();
            int c1 = riskColor2.compareTo(riskColor1);
            if (c1 != 0){
                return  c1;
            }
            else {
                Long riskID1 = o1.getRiskId();
                Long riskID2 = o2.getRiskId();
                return riskID1.compareTo(riskID2);
            }
        }
    };
}
