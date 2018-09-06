package com.dev.lishabora.Models;

public class MilkModel {
    private String unitQty;
    private UnitsModel unitsModel;
    private String valueLtrs;
    private String valueKsh;


    public String getUnitQty() {
        return unitQty;
    }

    public void setUnitQty(String unitQty) {
        this.unitQty = unitQty;
    }

    public UnitsModel getUnitsModel() {
        return unitsModel;
    }

    public void setUnitsModel(UnitsModel unitsModel) {
        this.unitsModel = unitsModel;
    }

    public String getValueLtrs() {
        if (unitsModel != null && unitsModel.getUnitcapacity() != null && unitsModel.getUnitprice() != null) {

            double unitCapacity = Double.valueOf(unitsModel.getUnitcapacity()) / 1000;
            double unitQtyCollected = Double.valueOf(unitQty);

            return String.valueOf(unitCapacity * unitQtyCollected);
        } else {
            return valueLtrs;
        }

    }

    public void setValueLtrs(String valueLtrs) {
        this.valueLtrs = valueLtrs;
    }

    public String getValueKsh() {

        if (unitsModel != null && unitsModel.getUnitcapacity() != null && unitsModel.getUnitprice() != null) {
            double unitCapacity = Double.valueOf(unitsModel.getUnitcapacity()) / 1000;
            double unitPricePer = Double.valueOf(unitsModel.getUnitprice());
            double unitQtyCollected = Double.valueOf(unitQty);

            return String.valueOf((unitCapacity * unitQtyCollected) * unitPricePer);
        } else {
            return valueKsh;
        }

    }

    public void setValueKsh(String valueKsh) {
        this.valueKsh = valueKsh;
    }
}
