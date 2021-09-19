import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Parser {
    private static final String COMMA_SEPARATOR = ",";

    @NotNull
    public static List<List<String>> parseValuesWithOpenCSV(@NotNull String fileName) {
        List<List<String>> values = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                values.add(Arrays.asList(nextLine));
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
        return values;
    }

    @NotNull
    public static List<List<String>> parseValues(@NotNull String fileName) {
        List<List<String>> values = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(line -> values.add(getRecordFromLine(line)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }

    @NotNull
    private static List<String> getRecordFromLine(@NotNull String line) {
        List<String> values = Arrays.asList(line.split(COMMA_SEPARATOR));
        for (int i = 0; i < values.size(); i++) {
            String value = values.get(i);
            value = value.trim();
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            values.set(i, value);
        }
        return values;
    }
}
