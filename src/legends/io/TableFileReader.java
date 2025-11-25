package legends.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Read whitespace-separated tables, skipping the first header line.
 */
public class TableFileReader {

    public static List<String[]> readTable(String path) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("#")) continue;
                if (first) {
                    first = false;
                    continue;
                }
                String[] tokens = line.split("\\s+");
                rows.add(tokens);
            }
        }
        return rows;
    }
}
