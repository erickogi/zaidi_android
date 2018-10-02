package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.Collection;
import com.dev.lishabora.Models.FamerModel;

public interface CollectListener {
    void createCollection(Collection c, FamerModel famerModel);

    void createCollection(Collection cAm, Collection cPm);

    void updateCollection(Collection c, FamerModel famerModel);

    void updateCollection(Collection cAm, Collection cPm);

    void error(String error);
}
