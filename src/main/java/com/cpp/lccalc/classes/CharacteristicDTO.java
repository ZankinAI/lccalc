package com.cpp.lccalc.classes;

import com.cpp.lccalc.models.Characteristic;

import java.lang.reflect.Array;

public class CharacteristicDTO {
    Long id;
    String name;
    double weight;
    String[] analogNames;
    int[] grades;

    public CharacteristicDTO(Characteristic characteristic){
        this.id = characteristic.getCharacteristicId();
        this.name = characteristic.getName();
        this.weight = characteristic.getWeight();
        this.analogNames = new String[2];
        this.analogNames[0] = "Проект";
        this.analogNames[1] = "Аналог";
        this.grades = new int[2];
        this.grades[0] = characteristic.getGrade();

    }

    public CharacteristicDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String[] getAnalogNames() {
        return analogNames;
    }

    public void setAnalogNames(String[] analogNames) {
        this.analogNames = analogNames;
    }

    public int[] getGrades() {
        return grades;
    }

    public void setGrades(int[] grades) {
        this.grades = grades;
    }
}
