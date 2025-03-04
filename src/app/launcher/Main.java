package app.launcher;

import app.core.State;
import app.core.StateMachine;

public class Main {
    public static int count = 0;
    public static void main(String[] args) {
        State state1 = (String currentState) -> {
            System.out.println(count + "  1");
            count++;
            if (count > 10) return "state2";
            return currentState;
        };
        State state2 = (String currentState) -> {
            System.out.println(count + "  2");
            count++;
            if (count > 20) return null;
            return currentState;
        };
        StateMachine stateMachine = new StateMachine();
        try {
            stateMachine.addState("state1", state1);
            stateMachine.addState("state2", state2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(stateMachine.getCurrentState());
        System.out.println(stateMachine.getNextState());

        stateMachine.startStateQueue();
        while (stateMachine.isStateQueueRunning()) {
            try {
                stateMachine.step();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}