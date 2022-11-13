package com.cpp.lccalc.models;

import javax.persistence.*;
import java.util.Comparator;

@Entity
public class AnalogCharacteristic {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private int grade;

    @ManyToOne
    @JoinColumn(name="analog_id", nullable=true)
    private Analog analog;

    @ManyToOne
    @JoinColumn(name="characteristic_id", nullable=true)
    private Characteristic characteristic;

    public AnalogCharacteristic() {
    }

    public AnalogCharacteristic(int grade, Analog analog, Characteristic characteristic) {
        this.grade = grade;
        this.analog = analog;
        this.characteristic = characteristic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public Analog getAnalog() {
        return analog;
    }

    public void setAnalog(Analog analog) {
        this.analog = analog;
    }

    public Characteristic getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(Characteristic characteristic) {
        this.characteristic = characteristic;
    }

    public static Comparator CharacteristicAnalogIdComparator = new Comparator<AnalogCharacteristic>() {
        @Override
        public int compare(AnalogCharacteristic characteristic1, AnalogCharacteristic characteristic2) {
            Long characteristicId1 = characteristic1.getId();
            Long characteristicId2 = characteristic2.getId();
            return characteristicId1.compareTo(characteristicId2);
        }
    };
}
