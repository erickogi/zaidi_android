package com.dev.lishabora.Models.Reports;

public class ReportLineChartModel {

    private String xvalue;
    private String yvalue;

    public ReportLineChartModel(String xvalue, String yvalue) {
        this.xvalue = xvalue;
        this.yvalue = yvalue;
    }

    public String getXvalue() {
        return xvalue;
    }

    public void setXvalue(String xvalue) {
        this.xvalue = xvalue;
    }

    public String getYvalue() {
        return yvalue;
    }

    public void setYvalue(String yvalue) {
        this.yvalue = yvalue;
    }
}
