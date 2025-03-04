package app.core;

@FunctionalInterface
public interface State {
    public String run(String currentState) throws Exception;
}
