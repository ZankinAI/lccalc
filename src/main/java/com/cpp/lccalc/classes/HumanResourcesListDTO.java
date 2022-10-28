package com.cpp.lccalc.classes;

import java.util.ArrayList;
import java.util.List;

public class HumanResourcesListDTO {
    public List<HumanResourcesDTO> humanResources;

    public HumanResourcesListDTO(){

    }

    public HumanResourcesListDTO(List<HumanResourcesDTO> resources) {
        this.humanResources = resources;
    }

    public void addResource(HumanResourcesDTO resources) {
        if (this.humanResources == null) {
            this.humanResources = new ArrayList<>();
            this.humanResources.add(resources);
        }
        else this.humanResources.add(resources);
    }

    public List<HumanResourcesDTO> getHumanResources() {
        return humanResources;
    }

    public void setHumanResources(List<HumanResourcesDTO> humanResources) {
        this.humanResources = humanResources;
    }
}
