package org.example;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DataStatistics {
    private int count = 0;
    private long minInt = Long.MAX_VALUE;
    private long maxInt = Long.MIN_VALUE;
    private long sumInt = 0;
    private double minFloat = Double.MAX_VALUE;
    private double maxFloat = Double.MIN_VALUE;
    private double sumFloat = 0.0;
    private int minStringLength = Integer.MAX_VALUE;
    private int maxStringLength = Integer.MIN_VALUE;

    public void addInteger(long value) {
        count++;
        minInt = Math.min(minInt, value);
        maxInt = Math.max(maxInt, value);
        sumInt += value;
    }

    public void addFloat(double value) {
        count++;
        minFloat = Math.min(minFloat, value);
        maxFloat = Math.max(maxFloat, value);
        sumFloat += value;
    }

    public void addString(String value) {
        count++;
        int length = value.length();
        minStringLength = Math.min(minStringLength, length);
        maxStringLength = Math.max(maxStringLength, length);
    }

    public int getCount() {
        return count;
    }

    public String getShortStats(DataType type) {
        return type + " count: " + count;
    }

    public String getFullStats(DataType type) {
        StringBuilder sb = new StringBuilder();
        sb.append(type).append(" count: ").append(count);

        if (count > 0) {
            switch (type) {
                case INTEGER:
                    sb.append(", min: ").append(minInt);
                    sb.append(", max: ").append(maxInt);
                    sb.append(", sum: ").append(sumInt);
                    sb.append(", average: ").append(
                            BigDecimal.valueOf((double) sumInt / count)
                                    .setScale(2, RoundingMode.HALF_UP)
                    );
                    break;
                case FLOAT:
                    sb.append(", min: ").append(
                            BigDecimal.valueOf(minFloat)
                                    .setScale(2, RoundingMode.HALF_UP)
                    );
                    sb.append(", max: ").append(
                            BigDecimal.valueOf(maxFloat)
                                    .setScale(2, RoundingMode.HALF_UP)
                    );
                    sb.append(", sum: ").append(
                            BigDecimal.valueOf(sumFloat)
                                    .setScale(2, RoundingMode.HALF_UP)
                    );
                    sb.append(", average: ").append(
                            BigDecimal.valueOf(sumFloat / count)
                                    .setScale(2, RoundingMode.HALF_UP)
                    );
                    break;
                case STRING:
                    sb.append(", min length: ").append(minStringLength);
                    sb.append(", max length: ").append(maxStringLength);
                    break;
            }
        }
        return sb.toString();
    }
}