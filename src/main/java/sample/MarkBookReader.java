package sample;

import java.io.*;
import java.util.LinkedHashMap;

public class MarkBookReader {
    public static String currentUser;

    public static LinkedHashMap<String, String> readMarkBook() throws IOException {
        currentUser = getCurrentUser();
        InputStream stream = MarkBookReader.class.getResourceAsStream("/markbook/markbook.txt");
        Reader reader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(reader);
        LinkedHashMap<String, String> termsMap = new LinkedHashMap<>();

        String currentLine = bufferedReader.readLine();
        while (currentLine != null) {
            String[] split;
            if (currentLine.contains(currentUser)) {
                split = currentLine.split(",");
                termsMap.put(split[0].trim(), split[1].trim());
            }
            currentLine = bufferedReader.readLine();
        }
        return termsMap;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String currentTermList) {
        currentUser = currentTermList;
    }

}
