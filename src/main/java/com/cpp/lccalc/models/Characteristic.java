package com.cpp.lccalc.models;

import javax.persistence.*;

@Entity
public class Characteristic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long characteristicId;

    String name;

    double weight;

    int grade;

    @ManyToOne
    @JoinColumn(name="project_id", nullable=true)
    private Project project;

    public Characteristic() {
    }

    public Characteristic(String name, double weight, int grade, Project project) {
        this.name = name;
        this.weight = weight;
        this.grade = grade;
        this.project = project;
    }

    public Long getCharacteristicId() {
        return characteristicId;
    }

    public void setCharacteristicId(Long characteristicId) {
        this.characteristicId = characteristicId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
