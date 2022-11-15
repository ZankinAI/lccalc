package com.cpp.lccalc.models;

import javax.persistence.*;
import java.util.*;

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

    @OneToMany(mappedBy = "characteristic", cascade = CascadeType.ALL )
    private Set<AnalogCharacteristic> analogCharacteristics;


    public Characteristic() {
    }

    public Characteristic(String name, double weight, int grade, Project project) {
        this.name = name;
        this.weight = weight;
        this.grade = grade;
        this.project = project;
    }

    public Set<AnalogCharacteristic> getAnalogCharacteristics() {
        return analogCharacteristics;
    }

    public void setAnalogCharacteristics(Set<AnalogCharacteristic> analogCharacteristics) {
        this.analogCharacteristics = analogCharacteristics;
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

    public static Comparator CharacteristicIdComparator = new Comparator<Characteristic>() {
        @Override
        public int compare(Characteristic characteristic1, Characteristic characteristic2) {
            Long characteristicId1 = characteristic1.getCharacteristicId();
            Long characteristicId2 = characteristic2.getCharacteristicId();
            return characteristicId1.compareTo(characteristicId2);
        }
    };

    public void sortAnalogCharacteristic(){
        if (this.analogCharacteristics!=null){
            List<AnalogCharacteristic> acList = new ArrayList<AnalogCharacteristic>(this.analogCharacteristics);
            Collections.sort(acList, AnalogCharacteristic.CharacteristicAnalogIdComparator);
            this.analogCharacteristics = new LinkedHashSet<AnalogCharacteristic>(acList);
        }

    }

}
