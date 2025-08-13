package de.tomreno.assessment.fullstack.backend.util;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StringSanitizerTest {

    @Nested
    class SanitizeAlphabeticString {

        @Test
        void shouldRemoveSpecialCharacters() {
            String alphabeticString = "A&l@p#h$a%b^e!t*i(c)_ s3t+r[i]n{g}\\|/<>☀?";

            String actual = StringSanitizer.sanitizeAlphabeticString(alphabeticString);

            assertThat(actual).isEqualTo("Alphabetic string");
        }

        @Test
        void shouldRemoveUnwantedDashes() {
            String alphabeticString = "- Alphabetic-string- -";

            String actual = StringSanitizer.sanitizeAlphabeticString(alphabeticString);

            assertThat(actual).isEqualTo("Alphabetic-string");
        }

    }

    @Nested
    class SanitizeNumericString {

        @Test
        void shouldRemoveSpecialCharacters() {
            String numericString = "1&2@3#4$5%6^7!8*9(0)_+[]{}\\|/<>☀?";

            String actual = StringSanitizer.sanitizeNumericString(numericString);

            assertThat(actual).isEqualTo("1234567890");
        }

    }

}
