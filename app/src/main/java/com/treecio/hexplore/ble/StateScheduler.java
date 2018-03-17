package com.treecio.hexplore.ble;


public abstract class StateScheduler {

    private State stateBroadcasting;
    private State stateDiscovery;

    public StateScheduler(State stateBroadcasting, State stateDiscovery) {
        this.stateBroadcasting = stateBroadcasting;
        this.stateDiscovery = stateDiscovery;
    }

    public abstract void start();
    public abstract void stop();

}
