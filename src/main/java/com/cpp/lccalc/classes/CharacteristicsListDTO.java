package com.cpp.lccalc.classes;

import java.util.ArrayList;
import java.util.List;

public class CharacteristicsListDTO {

    public String[] analogNames;

    public double[] qualityIndicator;
    public double[] technicalLevel;
    public List<CharacteristicDTO> characteristics;

    public CharacteristicsListDTO() {
        this.analogNames = new String[]{"Проект"};

    }

    public CharacteristicsListDTO(List<CharacteristicDTO> characteristics) {
        this.characteristics = characteristics;
    }

    public void addCharacteristicDTO(CharacteristicDTO characteristicDTO ){
        if (this.characteristics == null) {
            this.characteristics = new ArrayList<>();
            this.characteristics.add(characteristicDTO);
        }
        else this.characteristics.add(characteristicDTO);
        this.analogNames = characteristicDTO.getAnalogNames();
        findTechnicalLevel();
    }

    public void findQualityIndicator(){
        this.qualityIndicator = new double[this.analogNames.length];
        double scale = Math.pow(10, 2);
        for (int i=0; i<this.qualityIndicator.length; i++){
            this.qualityIndicator[i] = 0.0;
            for (CharacteristicDTO characteristicDTO:this.getCharacteristics()) {
                this.qualityIndicator[i] += characteristicDTO.getWeight() * characteristicDTO.getGrades()[i];
                this.qualityIndicator[i] = Math.ceil(this.qualityIndicator[i] * scale) / scale;
            }
        }

    }
    public void findTechnicalLevel(){
        findQualityIndicator();
        double scale = Math.pow(10, 2);
        this.technicalLevel = new double[this.qualityIndicator.length];
        for (int i=0; i<this.technicalLevel.length; i++){
            this.technicalLevel[i] = this.qualityIndicator[0] / this.qualityIndicator[i];
            this.technicalLevel[i] = Math.ceil(this.technicalLevel[i] * scale) / scale;

        }
    }

    public List<CharacteristicDTO> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<CharacteristicDTO> characteristics) {
        this.characteristics = characteristics;
    }
}
