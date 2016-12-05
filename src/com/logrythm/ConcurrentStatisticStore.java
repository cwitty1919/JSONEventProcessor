package com.logrythm;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by chriswittenberg on 12/5/16.
 *
 * This class contains the values that are incremented by the JSON Process Tasks and  are consumed by the
 * JSON Statistic Analyze Task.
 */
public class ConcurrentStatisticStore {
    public AtomicInteger eventCount = new AtomicInteger();
    public AtomicInteger alarmCount = new AtomicInteger();
    public AtomicInteger imageCount = new AtomicInteger();
    public AtomicInteger eventsProcessed = new AtomicInteger();
    public AtomicLong totalProcessingTime = new AtomicLong();
}
