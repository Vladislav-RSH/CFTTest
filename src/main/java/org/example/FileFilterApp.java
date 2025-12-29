package org.example;

import java.util.ArrayList;
import java.util.List;

public class FileFilterApp {
    private String outputPath = null;
    private String filePrefix = "";
    private boolean appendMode = false;
    private boolean fullStats = false;
    private boolean shortStats = false;
    private final List<String> inputFiles = new ArrayList<>();

    public void parseArguments(String[] args) {
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    if (i + 1 < args.length) {
                        outputPath = args[++i];
                    } else {
                        System.err.println("Error: -o requires a path argument");
                    }
                    break;
                case "-p":
                    if (i + 1 < args.length) {
                        filePrefix = args[++i];
                    } else {
                        System.err.println("Error: -p requires a prefix argument");
                    }
                    break;
                case "-a":
                    appendMode = true;
                    break;
                case "-s":
                    shortStats = true;
                    break;
                case "-f":
                    fullStats = true;
                    break;
                default:
                    if (args[i].startsWith("-")) {
                        System.err.println("Warning: Unknown option: " + args[i]);
                    } else {
                        inputFiles.add(args[i]);
                    }
                    break;
            }
        }

        if (!shortStats && !fullStats) {
            shortStats = true;
        }
    }

    public void run() {
        if (inputFiles.isEmpty()) {
            System.err.println("Error: No input files specified");
            System.err.println("Usage: java -jar filterapp.jar [options] file1 file2 ...");
            System.err.println("Options:");
            System.err.println("  -o <path>    Output directory");
            System.err.println("  -p <prefix>  File prefix for output files");
            System.err.println("  -a           Append mode (default: overwrite)");
            System.err.println("  -s           Short statistics (default)");
            System.err.println("  -f           Full statistics");
            return;
        }

        FileProcessor processor = null;
        try {
            processor = new FileProcessor(outputPath, filePrefix, appendMode);

            for (String inputFile : inputFiles) {
                try {
                    processor.processFile(inputFile);
                } catch (Exception e) {
                    System.err.println("Error processing file " + inputFile + ": " + e.getMessage());
                }
            }

            processor.printStatistics(fullStats);

        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (processor != null) {
                processor.close();
            }
        }
    }
}