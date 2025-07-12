package app.launcher;

import java.util.Scanner;

import app.core.StateMachine;

public class Main {
	static String username, output;

    public enum State {
        GetUserInput,
        ProcessInput,
        Output
    }
    public static void main(String[] args) {
        StateMachine<State> stateMachine = new StateMachine<>(State.class);
        stateMachine.addState(State.GetUserInput, Main::getUserInput);
        stateMachine.addState(State.ProcessInput, Main::processInput);
        stateMachine.addState(State.Output, Main::output);
        stateMachine.startStateQueue();
        while (stateMachine.isStateQueueRunning()) {
            stateMachine.step();
        }
    }

    public static State getUserInput(State currentSatate) {
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("Enter username:");
    	username = scanner.nextLine();
    	scanner.close();
    	return State.ProcessInput;
    }
    public static State processInput(State currentState) {
    	output = "The username of this user is: \"" + username + "\"";
    	return State.Output;
    }
    public static State output(State currentState) {
    	System.out.println(output);
    	return null;
    }
}