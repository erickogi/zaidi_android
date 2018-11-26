package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;

public interface CollectListener {
    void createCollection(Collection c, FamerModel famerModel, Double aDouble, Double milk);

    void updateCollection(Collection c, FamerModel famerModel, Double aDouble, Double milk);
    void error(String error);
}
