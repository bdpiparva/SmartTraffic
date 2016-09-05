package com.thoughtworks.smarttraffic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by salonivithalani on 9/3/16.
 */
public class Signal {

    private String name;
    private List<Lane> lanes;
    private int totalTime;
    private boolean isRunning;
    private int bufferTime;

    public Signal(String name, int bufferTime) {

        this.isRunning = true;
        this.bufferTime = bufferTime;
        this.name = name;
        this.lanes = new ArrayList<>(4);
    }

    public void addLane(Lane lane) {
        lanes.add(lane);
        totalTime += lane.getGreenTime();
    }

    public void start() throws InterruptedException {

        lanes.forEach(lane -> lane.start());
        new Thread(new TrafficProcessor()).start();

    }

    private class TrafficProcessor implements Runnable {

        private void processLaneTraffic() {

            lanes.forEach(lane -> {
                boolean isAtPeak = lane.isTrafficAtPeak();
                System.out.println("Is at peak: " + isAtPeak);
            });
        }

        @Override
        public void run() {

            long start = System.currentTimeMillis();
            System.out.print("\nTotal time: " + totalTime + " Buffer Time: " + bufferTime + "\n");
            boolean isProcessed = false;
            while (true) {

                if (isRunning) {
                    System.out.println(((System.currentTimeMillis() + 1 - start) / 1000) + ", ");
                    if (System.currentTimeMillis() >= ((totalTime - bufferTime) * 1000) + start && !isProcessed) {
                        processLaneTraffic();
                        isProcessed = true;
                    }

                    if (System.currentTimeMillis() >= start + (totalTime * 1000)) {
                        start = System.currentTimeMillis();
                        isProcessed = false;
                        System.out.print("\nTimer: ");
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
