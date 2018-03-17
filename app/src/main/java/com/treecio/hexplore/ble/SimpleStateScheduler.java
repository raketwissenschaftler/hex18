package com.treecio.hexplore.ble;

import android.os.AsyncTask;

import java.util.Random;

/**
 * Created by s156386 on 17-3-2018.
 */

public class SimpleStateScheduler extends StateScheduler {
    // The scheduler running on a separate thread
    AsyncScheduler scheduler;

    private State stateBroadcasting;
    private State stateDiscovery;

    public SimpleStateScheduler(State stateBroadcasting, State stateDiscovery){
        super(stateBroadcasting, stateDiscovery);
        scheduler = new AsyncScheduler();
        this.stateBroadcasting = stateBroadcasting;
        this.stateDiscovery = stateDiscovery;
    }

    @Override
    public void start() {
        scheduler.execute(stateBroadcasting, stateDiscovery);
    }

    @Override
    public void stop(){
        scheduler.cancel(true);
    }

    private static class AsyncScheduler extends AsyncTask<State, Void, String> {

        @Override
        protected String doInBackground(State... states) {
            Random rand = new Random();
            while (true){
                try {
                    // Put the device in broadcast mode
                    states[0].transitionIn();
                    states[1].transitionOut();
                    Thread.sleep(3000);
                    // Put the device in discovery mode
                    states[0].transitionOut();
                    states[1].transitionIn();
                    Thread.sleep(1000);
                    // In broadcast again
                    states[0].transitionIn();
                    states[1].transitionOut();
                    // And discovery mode
                    Thread.sleep(3000);
                    states[0].transitionOut();
                    states[1].transitionIn();

                    // Add a random offset to prevent devices from never finding eachother when their cycles line up
                    Thread.sleep((long) (1000 * rand.nextDouble()));
                }catch (InterruptedException e){
                    // Put it in the broadcast mode when something fails

                    states[0].transitionIn();
                    states[1].transitionOut();
                    break;
                }

            }
            return "Execution stopped";
        }
    }
}
