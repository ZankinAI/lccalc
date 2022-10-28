package com.cpp.lccalc.classes;

import java.util.ArrayList;
import java.util.List;

public class MaterialResourcesListDTO {
    public List<MaterialResourcesDTO> materialResources;

    public MaterialResourcesListDTO(){

    }

    public MaterialResourcesListDTO(List<MaterialResourcesDTO> resources) {
        this.materialResources = resources;
    }

    public void addResource(MaterialResourcesDTO resources) {
        if (this.materialResources == null) {
            this.materialResources = new ArrayList<>();
            this.materialResources.add(resources);
        }
        else this.materialResources.add(resources);
    }

    public List<MaterialResourcesDTO> getMaterialResources() {
        return materialResources;
    }

    public void setMaterialResources(List<MaterialResourcesDTO> materialResources) {
        this.materialResources = materialResources;
    }
}
