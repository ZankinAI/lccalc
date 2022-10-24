package com.cpp.lccalc.controllers;

import com.cpp.lccalc.classes.Category;
import com.cpp.lccalc.classes.GanttData;
import com.cpp.lccalc.models.Project;
import com.cpp.lccalc.models.Task;
import com.cpp.lccalc.repo.ProjectRopository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Controller
public class JsonResponseController {


    @Autowired
    ProjectRopository projectRopository;

    @GetMapping(value = "/gantt_data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<GanttData> getGanttData(@PathVariable(value = "id") long id){

        Project project = projectRopository.findById(id).orElseThrow();
        project.sortTasks();
        List<GanttData> ganttDatas = new ArrayList<>();

        for (Task task: project.getTasks()) {
            ganttDatas.add(new GanttData(task));

        }
        /*GanttData ganttData = new GanttData("1", "Задача 1", LocalDate.of(2014, 8, 1), LocalDate.of(2014, 8, 31), "0.28");

        ganttData.addChild(new GanttData("1_2", "Подзадача 2", LocalDate.of(2014, 8, 14), LocalDate.of(2014, 8, 28), "0.54"));
        ganttDatas.add(ganttData);

        ganttData = new GanttData("2", "Задача 2", LocalDate.of(2014, 8, 1), LocalDate.of(2014, 8, 31),"1");
        ganttDatas.add(ganttData);*/


        return ganttDatas;
    }
}
