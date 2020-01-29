package com.dev.zaidi.Utils;

import com.dev.zaidi.Models.Collection;
import com.dev.zaidi.Models.FamerModel;

public interface CollectListener {
    void createCollection(Collection c, FamerModel famerModel, Double aDoubleCash, Double milkLitres);

    void updateCollection(Collection c, FamerModel famerModel, Double aDoubleCash, Double milkLitres);

    void error(String error);
}
