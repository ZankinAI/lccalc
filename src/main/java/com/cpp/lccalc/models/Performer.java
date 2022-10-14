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

    private String industry;
    private String telephone;
    private String site;
    private String address;
    private String director;

        @OneToMany(mappedBy = "performer")
    private Set<CommercialOffer> CommercialOffers;


    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

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

    public Performer(String name, String description, String industry, String telephone, String site, String address, String director) {
        this.name = name;
        this.description = description;
        this.industry = industry;
        this.telephone = telephone;
        this.site = site;
        this.address = address;
        this.director = director;
    }

    public Performer(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
