package com.cpp.lccalc.classes;

import com.cpp.lccalc.models.BreakEven;

public class FeasibilityCalculation {

    public String dataVolumeOfSales;

    public String dataImplementationCosts;

    public String dataBreakEvenPoint;

    public double breakEven;

    public double reasonableSalesVolume;

    public FeasibilityCalculation() {
    }

    public FeasibilityCalculation(BreakEven breakEven, double projectBudget) {
        double step = 0.0;
        this.breakEven = projectBudget / (breakEven.getPrice() - breakEven.getOther());
        this.reasonableSalesVolume = (projectBudget + breakEven.getExpectedProfit()) / (breakEven.getPrice() - breakEven.getOther());

        /*if (this.breakEven<100) step=1;
        if ((this.breakEven<1000)&&(this.breakEven>100)) step=50;
        if ((this.breakEven<10000)&&(this.breakEven>1000)) step=100;
        if ((this.breakEven<100000)&&(this.breakEven>10000)) step=500;
        if ((this.breakEven<1000000)&&(this.breakEven>100000)) step=1000;*/

        step=1;
        this.dataVolumeOfSales = "[";
        for (double i = 0; i<this.breakEven * 2; i+=step){
            this.dataVolumeOfSales += "[" +i+ ","+ i*breakEven.getPrice() + "],";
        }
        this.dataVolumeOfSales += "]";

        this.dataImplementationCosts = "[";
        for (double i = 0; i<this.breakEven * 2; i+=step){
            this.dataImplementationCosts += "[" +i+ ","+ (i * breakEven.getOther() + projectBudget) + "],";
        }
        this.dataImplementationCosts += "]";

        this.dataBreakEvenPoint = "[["+ (int)this.breakEven +","+ this.breakEven * breakEven.getPrice() +"]]";

    }

    public String getDataVolumeOfSales() {
        return dataVolumeOfSales;
    }

    public void setDataVolumeOfSales(String dataVolumeOfSales) {
        this.dataVolumeOfSales = dataVolumeOfSales;
    }

    public String getDataImplementationCosts() {
        return dataImplementationCosts;
    }

    public void setDataImplementationCosts(String dataImplementationCosts) {
        this.dataImplementationCosts = dataImplementationCosts;
    }

    public String getDataBreakEvenPoint() {
        return dataBreakEvenPoint;
    }

    public void setDataBreakEvenPoint(String dataBreakEvenPoint) {
        this.dataBreakEvenPoint = dataBreakEvenPoint;
    }

    public double getBreakEven() {
        return breakEven;
    }

    public void setBreakEven(double breakEven) {
        this.breakEven = breakEven;
    }

    public double getReasonableSalesVolume() {
        return reasonableSalesVolume;
    }

    public void setReasonableSalesVolume(double reasonableSalesVolume) {
        this.reasonableSalesVolume = reasonableSalesVolume;
    }
}
