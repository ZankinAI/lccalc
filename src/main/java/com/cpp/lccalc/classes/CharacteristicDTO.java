package com.cpp.lccalc.classes;

import com.cpp.lccalc.models.AnalogCharacteristic;
import com.cpp.lccalc.models.Characteristic;

import java.lang.reflect.Array;

public class CharacteristicDTO {
    Long id;
    String name;
    double weight;
    String[] analogNames;
    int[] grades;

    Long[] analogId;

    public CharacteristicDTO(Characteristic characteristic){
        this.id = characteristic.getCharacteristicId();
        this.name = characteristic.getName();
        this.weight = characteristic.getWeight();
        if (characteristic.getAnalogCharacteristics()==null){
            this.analogNames = new String[1];
            this.analogId = new Long[1];
            this.grades = new int[1];
        }
        else{
            this.analogNames = new String[characteristic.getAnalogCharacteristics().size() + 1];
            this.analogId = new Long[characteristic.getAnalogCharacteristics().size() + 1];
            this.grades = new int[characteristic.getAnalogCharacteristics().size() + 1];
            characteristic.sortAnalogCharacteristic();
            int i = 1;
            for (AnalogCharacteristic analogCharacteristic:characteristic.getAnalogCharacteristics()) {
                this.analogNames[i] = analogCharacteristic.getAnalog().getName();
                this.analogId[i] = analogCharacteristic.getAnalog().getAnalogId();
                this.grades[i] = analogCharacteristic.getGrade();
                i++;
            }
        }

        this.analogNames[0] = "Проект";



        this.analogId[0] = characteristic.getProject().getProjectId();
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

    public Long[] getAnalogId() {
        return analogId;
    }

    public void setAnalogId(Long[] analogId) {
        this.analogId = analogId;
    }

    public int getIndexOfAnalog(Long analogId){
        int k = 0;
        for(int i = 1; i<this.analogId.length; i++){
            if (this.analogId[i].equals(analogId)) return i;
        }
        return k;
    }
}
