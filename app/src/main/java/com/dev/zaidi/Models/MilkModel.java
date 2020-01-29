package com.dev.zaidi.Models;

import java.io.Serializable;

public class MilkModel implements Serializable {
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
            double unitQtyCollected = 0;
            if (unitQty != null) {
                try {
                    unitQtyCollected = Double.valueOf(unitQty);
                } catch (Exception nm) {
                    nm.printStackTrace();
                }
            }

            return String.valueOf(unitCapacity * unitQtyCollected);
        } else {
            if (valueLtrs != null) {
                return valueLtrs;
            } else {
                return "0";
            }

        }

    }

    public void setValueLtrs(String valueLtrs) {
        this.valueLtrs = valueLtrs;
    }

    public String getValueKsh() {

        if (unitsModel != null && unitsModel.getUnitcapacity() != null && unitsModel.getUnitprice() != null) {
            double unitCapacity = Double.valueOf(unitsModel.getUnitcapacity());/// 1000;
            double unitPricePer = Double.valueOf(unitsModel.getUnitprice());
            double unitQtyCollected = 0;
            if (unitQty != null) {
                try {
                    unitQtyCollected = Double.valueOf(unitQty);
                } catch (Exception nm) {
                    nm.printStackTrace();
                }
            }

            return String.valueOf((unitQtyCollected) * unitPricePer);
        } else {
            return valueKsh;
        }

    }

    public void setValueKsh(String valueKsh) {
        this.valueKsh = valueKsh;
    }
}
