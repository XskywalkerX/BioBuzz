package org.firstinspires.ftc.teamcode.Util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CSVData {
    private final File file;
    private final List<String> headers = new ArrayList<>();
    private final Map<String, String> currentRow = new LinkedHashMap<>();
    private boolean headerChanged = false;

    public CSVData(String filePath) {
        this.file = new File(filePath);
        createFile();
    }

    public void createFile() {
        try {
            if (!file.exists()) {
                file.createNewFile();
            } else if (file.length() > 0) {
                try (CSVReader reader = new CSVReader(new FileReader(file))) {
                    String[] existingHeaders = reader.readNext();
                    if (existingHeaders != null) {
                        for (String h : existingHeaders) {
                            headers.add(h);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addData(String label, Object value) {
        if (!headers.contains(label)) {
            headers.add(label);
            headerChanged = true;
        }
        currentRow.put(label, String.valueOf(value));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextRow() {
        try {
            if (headerChanged) {
                updateHeaderLine();
                headerChanged = false;
            }

            String[] rowArray = new String[headers.size()];
            for (int i = 0; i < headers.size(); i++) {
                rowArray[i] = currentRow.getOrDefault(headers.get(i), "");
            }

            try (CSVWriter writer = new CSVWriter(new FileWriter(file, true))) {
                writer.writeNext(rowArray);
            }

            currentRow.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateHeaderLine() throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());

        java.io.StringWriter sw = new java.io.StringWriter();
        try (CSVWriter tempWriter = new CSVWriter(sw)) {
            tempWriter.writeNext(headers.toArray(new String[0]));
        }
        String formattedHeader = sw.toString().trim();

        if (lines.isEmpty()) {
            lines.add(formattedHeader);
        } else {
            lines.set(0, formattedHeader);
        }
        Files.write(file.toPath(), lines);
    }
}
