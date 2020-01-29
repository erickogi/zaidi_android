package com.dev.zaidi.Utils;

import com.dev.zaidi.Models.Collection;

public interface CollectionCreateUpdateListener {
    void createCollection(Collection c);

    void updateCollection(Collection c);

    void error(String error);
}
