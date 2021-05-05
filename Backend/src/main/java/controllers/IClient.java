package controllers;

public interface IClient {

    void equipParticipatingAgentsWithOntologyPlan();

    void processActingPhase();

    void flushReasoners();

    void shutdownExecutorService();

    boolean executorFinished();

    void createAgentsAtTick(int tick);

    void pauseAgents();
}
