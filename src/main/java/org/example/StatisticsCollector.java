package org.example;

import java.util.EnumMap;
import java.util.Map;

public class StatisticsCollector {
    private final Map<DataType, DataStatistics> statistics = new EnumMap<>(DataType.class);

    public StatisticsCollector() {
        for (DataType type : DataType.values()) {
            statistics.put(type, new DataStatistics());
        }
    }

    public void addInteger(long value) {
        statistics.get(DataType.INTEGER).addInteger(value);
    }

    public void addFloat(double value) {
        statistics.get(DataType.FLOAT).addFloat(value);
    }

    public void addString(String value) {
        statistics.get(DataType.STRING).addString(value);
    }

    public void printStatistics(boolean fullStats) {
        for (DataType type : DataType.values()) {
            DataStatistics stats = statistics.get(type);
            if (stats.getCount() > 0) {
                String statsString = fullStats ?
                        stats.getFullStats(type) :
                        stats.getShortStats(type);
                System.out.println(statsString);
            }
        }
    }
}