package com.qtivate.server.model;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TokensBySubjectName {
    private String name;

    private List<String> tokens;

    public TokensBySubjectName(String name, List<String> tokens) {
        this.name = name;
        this.tokens = List.copyOf(tokens);
    }

    public String getName() {
        return this.name;
    }

    public List<String> getTokens() {
        return List.copyOf(this.tokens);
    }

    @Override
    public String toString() {
        AtomicReference<String> ret = new AtomicReference<>("{\n" +
                this.name + ",");
        this.tokens.forEach(token -> {
            ret.set(ret.get() + token);
        });

        return ret.get() + "\n}";
    }
}
