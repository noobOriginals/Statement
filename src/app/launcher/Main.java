package app.launcher;

import java.util.Scanner;

import app.core.StateMachine;

public class Main {
	static String username, output;
    static Scanner scanner;

    public enum State {
        EXIT,
        GetUserInput,
        ProcessInput,
        Output
    }
    public static void main(String[] args) {
        StateMachine<State> stateMachine = new StateMachine<>(State.class);
        stateMachine.addState(State.GetUserInput, Main::getUserInput);
        stateMachine.addState(State.ProcessInput, Main::processInput);
        stateMachine.addState(State.Output, Main::output);
        stateMachine.setStartingState(State.GetUserInput);
        stateMachine.setExitState(State.EXIT);
        stateMachine.startStateQueueAndReset();
        while (stateMachine.isStateQueueRunning()) {
            stateMachine.autoExitStep();
        }
    }

    public static State getUserInput() {
    	scanner = new Scanner(System.in);
    	System.out.println("Enter username:");
    	username = scanner.nextLine();
    	return State.ProcessInput;
    }
    public static State processInput() {
    	output = "The username of this user is: \"" + username + "\"";
    	return State.Output;
    }
    public static State output() {
    	System.out.println(output);
        if (username.compareTo("q") == 0) {
    	    scanner.close();
            return State.EXIT;
        }
    	return State.GetUserInput;
    }
}