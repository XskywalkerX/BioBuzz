package org.firstinspires.ftc.teamcode.Util;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CSVData {
    private final File file;
    private final Telemetry telemetry;
    private final List<String> headers = new ArrayList<>();
    private final Map<String, String> currentRow = new LinkedHashMap<>();
    private boolean headerChanged = false;

    public CSVData(String filePath, Telemetry telemetry) {
        this.file = new File(filePath);
        this.telemetry = telemetry;
        createFile();
    }

    public void createFile() {
        try {
            File parentDir = file.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            } else if (file.length() > 0) {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String headerLine = reader.readLine();
                reader.close();

                if (headerLine != null && !headerLine.isEmpty()) {
                    for (String h : headerLine.split(",")) {
                        headers.add(h.replace("\"", "").trim());
                    }
                }
            }
        } catch (Exception e) {
            if (telemetry != null) {
                telemetry.addData("CSV Create Error", e.toString());
            }
        }
    }

    public void addData(String label, Object value) {
        if (!headers.contains(label)) {
            headers.add(label);
            headerChanged = true;
        }
        currentRow.put(label, escapeCsv(String.valueOf(value)));
    }

    public void nextRow() {
        if (currentRow.isEmpty()) return;

        try {
            if (headerChanged) {
                updateHeaderLine();
                headerChanged = false;
            }

            StringBuilder rowBuilder = new StringBuilder();
            for (int i = 0; i < headers.size(); i++) {
                if (i > 0) rowBuilder.append(",");
                String val = currentRow.get(headers.get(i));
                rowBuilder.append(val != null ? val : "");
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            writer.write(rowBuilder.toString());
            writer.newLine();
            writer.close();

            currentRow.clear();
        } catch (Exception e) {
            if (telemetry != null) {
                telemetry.addData("CSV Write Error", e.toString());
            }
        }
    }

    private void updateHeaderLine() throws IOException {
        List<String> lines = new ArrayList<>();
        if (file.exists() && file.length() > 0) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();
        }

        StringBuilder headerBuilder = new StringBuilder();
        for (int i = 0; i < headers.size(); i++) {
            if (i > 0) headerBuilder.append(",");
            headerBuilder.append(escapeCsv(headers.get(i)));
        }

        if (lines.isEmpty()) {
            lines.add(headerBuilder.toString());
        } else {
            lines.set(0, headerBuilder.toString());
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
        writer.close();
    }

    private String escapeCsv(String val) {
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }
}
