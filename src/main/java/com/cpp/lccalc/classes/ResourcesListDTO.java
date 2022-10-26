package com.cpp.lccalc.classes;

import java.util.ArrayList;
import java.util.List;

public class ResourcesListDTO {
    public List<ResourcesDTO> resources;

    public ResourcesListDTO(){

    }

    public ResourcesListDTO(List<ResourcesDTO> resources) {
        this.resources = resources;
    }

    public void addResource(ResourcesDTO resources) {
        if (this.resources == null) {
            this.resources = new ArrayList<>();
            this.resources.add(resources);
        }
        else this.resources.add(resources);
    }

    public List<ResourcesDTO> getResources() {
        return resources;
    }

    public void setResources(List<ResourcesDTO> resources) {
        this.resources = resources;
    }
}
