package com.logrythm;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

/**
 * Created by chriswittenberg on 12/5/16.
 *
 * This runnable task parses the JSON file from the given input path and increments the relevant counts in the
 * Concurrent Statistic Store class.
 */
public class JSONProcessTask implements  Runnable {
    private ConcurrentStatisticStore statisticStore;
    private String input;

    public JSONProcessTask(ConcurrentStatisticStore statisticStore, String input){
        this.statisticStore = statisticStore;
        this.input = input;
    }

    public void run() {
        try {
            long processingStartTime = System.currentTimeMillis();
            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject) parser.parse(new FileReader(input));
            String type = (String) object.get("Type");
            switch (type) {
                case "alarm":
                    statisticStore.alarmCount.getAndIncrement();
                    statisticStore.eventCount.getAndIncrement();
                    statisticStore.eventsProcessed.getAndIncrement();
                    break;
                case "img":
                    statisticStore.imageCount.getAndIncrement();
                    statisticStore.eventCount.getAndIncrement();
                    statisticStore.eventsProcessed.getAndIncrement();
                    break;
                default:
            }
            long currentTime = System.currentTimeMillis();
            statisticStore.totalProcessingTime.getAndAdd((currentTime - processingStartTime));
        }
        catch (Exception e){
            System.err.println(e.getStackTrace());
        }
    }
}
