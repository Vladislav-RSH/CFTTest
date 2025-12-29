package org.example;

public class Main {
    public static void main(String[] args) {
        FileFilterApp app = new FileFilterApp();
        app.parseArguments(args);
        app.run();
    }
}