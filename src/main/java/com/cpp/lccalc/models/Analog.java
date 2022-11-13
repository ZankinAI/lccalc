package com.cpp.lccalc.models;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Analog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long analogId;

    private String name;

    @OneToMany(mappedBy = "analog", cascade = CascadeType.ALL )
    private Set<AnalogCharacteristic> analogCharacteristics;

    public Analog() {
    }

    public Analog(String name) {
        this.name = name;
    }

    public Long getAnalogId() {
        return analogId;
    }

    public void setAnalogId(Long analogId) {
        this.analogId = analogId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<AnalogCharacteristic> getAnalogCharacteristics() {
        return analogCharacteristics;
    }

    public void setAnalogCharacteristics(Set<AnalogCharacteristic> analogCharacteristics) {
        this.analogCharacteristics = analogCharacteristics;
    }
}
