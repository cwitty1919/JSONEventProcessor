package com.logrythm;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by chriswittenberg on 12/5/16.
 *
 * This task consumes the numbers stored in the Concurrent StatisticStore class and uses them to calculate average
 * processing time and outputs the total event counts, number of alarms, number of images, and average processing time
 * to the console each second.
 */
public class JSONStatisticAnalyzeTask implements Runnable{
    private ConcurrentStatisticStore statisticStore;
    private long startTime;
    private double prevAvgProcessingTime = 0;

    public JSONStatisticAnalyzeTask(ConcurrentStatisticStore statisticStore, long startTime){
        this.statisticStore = statisticStore;
        this.startTime = startTime;
    }

    public void run(){

        while(true) {
            if (System.currentTimeMillis() - startTime >= 1000) {
                double avgProcessingTime = getAvgProcessingTime(statisticStore.totalProcessingTime, statisticStore.eventsProcessed);
                System.out.println(createOutputString(statisticStore.eventCount, statisticStore.alarmCount, statisticStore.imageCount, avgProcessingTime));
                startTime = System.currentTimeMillis();
                statisticStore.totalProcessingTime.set(0);
                statisticStore.eventsProcessed.set(0);
            }
        }
    }

    public double getAvgProcessingTime(AtomicLong totalProcessingTime, AtomicInteger eventsProcessed){
        if (eventsProcessed.get() > 0) {
            return (double) totalProcessingTime.get() / (double) eventsProcessed.get();
        }
        else{
            return 0.0;
        }
    }

    public static String createOutputString(AtomicInteger eventCount, AtomicInteger alarmCount, AtomicInteger imageCount, double avgProcessingTime){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Event Count: ").append(eventCount);
        stringBuilder.append(", Alarm Count: ").append(alarmCount);
        stringBuilder.append(", Image Count: ").append(imageCount);
        stringBuilder.append(", Average Processing Time: "). append(avgProcessingTime).append("ms");
        return stringBuilder.toString();
    }

}

