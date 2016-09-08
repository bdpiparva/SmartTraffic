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
    private float adjustmentFactor;
    private int currentLaneIndex;
    private int yellowTime;

    public Signal(String name, int bufferTime) {

        this.isRunning = true;
        this.bufferTime = bufferTime;
        this.name = name;
        this.lanes = new ArrayList<>(4);
        this.adjustmentFactor = 15f;
        this.currentLaneIndex = 0;
        this.yellowTime = 3;
    }

    public void setAdjustmentFactor(float adjustmentFactor) {
        this.adjustmentFactor = adjustmentFactor;
    }

    public void addLane(Lane lane) {
        lanes.add(lane);
        totalTime += lane.getGreenTime();
        shortestLaneDuration(lane.getGreenTime());
    }

    private void shortestLaneDuration(int greenTime) {
        if (shortestLaneDuration == 0) {
            shortestLaneDuration = greenTime;
        } else if (greenTime < shortestLaneDuration) {
            shortestLaneDuration = greenTime;
        }
    }

    public void start() throws InterruptedException {
        lanes.forEach(lane -> lane.startSensor());
        Thread trafficProcessor = new Thread(new TrafficProcessor());
        trafficProcessor.setPriority(Thread.MAX_PRIORITY);
        greenTimeFactor = (int) Math.ceil(shortestLaneDuration / adjustmentFactor);
        trafficProcessor.start();
    }

    private class TrafficProcessor implements Runnable {

        @Override
        public void run() {

            long start = System.currentTimeMillis();
            boolean isProcessed = false;
            int timer = 0;
            currentLaneIndex = 0;
            Lane currentLane = lanes.get(currentLaneIndex);
            while (true) {

                if (isRunning) {
                    long beforeProcessing = System.currentTimeMillis();

                    System.out.println(((System.currentTimeMillis() + 1 - start) / 1000) + ", ");

                    ++timer;

                    if (timer >= currentLane.getGreenTime() - yellowTime) {
                        currentLane.display("Yellow " + timer);
                    } else {
                        currentLane.display("Green " + timer);
                    }

                    if (timer == currentLane.getGreenTime()) {
                        currentLane.display("Red");
                        ++currentLaneIndex;
                        if (lanes.size() > currentLaneIndex)
                            currentLane = lanes.get(currentLaneIndex);
                        timer = 0;
                    }


                    if (System.currentTimeMillis() >= ((totalTime - bufferTime) * 1000) + start && !isProcessed) {
                        adjustLaneTimings();
                        isProcessed = true;
                    }

                    if (System.currentTimeMillis() >= start + (totalTime * 1000)) {
                        start = System.currentTimeMillis();
                        timer = 0;
                        currentLaneIndex = 0;
                        currentLane = lanes.get(0);
                        lanes.forEach(lane -> lane.updateGreenTime());
                        isProcessed = false;
                        System.out.print("\nTimer: ");
                    }
                    try {
                        if (System.currentTimeMillis() - beforeProcessing < 995) {
                            Thread.sleep(1000 - (System.currentTimeMillis() - beforeProcessing));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        private void adjustLaneTimings() {

            System.out.println("Adjust Time");
            List<Lane> lanesAtPeak = new ArrayList<>(4);
            lanes.forEach(lane -> {
                if (lane.isTrafficAtPeak()) {
                    lanesAtPeak.add(lane);
                    System.out.print("is traffic at peak ");
                }
            });
            int noOfLanesAtPeakTraffic = lanesAtPeak.size();

            System.out.println("noOfLanesAtPeakTraffic " + noOfLanesAtPeakTraffic);
            if (noOfLanesAtPeakTraffic != 0 && noOfLanesAtPeakTraffic != lanes.size()) {
                int timeToAdd = (lanes.size() - noOfLanesAtPeakTraffic) * greenTimeFactor;
                int timeToReduce = noOfLanesAtPeakTraffic * greenTimeFactor;

                lanes.forEach(lane -> {
                    System.out.println("Is at peak " + lane.getGreenTime());
                    System.out.println("Green time factor " + greenTimeFactor);

                    if (lanesAtPeak.contains(lane)) {
                        lane.setNextGreenTime(lane.getDefaultGreenTime() + timeToAdd);
                    } else {
                        lane.setNextGreenTime(lane.getDefaultGreenTime() - timeToReduce);
                    }

                    System.out.println("Adjusted green time " + lane.getGreenTime());
                });
            } else {
                lanes.forEach(lane -> {
                    lane.setNextGreenTimeAsDefault();
                    System.out.println("Adjusted green time " + lane.getGreenTime());
                });
            }
        }
    }
}
