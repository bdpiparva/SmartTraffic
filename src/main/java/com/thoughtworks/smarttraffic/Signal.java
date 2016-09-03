package com.thoughtworks.smarttraffic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

/**
 * Created by salonivithalani on 9/3/16.
 */
public class Signal {

    private String name;
    private List<Lane> lanes;
    private int totalTime;
    private int timer;
    private boolean pause;
    private int bufferTime;
    private boolean processTraffic ;

    public Signal(String name, int bufferTime) {

        this.pause = true;
        this.processTraffic = false;
        new ArrayList<>(4);

        this.bufferTime = bufferTime;
        this.name = name;
    }

    public void addLane(Lane lane){
        lanes.add(lane);
        totalTime += lane.getGreenTime();
    }

    public void start() throws InterruptedException {

        lanes.forEach(lane -> lane.start());
        new Thread(new TrafficProcessor()).start();

        while(pause) {
            timer++;
            processTraffic = true;
            wait(1000);
        }
    }

    private class TrafficProcessor implements Runnable {

        private void processLaneTraffic() {

            //TODO: write logic to handle traffic and set timer of lanes
            lanes.forEach(lane -> {

            });
        }

        @Override
        public void run() {

            while (processTraffic) {

                if((timer + bufferTime) == totalTime){
                    processLaneTraffic();
                }

                if (timer == totalTime) {
                    timer = 0;
                }

                processTraffic = false;
            }
        }
    }
}
