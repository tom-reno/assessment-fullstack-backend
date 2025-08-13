package de.tomreno.assessment.fullstack.backend.util;

import org.apache.commons.lang3.StringUtils;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class StringSanitizer {

    public static String sanitizeAlphabeticString(String string) {
        string = string.replaceAll("[^A-Za-zÄÖÜäöüß\\s-]", "");
        if (!string.contains("-")) {
            return StringUtils.strip(string);
        }
        return Stream.of(string.split("-"))
                .map(StringUtils::strip)
                .filter(StringUtils::isAlpha)
                .collect(Collectors.joining("-"));
    }

    public static String sanitizeNumericString(String string) {
        string = string.replaceAll("[^0-9]", "");
        return StringUtils.strip(string);
    }

    private StringSanitizer() {
    }

}
