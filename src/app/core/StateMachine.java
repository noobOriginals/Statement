package app.core;

import java.util.EnumMap;

/**
 * This is a state machine helper class.
 * <p>It helps organize the code that would be required to make a state machine, getting rid of the need for switch statements.
 * and using an {@code EnumMap} instead.
 *
 * @param <State> the enum where the keys are defined
 */
public class StateMachine<State extends Enum<State>> {
    private EnumMap<State, StateMethod<State>> states;
    private State currentState = null, nextState = null, startingState = null, exitState = null;
    private boolean stateQueueRunning = false;

    /**
     * Create a new object of type {@code StateMachine}
     * {@code StateMachine<Enum>} the {@code Enum} template parameter needs to be a user defined enum.
     * <p>{@code StateMachine<Enum>(Class<Enum> enumClass)} the {@code enumClass} is the user defined enum class.
     * object so if the user creates an enum: {@code enum States}, the given parameter would be {@code States.class}.
     *
     * @param enumClass
     */
    public StateMachine(Class<State> enumClass) {
        states = new EnumMap<>(enumClass);
    }

    /**
     * Maps a {@code StateMethod<State>} method callback to the given enum key (the state).
     *
     * @param state
     * @param method
     */
    public void addState(State state, StateMethod<State> method) {
        states.put(state, method);
    }
    /**
     * Simply runs the given state, no impact on other state machine behaviour what so ever.
     *
     * @param state
     * @throws RuntimeException if the specified key does not exist.
     */
    public void runState(State state) {
        if (!states.containsKey(state)) {
            throw new RuntimeException("The given state is innexistent!");
        }
        states.get(state).run();
    }
    /**
     * Same as {@link #runState(Enum)}, but this one also gives you the returned value from the method that was executed.
     * If the returned value was {@code null}, then this method return the {@code state} that was provided to be executed (predefined looping behavior).
     *
     * @param state
     * @return {@code Enum} the type of the user provided enum.
     * @throws RuntimeException if the specified key does not exist.
     */
    public State runAndGetState(State state) {
        if (!states.containsKey(state)) {
            throw new RuntimeException("The given state is innexistent!");
        }
        State returnedState = states.get(state).run();
        if (returnedState == null) {
            returnedState = state;
        }
        return returnedState;
    }
    /**
     * Sets the current state of the state machine.
     * Helpful if more control over what is happening is needed.
     *
     * @param state
     */
    public void setCurrentState(State state) {
        currentState = state;
    }
    /**
     * Sets the next state of the state machine.
     * Helpful if more control over what is happening is needed.
     *
     * @param state
     */
    public void setNextState(State state) {
        nextState = state;
    }
    /**
     * Returns the value of {@code currentState}.
     *
     * @return {@code Enum} the value of {@code currentState}.
     */
    public State getCurrentState() {
        return currentState;
    }
    /**
     * Returns the value of {@code nextState}.
     *
     * @return {@code Enum} the value of {@code nextState}.
     */
    public State getNextState() {
        return nextState;
    }
    /**
     * Sets the state from which the state machine starts running the state queue.
     * <p>Cannot be {@code null} and must be set before starting the state queue ({@link #startStateQueue()}).
     *
     * @param state
     * @throws RuntimeException if the given starting state state is {@code null}.
     */
    public void setStartingState(State state) {
        if (state == null) {
            throw new RuntimeException("Starting state cannot be null!");
        }
        startingState = state;
    }
    /**
     * Sets the state which will cause the state queue to stop when it is returned by the last executed state.
     * <p>Cannot be {@code null} and must be set before starting the state queue ({@link #startStateQueue()}).
     *
     * @param state
     * @throws RuntimeException if the given exit state is {@code null}.
     */
    public void setExitState(State state) {
        if (state == null) {
            throw new RuntimeException("Exit state cannot be null!");
        }
        exitState = state;
    }
    /**
     * Runs the state that is stored in {@code currentState} and sets {@code nextState} to the returned value.
     * If the returned value is {@code null} then {@code nextState} is set to {@code currentState} (predefined looping behavior).
     *
     * @throws RuntimeException if the state queue is running, as we cannot run these while the state queue is running.
     * @throws RuntimeException if the specified key ({@code currentState}) does not exist.
     */
    public void runCurrentState() {
        if (stateQueueRunning) {
            throw new RuntimeException("No states can be ran manually while the state queue is running, except if explicitly ran with the \"runState(state)\" or the \"runAndGetState(state)\" methods!");
        }
        if (!states.containsKey(currentState)) {
            throw new RuntimeException("The given state is innexistent!");
        }
        nextState = states.get(currentState).run();
        if (nextState == null) {
            nextState = currentState;
        }
    }
    /**
     * Sets {@code currentState} to {@code nextState}, runs the state that is stored in {@code currentState} and sets {@code nextState} to the returned value.
     * If the returned value is {@code null} then {@code nextState} is set to {@code currentState} (predefined looping behavior).
     *
     * @throws RuntimeException if the state queue is running, as we cannot run these while the state queue is running.
     * @throws RuntimeException if the specified key ({@code nextState}) does not exist.
     */
    public void runNextState() {
        if (stateQueueRunning) {
            throw new RuntimeException("No states can be ran manually while the state queue is running, except if explicitly ran with the \"runState(state)\" or the \"runAndGetState(state)\" methods!");
        }
        if (!states.containsKey(currentState)) {
            throw new RuntimeException("The given state is innexistent!");
        }
        currentState = nextState;
        nextState = states.get(currentState).run();
        if (nextState == null) {
            nextState = currentState;
        }
    }
    /**
     * Resets the state queue. Sets {@code currentState} and {@code nextState} to {@code startingState}.
     */
    public void resetStateQueue() {
        nextState = currentState = startingState;
    }
    /**
     * Starts the state queue. (Sets the {@code stateQueueRunning} boolean to {@code true}).
     *
     * @throws RuntimeException if the starting state is {@code null}.
     * @throws RuntimeException if the exit state is {@code null}.
     */
    public void startStateQueue() {
        if (startingState == null) {
            throw new RuntimeException("Starting state must not be null when starting a state queue!");
        }
        if (exitState == null) {
            throw new RuntimeException("Exit state must not be null when starting a state queue, as null is used for looping!");
        }
        stateQueueRunning = true;
    }
    /**
     * Same as {@link #startStateQueue()}, but it auto resets the state queue if the state queue was stopped before starting
     * <p>Starts the state queue. (Sets the {@code stateQueueRunning} boolean to {@code true}).
     * <p>If the state was stopped before starting it, the state queue is reset.
     *
     * @throws RuntimeException if the starting state is {@code null}.
     * @throws RuntimeException if the exit state is {@code null}.
     */
    public void startStateQueueAndReset() {
        if (startingState == null) {
            throw new RuntimeException("Starting state must not be null when starting a state queue!");
        }
        if (exitState == null) {
            throw new RuntimeException("Exit state must not be null when starting a state queue, as null is used for looping!");
        }
        if (!stateQueueRunning) {
            resetStateQueue();
        }
        stateQueueRunning = true;
    }
    /**
     * Stop the state queue. (Sets the {@code stateQueueRunning} boolean to {@code true}).
     */
    public void stopStateQueue() {
        stateQueueRunning = false;
    }
    /**
     * Steps the state queue once.
     * That means that it runs the state that is stored in {@code currentState} and sets {@code nextState} to the returned value.
     * If the returned value is {@code null} then {@code nextState} is set to {@code currentState} (predefined looping behavior).
     * <p>State queue must be started before stepping!
     *
     * @throws RuntimeException if the state queue is not started yet.
     */
    public void step() {
        if (!stateQueueRunning) {
            throw new RuntimeException("State queue must be started before stepping!");
        }
        currentState = nextState;
        nextState = states.get(currentState).run();
        if (nextState == null) {
            nextState = currentState;
        }
    }
    /**
     * Same as {@link #step()}, but auto stops the state queue when the exit state is returned.
     * <p>Steps the state queue once.
     * That means that it runs the state that is stored in {@code currentState} and sets {@code nextState} to the returned value.
     * If the returned value is {@code null} then {@code nextState} is set to {@code currentState} (predefined looping behavior).
     * If the returned value is equal to {@code exitState} then the state queue is automatically stopped.
     * <p>State queue must be started before stepping!
     *
     * @throws RuntimeException if the state queue is not started yet.
     */
    public void autoExitStep() {
        if (!stateQueueRunning) {
            throw new RuntimeException("State queue must be started before stepping!");
        }
        currentState = nextState;
        nextState = states.get(currentState).run();
        if (nextState == null) {
            nextState = currentState;
        }
        if (nextState == exitState) {
            stopStateQueue();
        }
    }
    /**
     * Returns the value of {@code stateQueueRunning} boolean that determines if the state queue is running (has been started yet) or not.
     *
     * @return {@code boolean} the value of {@code stateQueueRunning}.
     */
    public boolean isStateQueueRunning() {
    	return stateQueueRunning;
    }

    // Utility classes
    /**
     * Functional interface for storing methods to execute for each given state.
     *
     * @param <State> the return type of the method, in this case the enum where the keys are defined.
     */
    @FunctionalInterface
    public interface StateMethod<State> {
        public State run();
    }
}
