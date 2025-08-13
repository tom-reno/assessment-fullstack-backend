package de.tomreno.assessment.fullstack.backend.entity;

import java.util.stream.Stream;

public enum Color {

    BLUE(1,  "blau"),
    GREEN(2, "grün"),
    VIOLET(3, "violett"),
    RED(4, "rot"),
    YELLOW(5, "gelb"),
    TURQUOISE(6, "türkis"),
    WHITE(7, "weiß");

    private final int id;
    private final String name;

    Color(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Color fromId(int id) {
        return Stream.of(values()).filter(c -> c.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Color id " + id + " does not exist"));
    }

    public static Color fromName(String name) {
        return Stream.of(values()).filter(c -> c.getName().equals(name)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Color name " + name + " does not exist"));
    }

}
