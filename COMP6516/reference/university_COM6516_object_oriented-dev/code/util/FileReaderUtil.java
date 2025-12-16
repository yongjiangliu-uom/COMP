package util;
/*
 * Author : Manu Kenchappa Junjanna
 * Email : mkenchappajunjanna1@sheffield.ac.uk
 * Created on Sun Dec 3 2023
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileReaderUtil {

    public static StringBuilder readFile(String filePath, int maxCharLimit) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (content.length() + line.length() <= maxCharLimit) {
                    content.append(line).append("\n");
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return content;
    }
}
