package com.cpp.lccalc.models;

import javax.persistence.*;
import java.util.Set;
@Entity
public class Performer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long performerId;

    private String name;

    private String description;

    @OneToMany(mappedBy = "performer")
    private Set<CommercialOffer> CommercialOffers;


    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<CommercialOffer> getCommercialOffers() {
        return CommercialOffers;
    }

    public void setCommercialOffers(Set<CommercialOffer> commercialOffers) {
        CommercialOffers = commercialOffers;
    }

    public Performer() {
    }

    public Performer(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
