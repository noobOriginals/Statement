package app.core;

import java.util.HashMap;

public class StateMachine {
    private HashMap<String, State> states = new HashMap<>();
    private String currentState = null, nextState = null;
    private boolean stateQueueRunning = false;

    public void addState(String stateName, State state) throws Exception {
        if (states.containsKey(stateName)) throw new Exception("Cannot add a state with a name that has already been used.\nTrying to add state " + stateName + ".");
        states.put(stateName, state);
        if (states.size() < 2) currentState = nextState = stateName;
    }

    public void runState(String stateName) {
        try {
            states.get(stateName).run(stateName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setCurrentState(String stateName) {
        currentState = stateName;
    }
    public void setNextState(String stateName) {
        nextState = stateName;
    }
    public String getCurrentState() {
        return currentState;
    }
    public String getNextState() {
        return nextState;
    }

    public void runCurrentState() throws Exception {
        if (stateQueueRunning) throw new Exception("No states can be ran manually while the state queue is running, except if explicitly ran with the \"runState(stateName)\" method.");
        try {
            nextState = states.get(currentState).run(currentState);
        } catch (Exception e) {
            nextState = null;
            e.printStackTrace();
        }
    }
    public void runNextState() throws Exception {
        if (stateQueueRunning) throw new Exception("No states can be ran manually while the state queue is running, except if explicitly ran with the \"runState(stateName)\" method.");
        currentState = nextState;
        try {
            nextState = states.get(currentState).run(currentState);
        } catch (Exception e) {
            nextState = null;
            e.printStackTrace();
        }
    }
    private void runNextStep() {
        currentState = nextState;
        try {
            nextState = states.get(currentState).run(currentState);
        } catch (Exception e) {
            nextState = null;
            e.printStackTrace();
        }
    }
    public void startStateQueue() {
        if (!stateQueueRunning) nextState = currentState;
        stateQueueRunning = true;
    }
    public void stopStateQueue() {
        stateQueueRunning = false;
    }
    public void step() throws Exception {
        if (!stateQueueRunning) throw new Exception("State queue must be started before stepping.");
        runNextStep();
        if (nextState == null) {
            stopStateQueue();
        }
    }
    public boolean isStateQueueRunning() {
        return stateQueueRunning;
    }
}
