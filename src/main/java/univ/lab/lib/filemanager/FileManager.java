package univ.lab.lib.filemanager;

import java.io.*;

public class FileManager {
    public void write(String filename, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read(String filename) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    public void append(String filename, String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
