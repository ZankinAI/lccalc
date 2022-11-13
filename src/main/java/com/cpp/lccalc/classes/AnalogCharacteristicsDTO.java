package com.cpp.lccalc.classes;

public class AnalogCharacteristicsDTO {
    public Long characteristicId;
    public String characteristicName;
    public int analogGrade;

    public AnalogCharacteristicsDTO() {
    }

    public AnalogCharacteristicsDTO(Long characteristicId, String characteristicName, int analogGrade) {
        this.characteristicId = characteristicId;
        this.characteristicName = characteristicName;
        this.analogGrade = analogGrade;
    }

    public Long getCharacteristicId() {
        return characteristicId;
    }

    public void setCharacteristicId(Long characteristicId) {
        this.characteristicId = characteristicId;
    }

    public String getCharacteristicName() {
        return characteristicName;
    }

    public void setCharacteristicName(String characteristicName) {
        this.characteristicName = characteristicName;
    }

    public int getAnalogGrade() {
        return analogGrade;
    }

    public void setAnalogGrade(int analogGrade) {
        this.analogGrade = analogGrade;
    }
}
