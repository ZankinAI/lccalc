package com.cpp.lccalc.controllers;

import com.cpp.lccalc.classes.HumanResourcesDTO;
import com.cpp.lccalc.classes.HumanResourcesListDTO;
import com.cpp.lccalc.classes.MaterialResourcesDTO;
import com.cpp.lccalc.classes.MaterialResourcesListDTO;
import com.cpp.lccalc.models.*;
import com.cpp.lccalc.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ResourcesController {

    @Autowired
    private ProjectRopository projectRopository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private ProjectManagerRepository projectManagerRepository;

    @Autowired
    private PerformerRepository performerRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CommercialOfferRepository commercialOfferRepository;

    @Autowired SubTaskRepository subTaskRepository;

    @Autowired
    private HumanResourceRepository humanResourceRepository;

    @Autowired
    private HumanResourceSubTaskRepository humanResourceSubTaskRepository;

    @Autowired
    private MaterialResourcesRepository materialResourcesRepository;

    @Autowired
    private MaterialResourcesSubTaskRepository materialResourcesSubTaskRepository;

    //Открыть страницу с ресурсами
    @GetMapping(value = "/resources")
    public String resources(Model model){

        Iterable<MaterialResources> materialResources = materialResourcesRepository.findAll();
        model.addAttribute("materialResources", materialResources);
        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        model.addAttribute("humanResources", humanResources);
        return "resources";
    }


    @PostMapping(value = "/resources/add_human_resource")
    public String addHumanResource(@RequestParam String name, @RequestParam Long tariff,
                                   @RequestParam Long amount,
                                   Model model){

        HumanResources humanResource = new HumanResources(tariff, amount, name);
        humanResourceRepository.save(humanResource);
        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        model.addAttribute("humanResources", humanResources);

        return "resources";
    }

    @PostMapping(value = "/resources/add_material_resource")
    public String addMaterialResource(@RequestParam String name, @RequestParam double rate,
                                      @RequestParam Long amount,
                                      Model model){

        MaterialResources materialResource = new MaterialResources(amount, rate, name);
        materialResourcesRepository.save(materialResource);

        Iterable<MaterialResources> materialResources = materialResourcesRepository.findAll();
        model.addAttribute("materialResources", materialResources);

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        model.addAttribute("humanResources", humanResources);

        return "resources";
    }

    @PostMapping("/subtask/{id}/add_material_resources")
    public String addMaterialResourceInSubTask(@PathVariable(value = "id") long id, @ModelAttribute MaterialResourcesListDTO resourcesList, Model model){
        SubTask subTask = subTaskRepository.findById(id).orElseThrow();

        MaterialResourcesSubTask materialResourcesSubTask = null;
        for (MaterialResourcesDTO materialesourcesDTO: resourcesList.getMaterialResources()) {
            materialResourcesSubTask = null;
            if (materialesourcesDTO.isChecked()){
                materialResourcesSubTask = subTask.findMaterialResourceById(materialesourcesDTO.getId());
                if (materialResourcesSubTask == null){
                    if (materialesourcesDTO.getAmount()>0){
                        MaterialResourcesSubTask materialResourcesSubTask1 = new MaterialResourcesSubTask(materialesourcesDTO.getAmount(), materialResourcesRepository.findById(materialesourcesDTO.getId()).orElseThrow(), subTask);
                        materialResourcesSubTaskRepository.save(materialResourcesSubTask1);
                        subTask.addMaterialResource(materialResourcesSubTask1);
                    }

                }
                else {
                    MaterialResourcesSubTask materialResourcesSubTaskEdit = materialResourcesSubTaskRepository.findById(materialResourcesSubTask.getMaterialResourceSubTaskId()).orElseThrow();
                    materialResourcesSubTaskEdit.setAmount(materialesourcesDTO.getAmount());
                    if (materialesourcesDTO.getAmount().equals(0L)){
                        materialResourcesSubTaskRepository.deleteById(materialResourcesSubTaskEdit.getMaterialResourceSubTaskId());
                        subTask.deleteMaterialResource(materialResourcesSubTaskEdit.getMaterialResourceSubTaskId());
                    }
                    else materialResourcesSubTaskRepository.save(materialResourcesSubTaskEdit);
                }

            }

        }

        subTask = subTaskRepository.findById(id).orElseThrow();
        subTask.findDuration();
        subTask.findBudget();
        subTaskRepository.save(subTask);

        model.addAttribute("subtask", subTask);

        model.addAttribute("task", subTask.getTask());
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);

        model.addAttribute("resourcesList", resourcesList);

        MaterialResourcesListDTO materialResourcesList = new MaterialResourcesListDTO();

        Iterable<MaterialResources> materialResources = materialResourcesRepository.findAll();

        for(MaterialResources materialResource: materialResources){
            materialResourcesList.addResource(new MaterialResourcesDTO(materialResource));
        }

        model.addAttribute("materialResourcesList", materialResourcesList);

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        for (HumanResources humanResource: humanResources) {
            humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
        }
        model.addAttribute("humanResourcesList", humanResourcesList);

        return "subtask-edit";
    }

    //Обновление человеческих ресурсов в подзадаче
    @PostMapping("/subtask/{id}/add_human_resources")
    public String addHumanResourceInSubtask(@PathVariable(value = "id") long id, @ModelAttribute HumanResourcesListDTO resourcesList, Model model){
        SubTask subTask = subTaskRepository.findById(id).orElseThrow();

        HumanResourcesSubTask humanResourcesSubTask = null;
        for (HumanResourcesDTO humanresourcesDTO: resourcesList.getHumanResources()) {
            humanResourcesSubTask = null;
            if (humanresourcesDTO.isChecked()){
                humanResourcesSubTask = subTask.findResourceById(humanresourcesDTO.getId());
                if (humanResourcesSubTask == null){
                    if (humanresourcesDTO.getAmount()>0){
                        HumanResourcesSubTask humanResourcesSubTask1 = new HumanResourcesSubTask(humanresourcesDTO.getAmount(), humanResourceRepository.findById(humanresourcesDTO.getId()).orElseThrow(), subTask);
                        humanResourceSubTaskRepository.save(humanResourcesSubTask1);
                        subTask.addHumanResource(humanResourcesSubTask1);
                    }

                }
                else {
                    HumanResourcesSubTask humanResourcesSubTaskEdit = humanResourceSubTaskRepository.findById(humanResourcesSubTask.getHumanResourceSubTaskId()).orElseThrow();
                    humanResourcesSubTaskEdit.setAmount(humanresourcesDTO.getAmount());
                    if (humanresourcesDTO.getAmount().equals(0L)){
                        humanResourceSubTaskRepository.deleteById(humanResourcesSubTaskEdit.getHumanResourceSubTaskId());
                        subTask.deleteHumanResource(humanResourcesSubTaskEdit.getHumanResourceSubTaskId());
                    }
                    else humanResourceSubTaskRepository.save(humanResourcesSubTaskEdit);
                }

            }

        }

        subTask = subTaskRepository.findById(id).orElseThrow();
        subTask.findDuration();
        subTask.findBudget();
        subTaskRepository.save(subTask);

        model.addAttribute("subtask", subTask);

        model.addAttribute("task", subTask.getTask());
        Iterable<Performer> performers = performerRepository.findAll();
        model.addAttribute("performers", performers);

        model.addAttribute("resourcesList", resourcesList);

        MaterialResourcesListDTO materialResourcesList = new MaterialResourcesListDTO();

        Iterable<MaterialResources> materialResources = materialResourcesRepository.findAll();

        for(MaterialResources materialResource: materialResources){
            materialResourcesList.addResource(new MaterialResourcesDTO(materialResource));
        }

        model.addAttribute("materialResourcesList", materialResourcesList);

        Iterable<HumanResources> humanResources = humanResourceRepository.findAll();
        HumanResourcesListDTO humanResourcesList = new HumanResourcesListDTO();

        for (HumanResources humanResource: humanResources) {
            humanResourcesList.addResource(new HumanResourcesDTO(humanResource));
        }
        model.addAttribute("humanResourcesList", humanResourcesList);

        return "subtask-edit";
    }
}
