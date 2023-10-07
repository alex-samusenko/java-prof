package ru.otus.entity;

import java.util.HashMap;
import java.util.Map;

public class BanknoteStack {
    private Map<Banknote, Long> content;

    public BanknoteStack() {
        content = new HashMap<>();
    }

    public Map<Banknote, Long> getContent() {
        return content;
    }

    public void setContent(Map<Banknote, Long> content) {
        this.content = content;
    }
}
