package com.cpp.lccalc.controllers;

import com.cpp.lccalc.classes.Category;
import com.cpp.lccalc.classes.GanttData;
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


    private List<GanttData> createChildren(){
        List<GanttData> childrens = new ArrayList<>();
        GanttData ganttData = new GanttData("1", "Подзадача 1", LocalDate.of(2014, 8, 12), LocalDate.of(2014, 8, 18));
        childrens.add(ganttData);
        return childrens;
    }

    @GetMapping(value = "/gantt_data/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<GanttData> getGanttData(@PathVariable(value = "id") long id){
        List<GanttData> ganttDatas = new ArrayList<>();
        GanttData ganttData = new GanttData("1", "Задача 1", LocalDate.of(2014, 8, 1), LocalDate.of(2014, 8, 31));
        ganttData.setChildren(createChildren());
        ganttDatas.add(ganttData);

        ganttData = new GanttData("2", "Задача 2", LocalDate.of(2014, 8, 1), LocalDate.of(2014, 8, 31));
        ganttDatas.add(ganttData);


        return ganttDatas;
    }
}
