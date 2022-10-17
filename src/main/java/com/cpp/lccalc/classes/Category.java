package com.cpp.lccalc.classes;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class Category {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private LocalDate start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private LocalDate end;

    private String lable;

    public Category() {
    }

    public Category(LocalDate start, LocalDate end, String lable) {
        this.start = start;
        this.end = end;
        this.lable = lable;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public String getLable() {
        return lable;
    }

    public void setLable(String lable) {
        this.lable = lable;
    }
}
