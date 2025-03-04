package app.core;

public class ThreadedState implements Runnable {
    private Thread thread;
    private boolean isDone = true;
    private State state;
    private String stateName, nextState;

    public ThreadedState(String stateName, State state) {
        this.state = state;
        this.stateName = stateName;
        nextState = null;
    }

    public void start() {
        thread = new Thread(this, stateName + " Thread");
        thread.start();
    }

    @Override
    public void run() {
        isDone = false;
        try {
            nextState = state.run(stateName);
        } catch (Exception e) {
            nextState = null;
            e.printStackTrace();
        }
        isDone = true;
    }

    public boolean isDone() {
        return isDone;
    }
    public String getStateName() {
        return stateName;
    }
    public String getNextState() throws Exception {
        if (isDone) return nextState;
        else throw new Exception("If state is not done, the next state is not known.");
    }
}
