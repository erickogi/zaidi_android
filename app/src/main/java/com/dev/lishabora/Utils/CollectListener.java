package com.dev.lishabora.Utils;

import com.dev.lishabora.Models.Collection;

public interface CollectListener {
    void createCollection(Collection c);

    void updateCollection(Collection c);

    void error(String error);
}
