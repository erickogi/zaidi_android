package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;

public interface CollectListener {
    void createCollection(Collection c, FamerModel famerModel, Double aDoubleCash, Double milkLitres);

    void updateCollection(Collection c, FamerModel famerModel, Double aDoubleCash, Double milkLitres);

    void error(String error);
}
