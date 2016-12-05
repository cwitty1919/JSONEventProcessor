package com.logrythm;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static java.nio.file.StandardWatchEventKinds.*;
import java.nio.file.*;

public class Main {

    public static void main(String[] args) {

        ConcurrentStatisticStore statisticStore = new ConcurrentStatisticStore();

        final ExecutorService threadPool = Executors.newFixedThreadPool(20);

        long startTime = System.currentTimeMillis();

        String directory = System.getProperty("user.dir") + "/input";

        try {
            // Register input directory for FileWatch Service to detect incoming files or file modifications
            Path directoryPath = FileSystems.getDefault().getPath(directory);
            WatchService watchService = directoryPath.getFileSystem().newWatchService();
            directoryPath.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);

            // Spin off thread to calculate and print statistics each second
            threadPool.execute(new JSONStatisticAnalyzeTask(statisticStore, startTime));

            // Process any existing files in the directory
            if (directoryPath.toFile().listFiles().length > 0){
                for (File file : directoryPath.toFile().listFiles()){
                    threadPool.execute(new JSONProcessTask(statisticStore, file.getAbsolutePath()));
                }
            }

            // Process files as they are added to the directory
            for(;;) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event: key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind != OVERFLOW) {
                        WatchEvent<Path> ev = (WatchEvent<Path>) event;
                        String input = directory + ev.context().getFileName();
                        System.out.println(input);
                        threadPool.execute(new JSONProcessTask(statisticStore, input));
                    }
                }
            }
        }
        catch (Exception e){
            System.err.println(e);
        }
        finally {
            System.out.println("Shutting down JSON Event Processor....");
            threadPool.shutdownNow();
            System.out.println("Shut down complete.");
        }
    }
}

