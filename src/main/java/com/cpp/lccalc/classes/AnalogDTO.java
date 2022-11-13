package com.cpp.lccalc.classes;

import java.util.*;

public class AnalogDTO {
    public Long id;
    public String name;

    public List<AnalogCharacteristicsDTO> analogCharacteristicsDTOList;

    public AnalogDTO() {
        this.analogCharacteristicsDTOList = new ArrayList<>();
    }

    public AnalogDTO(Long id, String name) {
        this.id = id;
        this.name = name;
        this.analogCharacteristicsDTOList = new ArrayList<>();
    }

    public void addAnalogCharacteristic(Long id, String name){
        this.analogCharacteristicsDTOList.add(new AnalogCharacteristicsDTO(id,name, 0));
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

    public List<AnalogCharacteristicsDTO> getAnalogCharacteristicsDTOList() {
        return analogCharacteristicsDTOList;
    }

    public void setAnalogCharacteristicsDTOList(List<AnalogCharacteristicsDTO> analogCharacteristicsDTOList) {
        this.analogCharacteristicsDTOList = analogCharacteristicsDTOList;
    }
}
