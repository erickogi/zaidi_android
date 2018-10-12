package com.dev.lishabora.Views;

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;

import java.util.List;

public class CollectMilkConstants {

    static FamerModel famerModel;
    static List<Collection> collectionss;


    public CollectMilkConstants(FamerModel famerModel, List<Collection> collectionss) {
        CollectMilkConstants.famerModel = famerModel;
        CollectMilkConstants.collectionss = collectionss;
    }

    public CollectMilkConstants() {
    }

    public static FamerModel getFamerModel() {
        return famerModel;
    }

    public static void setFamerModel(FamerModel famerModel) {
        CollectMilkConstants.famerModel = famerModel;
    }

    public static List<Collection> getCollectionss() {
        return collectionss;
    }

    public static void setCollectionss(List<Collection> collectionss) {
        CollectMilkConstants.collectionss = collectionss;
    }
}
