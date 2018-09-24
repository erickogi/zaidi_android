package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.Collection;

public interface CollectListener {
    void createCollection(Collection c);

    void createCollection(Collection cAm, Collection cPm);

    void updateCollection(Collection c);

    void updateCollection(Collection cAm, Collection cPm);

    void error(String error);
}
