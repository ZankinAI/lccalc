package com.cpp.lccalc.classes;

import java.util.ArrayList;
import java.util.List;

public class CharacteristicsListDTO {
    public List<CharacteristicDTO> characteristics;

    public CharacteristicsListDTO() {
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
    }

    public List<CharacteristicDTO> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(List<CharacteristicDTO> characteristics) {
        this.characteristics = characteristics;
    }
}
