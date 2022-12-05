package com.qtivate.server.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class ManualPresenceResult {
    private List<String> added;

    private List<String> removed;

    public List<String> getAdded() {
        return List.copyOf(this.added);
    }
    public List<String> getRemoved() {
        return List.copyOf(this.removed);
    }

    @Override
    public String toString() {
        AtomicReference<String> ret = new AtomicReference<>("");
        this.added.forEach(aid -> {
            ret.updateAndGet(v -> v + added + ",");
        });
        this.removed.forEach(removed -> {
            ret.updateAndGet(v -> v + removed + ",");
        });

        return ret.get();
    }
}
