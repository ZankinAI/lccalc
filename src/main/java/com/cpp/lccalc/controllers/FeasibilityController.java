package com.cpp.lccalc.controllers;

import com.cpp.lccalc.classes.CharacteristicDTO;
import com.cpp.lccalc.classes.CharacteristicsListDTO;
import com.cpp.lccalc.classes.FeasibilityCalculation;
import com.cpp.lccalc.models.BreakEven;
import com.cpp.lccalc.models.Characteristic;
import com.cpp.lccalc.models.Project;
import com.cpp.lccalc.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Set;

@Controller
public class FeasibilityController {

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

    @Autowired
    private BreakEvenRepository breakEvenRepository;

    @Autowired
    CharacteristicRepository characteristicRepository;


    //Открытие страницы оценки целесообразности
    @GetMapping("/feasibility_assessment")
    public String feasibilityAssessment(Model model) {

        //Project project = projectRopository.findById(id).orElseThrow();
        Iterable<Project> projects = projectRopository.findAll();
        //model.addAttribute("project", project);
        model.addAttribute("projects", projects);

        CharacteristicsListDTO characteristicsListDTO = new CharacteristicsListDTO();

        model.addAttribute("characteristicsList", characteristicsListDTO);

        return "feasibility-assessment";
    }
    //Выбор проекта на странице оценки целесообразности
    @PostMapping("/feasibility_assessment")
    public String feasibilityAssessmentId(@RequestParam Long projectId, Model model) {
        Project project = projectRopository.findById(projectId).orElseThrow();
        Iterable<Project> projects = projectRopository.findAll();
        BreakEven breakEven = null;
        if (!project.getBreakEvens().isEmpty()){
            breakEven = project.getFirstBreakEven();
        }

        FeasibilityCalculation feasibilityCalculation = null;

        if (breakEven!=null){
            feasibilityCalculation = new FeasibilityCalculation(breakEven, project.getBudget());
        }


        if (feasibilityCalculation!=null){
            model.addAttribute("dataVolumeOfSales", feasibilityCalculation.getDataVolumeOfSales());
            model.addAttribute("dataImplementationCosts", feasibilityCalculation.getDataImplementationCosts());
            model.addAttribute("dataBreakEvenPoint", feasibilityCalculation.getDataBreakEvenPoint());
        }

        model.addAttribute("feasibilityCalculation", feasibilityCalculation);

        model.addAttribute("breakEven", breakEven);
        model.addAttribute("project", project);
        model.addAttribute("projects", projects);

        Set<Characteristic> characteristics = project.getCharacteristics();
        CharacteristicsListDTO characteristicsListDTO = new CharacteristicsListDTO();

        for (Characteristic characteristic: characteristics) {
            characteristicsListDTO.addCharacteristicDTO(new CharacteristicDTO(characteristic));
        }

        model.addAttribute("characteristicsList", characteristicsListDTO);

        return "feasibility-assessment";
    }

    @PostMapping("/feasibility_assessment/change")
    public String feasibilityAssessmentChange(@RequestParam Long breakEvenId, Model model) {
        BreakEven breakEven = breakEvenRepository.findById(breakEvenId).orElseThrow();
        Project project = breakEven.getProject();
        Iterable<Project> projects = projectRopository.findAll();

        model.addAttribute("breakEven", breakEven);
        model.addAttribute("project", project);
        model.addAttribute("projects", projects);

        FeasibilityCalculation feasibilityCalculation = new FeasibilityCalculation(breakEven, project.getBudget());

        model.addAttribute("breakEven", breakEven);
        model.addAttribute("project", project);
        model.addAttribute("projects", projects);
        model.addAttribute("feasibilityCalculation", feasibilityCalculation);

        model.addAttribute("dataVolumeOfSales", feasibilityCalculation.getDataVolumeOfSales());
        model.addAttribute("dataImplementationCosts", feasibilityCalculation.getDataImplementationCosts());
        model.addAttribute("dataBreakEvenPoint", feasibilityCalculation.getDataBreakEvenPoint());

        return "feasibility-assessment";
    }

    @PostMapping("/feasibility_assessment/{breakEvenId}/edit")
    public String feasibilityAssessmentEdit(@PathVariable(value = "breakEvenId") long id,
                                           @RequestParam double price, @RequestParam double other,
                                           @RequestParam double expectedProfit, @RequestParam boolean idChecked, Model model) {

        BreakEven breakEven = breakEvenRepository.findById(id).orElseThrow();
        breakEven.setOther(other);
        breakEven.setPrice(price);
        breakEven.setExpectedProfit(expectedProfit);
        Project project = breakEven.getProject();
        if (idChecked) breakEvenRepository.save(breakEven);

        FeasibilityCalculation feasibilityCalculation = feasibilityCalculation = new FeasibilityCalculation(breakEven, project.getBudget());

        Iterable<Project> projects = projectRopository.findAll();

        model.addAttribute("breakEven", breakEven);
        model.addAttribute("project", project);
        model.addAttribute("projects", projects);

        model.addAttribute("feasibilityCalculation", feasibilityCalculation);
        model.addAttribute("dataVolumeOfSales", feasibilityCalculation.getDataVolumeOfSales());
        model.addAttribute("dataImplementationCosts", feasibilityCalculation.getDataImplementationCosts());
        model.addAttribute("dataBreakEvenPoint", feasibilityCalculation.getDataBreakEvenPoint());

        return "feasibility-assessment";
    }

    @PostMapping("/project/{id}/add_break_even")
    public String feasibilityAssessmentAdd(@PathVariable(value = "id") long id,
                                           @RequestParam double price, @RequestParam double other,
                                           @RequestParam double expectedProfit, Model model) {

        BreakEven breakEven = new BreakEven();
        breakEven.setOther(other);
        breakEven.setPrice(price);
        breakEven.setExpectedProfit(expectedProfit);
        Project project = projectRopository.findById(id).orElseThrow();
        breakEven.setProject(project);
        breakEvenRepository.save(breakEven);

        FeasibilityCalculation feasibilityCalculation = feasibilityCalculation = new FeasibilityCalculation(breakEven, project.getBudget());

        Iterable<Project> projects = projectRopository.findAll();

        model.addAttribute("breakEven", breakEven);
        model.addAttribute("project", project);
        model.addAttribute("projects", projects);

        model.addAttribute("feasibilityCalculation", feasibilityCalculation);
        model.addAttribute("dataVolumeOfSales", feasibilityCalculation.getDataVolumeOfSales());
        model.addAttribute("dataImplementationCosts", feasibilityCalculation.getDataImplementationCosts());
        model.addAttribute("dataBreakEvenPoint", feasibilityCalculation.getDataBreakEvenPoint());

        return "feasibility-assessment";
    }




}
