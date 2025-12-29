package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileProcessor {
    private final String outputPath;
    private final String filePrefix;
    private final boolean appendMode;
    private final StatisticsCollector statisticsCollector;

    private PrintWriter intWriter;
    private PrintWriter floatWriter;
    private PrintWriter stringWriter;

    private boolean intFileCreated = false;
    private boolean floatFileCreated = false;
    private boolean stringFileCreated = false;

    public FileProcessor(String outputPath, String filePrefix, boolean appendMode) {
        this.outputPath = outputPath != null ? outputPath : ".";
        this.filePrefix = filePrefix != null ? filePrefix : "";
        this.appendMode = appendMode;
        this.statisticsCollector = new StatisticsCollector();

        try {
            Files.createDirectories(Paths.get(this.outputPath));
        } catch (IOException e) {
            System.err.println("Warning: Cannot create directory: " + e.getMessage());
        }
    }

    public void processFile(String inputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                try {
                    if (isInteger(line)) {
                        long value = Long.parseLong(line);
                        writeInteger(line);
                        statisticsCollector.addInteger(value);
                    } else if (isFloat(line)) {
                        double value = Double.parseDouble(line);
                        writeFloat(line);
                        statisticsCollector.addFloat(value);
                    } else {
                        writeString(line);
                        statisticsCollector.addString(line);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing line " + lineNumber +
                            " in file " + inputFile + ": " + e.getMessage());
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + inputFile);
        } catch (IOException e) {
            System.err.println("Error reading file " + inputFile + ": " + e.getMessage());
        }
    }

    private boolean isInteger(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isFloat(String str) {
        try {
            Double.parseDouble(str);
            return !isInteger(str);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void writeInteger(String data) throws IOException {
        if (!intFileCreated) {
            Path filePath = Paths.get(outputPath, filePrefix + "integers.txt");
            StandardOpenOption[] options = getFileOptions();
            intWriter = new PrintWriter(Files.newBufferedWriter(filePath, options));
            intFileCreated = true;
        }
        intWriter.println(data);
    }

    private void writeFloat(String data) throws IOException {
        if (!floatFileCreated) {
            Path filePath = Paths.get(outputPath, filePrefix + "floats.txt");
            StandardOpenOption[] options = getFileOptions();
            floatWriter = new PrintWriter(Files.newBufferedWriter(filePath, options));
            floatFileCreated = true;
        }
        floatWriter.println(data);
    }

    private void writeString(String data) throws IOException {
        if (!stringFileCreated) {
            Path filePath = Paths.get(outputPath, filePrefix + "strings.txt");
            StandardOpenOption[] options = getFileOptions();
            stringWriter = new PrintWriter(Files.newBufferedWriter(filePath, options));
            stringFileCreated = true;
        }
        stringWriter.println(data);
    }

    private StandardOpenOption[] getFileOptions() {
        if (appendMode) {
            return new StandardOpenOption[] {
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            };
        } else {
            return new StandardOpenOption[] {
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            };
        }
    }

    public void printStatistics(boolean fullStats) {
        statisticsCollector.printStatistics(fullStats);
    }

    public void close() {
        if (intWriter != null) intWriter.close();
        if (floatWriter != null) floatWriter.close();
        if (stringWriter != null) stringWriter.close();
    }
}