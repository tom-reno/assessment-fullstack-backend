package de.tomreno.assessment.fullstack.backend.domain;

import java.util.stream.Stream;

public enum ColorDto {

    BLUE(1, "blau"),
    GREEN(2, "grün"),
    VIOLET(3, "violett"),
    RED(4, "rot"),
    YELLOW(5, "gelb"),
    TURQUOISE(6, "türkis"),
    WHITE(7, "weiß");

    private final int id;
    private final String name;

    ColorDto(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static ColorDto fromId(int id) {
        return Stream.of(values()).filter(c -> c.getId() == id).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Color id " + id + " does not exist"));
    }

    public static ColorDto fromName(String name) {
        return Stream.of(values()).filter(c -> c.getName().equals(name)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Color name " + name + " does not exist"));
    }

}
