package app.core;

import java.util.EnumMap;

public class StateMachine<State extends Enum<State>> {
    private EnumMap<State, StateMethod<State>> states;
    private State currentState = null, nextState = null, startingState = null;
    private boolean stateQueueRunning = false;

    public StateMachine(Class<State> enumClass) {
        states = new EnumMap<>(enumClass);
    }

    public void addState(State state, StateMethod<State> method) {
        states.put(state, method);
        if (startingState == null) {
        	startingState = state;
        }
    }
    public void runState(State state) {
        if (!states.containsKey(state)) {
            throw new RuntimeException("The given state is innexistent!");
        }
        states.get(state).run(currentState);
    }
    public void setCurrentState(State state) {
        currentState = state;
    }
    public void setNextState(State state) {
        nextState = state;
    }
    public void setStartingState(State state) {
        startingState = state;
    }
    public void runCurrentState() {
        if (stateQueueRunning) {
            throw new RuntimeException("No states can be ran manually while the state queue is running, except if explicitly ran with the \"runState(stateName)\" method.");
        }
        if (!states.containsKey(currentState)) {
            throw new RuntimeException("The given state is innexistent!");
        }
        nextState = states.get(currentState).run(currentState);
    }
    public void runNextState() {
        if (stateQueueRunning) {
            throw new RuntimeException("No states can be ran manually while the state queue is running, except if explicitly ran with the \"runState(stateName)\" method.");
        }
        if (!states.containsKey(currentState)) {
            throw new RuntimeException("The given state is innexistent!");
        }
        currentState = nextState;
        nextState = states.get(currentState).run(currentState);
    }
    public void resetStateQueue() {
        nextState = currentState = startingState;
    }
    public void startStateQueue() {
        if (!stateQueueRunning) {
            resetStateQueue();
        }
        stateQueueRunning = true;
    }
    public void stopStateQueue() {
        stateQueueRunning = false;
    }
    public void step() {
        if (!stateQueueRunning) {
            throw new RuntimeException("State queue must be started before stepping.");
        }
        currentState = nextState;
        nextState = states.get(currentState).run(currentState);
        if (nextState == null) {
            stopStateQueue();
        }
    }
    public boolean isStateQueueRunning() {
    	return stateQueueRunning;
    }

    // Utility classes
    @FunctionalInterface
    public interface StateMethod<State> {
        public State run(State currentState);
    }
}
