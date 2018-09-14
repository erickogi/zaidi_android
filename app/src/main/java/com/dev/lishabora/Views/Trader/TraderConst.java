package com.dev.lishabora.Views.Trader;

import com.dev.lishabora.Models.Trader.TraderModel;

public class TraderConst {
    public static int INTENT_TYPE_NEW = 12;
    public static int INTENT_TYPE_EDIT = 13;
    private static TraderModel traderModel;
    private static int type_selected;

    public static TraderModel getTraderModel() {
        return traderModel;
    }

    public static void setTraderModel(TraderModel traderModel) {
        TraderConst.traderModel = traderModel;
    }

    public static int getIntentTypeNew() {
        return INTENT_TYPE_NEW;
    }

    public static void setIntentTypeNew(int intentTypeNew) {
        INTENT_TYPE_NEW = intentTypeNew;
    }

    public static int getIntentTypeEdit() {
        return INTENT_TYPE_EDIT;
    }

    public static void setIntentTypeEdit(int intentTypeEdit) {
        INTENT_TYPE_EDIT = intentTypeEdit;
    }

    public static int getType_selected() {
        return type_selected;
    }

    public static void setType_selected(int type_selecteds) {
        type_selected = type_selecteds;
    }
}
