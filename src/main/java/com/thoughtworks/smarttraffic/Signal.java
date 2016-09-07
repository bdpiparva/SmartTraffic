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
    private int shortestLaneDuration;
    private int greenTimeFactor;

    public Signal(String name, int bufferTime) {

        this.isRunning = true;
        this.bufferTime = bufferTime;
        this.name = name;
        this.lanes = new ArrayList<>(4);
    }

    public void addLane(Lane lane) {
        lanes.add(lane);
        totalTime += lane.getGreenTime();
        shortestLaneDuration(lane.getGreenTime());
    }

    private void shortestLaneDuration(int greenTime) {
        if(shortestLaneDuration == 0){
            shortestLaneDuration = greenTime;
        }
        else if(greenTime < shortestLaneDuration) {
            shortestLaneDuration = greenTime;
            greenTimeFactor = (int)Math.ceil(shortestLaneDuration / 15);
        }
    }

    public void start() throws InterruptedException {
        lanes.forEach(lane -> lane.startSensor());
        Thread trafficProcessor = new Thread(new TrafficProcessor());
        trafficProcessor.setPriority(Thread.MAX_PRIORITY);
        trafficProcessor.start();
    }

    private class TrafficProcessor implements Runnable {

        @Override
        public void run() {

            long start = System.currentTimeMillis();
            boolean isProcessed = false;
            while (true) {

                if (isRunning) {
                    System.out.println(((System.currentTimeMillis() + 1 - start) / 1000) + ", ");
                    long beforeProcessing = System.currentTimeMillis();

                    if (System.currentTimeMillis() >= ((totalTime - bufferTime) * 1000) + start && !isProcessed) {
                        adjustLaneTimings();
                        isProcessed = true;
                    }

                    if (System.currentTimeMillis() >= start + (totalTime * 1000)) {
                        start = System.currentTimeMillis();
                        isProcessed = false;
                        System.out.print("\nTimer: ");
                    }
                    try {
                        if(System.currentTimeMillis() - beforeProcessing < 995) {
                            Thread.sleep(1000 - (System.currentTimeMillis() - beforeProcessing));
                        } else {
                            Thread.sleep(1000);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void adjustLaneTimings() {

            lanes.forEach(lane -> lane.setGreenTimeAsDefault());

            int noOfLanesAtPeakTraffic = (int) lanes.stream().filter(lane -> lane.isTrafficAtPeak()).count();

            if(noOfLanesAtPeakTraffic != 0 && noOfLanesAtPeakTraffic != lanes.size()){


                lanes.forEach(lane -> {

                    int adjustedGreenTime = 0;
                    if(lane.isTrafficAtPeak()) {
                        adjustedGreenTime = lane.getGreenTime() - ((lanes.size() - noOfLanesAtPeakTraffic) * greenTimeFactor);
                    } else {
                        adjustedGreenTime = lane.getGreenTime() + (noOfLanesAtPeakTraffic * greenTimeFactor);
                    }

                    lane.setGreenTime(adjustedGreenTime);
                });
            }
        }
    }
}
