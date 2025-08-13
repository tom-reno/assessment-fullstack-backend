package de.tomreno.assessment.fullstack.backend.parser;

import de.tomreno.assessment.fullstack.backend.config.AppCsvPersonProps;
import de.tomreno.assessment.fullstack.backend.entity.Color;
import de.tomreno.assessment.fullstack.backend.entity.Person;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static de.tomreno.assessment.fullstack.backend.util.StringSanitizer.sanitizeAlphabeticString;
import static de.tomreno.assessment.fullstack.backend.util.StringSanitizer.sanitizeNumericString;

@Component
public class PersonCsvParser {

    private static final Logger LOG = LoggerFactory.getLogger(PersonCsvParser.class);

    private static final String CSV_FILE_PATTERN = "/*.csv";
    private static final String CSV_SEPARATOR = ",";
    private static final String CSV_SEPARATOR_PATTERN = "[,;\\t]";
    private static final int CSV_COLUMN_COUNT = 4;

    private final AppCsvPersonProps personCsvProps;

    public PersonCsvParser(AppCsvPersonProps personCsvProps) {
        this.personCsvProps = personCsvProps;
    }

    /**
     * Iterates through the person CSV files to extract the person's name, lastname, zipcode, city and favorite color.
     * To also be able to handle broken or non-RFC-conform CSV files, the parsing happens manually without any third
     * party CSV parser by iterating through the data until the expected amount of data for a person has reached. The
     * CSV files must have four columns, and each column is required to have data.
     *
     * @return the list of persons parsed from the CSV files
     * @throws IOException if reading CSV files failed
     */
    public List<Person> readFromCsv() throws IOException {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] csvResources = resolver.getResources(personCsvProps.getDirectory() + CSV_FILE_PATTERN);

        List<Person> persons = new ArrayList<>();
        for (Resource resource : csvResources) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                List<String> fields = new ArrayList<>();
                String line;
                while ((line = StringUtils.strip(reader.readLine())) != null && !line.isEmpty()) {
                    String[] columns = line.split(CSV_SEPARATOR_PATTERN);
                    for (String column : columns) {
                        column = StringUtils.strip(column);
                        if (StringUtils.isEmpty(column)) {
                            continue;
                        }
                        fields.add(column);
                        if (fields.size() == CSV_COLUMN_COUNT) {
                            try {
                                persons.add(createPerson(persons.size() + 1, fields));
                            } catch (IllegalArgumentException e) {
                                LOG.warn("Failed to parse CSV entry: {}", e.getMessage());
                            }
                            fields.clear();
                        }
                    }
                }
            }
        }
        return persons;
    }

    public void saveToCsv(Person person) throws IOException, URISyntaxException {
        String filename = personCsvProps.getDirectory() + "/zzz_persons_" + LocalDate.now() + ".csv";
        Path csvFilePath = Path.of(new URI(filename));
        if (!Files.exists(csvFilePath)) {
            Files.createFile(csvFilePath);
        }
        StringBuilder sb = new StringBuilder();
        sb.append(person.getLastname()).append(CSV_SEPARATOR);
        sb.append(person.getName()).append(CSV_SEPARATOR);
        sb.append(person.getZipcode()).append(" ").append(person.getCity()).append(CSV_SEPARATOR);
        sb.append(person.getColor().getId());
        try (
                FileWriter fw = new FileWriter(csvFilePath.toFile(), true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter writer = new PrintWriter(bw)
        ) {
            writer.println(sb);
        }
    }

    private static Person createPerson(long id, List<String> fields) {
        Person person = new Person(id);
        person.setLastname(sanitizeAlphabeticString(fields.get(0)));
        person.setName(sanitizeAlphabeticString(fields.get(1)));
        String[] zipcodeAndStreet = fields.get(2).split(" ", 2);
        person.setZipcode(sanitizeNumericString(zipcodeAndStreet[0]));
        person.setCity(sanitizeAlphabeticString(zipcodeAndStreet[1]));
        person.setColor(Color.fromId(Integer.parseInt(sanitizeNumericString(fields.get(3)))));
        return person;
    }

}
