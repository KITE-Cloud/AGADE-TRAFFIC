package jadexMAS.agents;

import controllers.ConfigurationHandler;
import model.frontend.Configurations;
import rmi.RMIClient;
import jadexMAS.goals.DirectThePlanGoal;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentBody;
import jadex.micro.annotation.AgentCreated;
import controllers.Client;
import controllers.TrafficJamDataHandler;
import jadexMAS.YellowPages;

import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.logging.Level;

@Agent
public class TrafficDirectorBDI {

    @Belief
     int TICKLENGTH_IN_SECONDS;

    @Belief
    RMIClient routingRMI;

    @Agent
    protected IInternalAccess agent;

    @AgentCreated
    public void init() {
        YellowPages.addDirector(agent);
        routingRMI = RMIClient.getInstance();
    }

    @AgentBody
    public void body() {
        this.TICKLENGTH_IN_SECONDS = Client.getInstance().getTICKLENGTH_IN_SECONDS();
        //System.out.println("Director goal DirectThePlan");
        agent.getComponentFeature(IBDIAgentFeature.class).dispatchTopLevelGoal(new DirectThePlanGoal()).get();
    }


    @Plan(trigger = @Trigger(goals = DirectThePlanGoal.class))
    protected boolean directTheGamePlan(DirectThePlanGoal goal) {

        Client client = Client.getInstance();
        Configurations simConfig = ConfigurationHandler.getInstance().getConfigurations();
        int numTicksToSimulate = (simConfig.getSimulationTime()*60*60)/this.TICKLENGTH_IN_SECONDS;
        System.out.println("Director beginnt mit der Simulation");
        System.out.println("NumTicks to Simulate: "+ numTicksToSimulate);
        int tick = client.getCurrentTick();
        int initialTick = tick;

        while (tick < numTicksToSimulate+initialTick){
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> Tick: " + tick + "; Total number of agents created: " + Client.numberOfCreatedAgents /*+"; Moving agents in last tick: " + YellowPages.getAllAgents().size()*/);

            synchronized (agent) {
                try {
                    client.createAgentsAtTick(tick);
                    agent.getLogger().log(Level.INFO, "create agents wait()");
                    //System.out.println("equip with wait()");
                    agent.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            synchronized (agent) {
                try {
                    client.equipParticipatingAgentsWithOntologyPlan();
                    agent.getLogger().log(Level.INFO, "equipParticipantsWithOntologyPlan wait()");
                    //System.out.println("equip with wait()");
                    agent.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            synchronized (agent) {
                try {
                    client.planAgentJourneys();
                    agent.getLogger().log(Level.INFO, "planAgentJourneys wait()");
                    agent.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Traffic Data
            //System.out.println("Handle traffic jam");
            HashMap<Long, Integer> roadInfo = TrafficJamDataHandler.getInstance().getRoadInformation();
            routingRMI.nextTick(TICKLENGTH_IN_SECONDS, roadInfo, simConfig.getTrafficActionObjects());

            // Before we run the phases we have to make sure that each reasoner
            // is up to date by calling flush()!
            synchronized (agent) {
                try {
                    Client.getInstance().flushReasoners();
                    agent.getLogger().log(Level.INFO, "flushReasoners wait()");
                    //System.out.println("Director flushReasoners wait()");
                    agent.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // ActingPhase for all jadex.agents
            synchronized (agent) {
                try {
                    Client.getInstance().processActingPhase();
                    agent.getLogger().log(Level.INFO, "ActingPhase wait()");
                    //System.out.println("Director wartet!");
                    agent.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //fixme: temporary solution - not working all the time - may need some luck
            if(YellowPages.getAllAgents().size() > 800){
                try { //create pause between ticks for OS to hopefully release unsused "handles"
                    if (YellowPages.getAllAgents().size() < 1500) {
                        Thread.sleep(5000);
                    } else {
                        Thread.sleep(10000);

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            tick++;
        }

        // pause ActingPhase for all jadex.agents
        synchronized (agent) {
            try {
                Client.getInstance().pauseAgents();
                agent.getLogger().log(Level.INFO, "Pause ActingPhase wait()");
                //System.out.println("Director wartet bis alle pausiert haben!");
                agent.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("END OF SIMULATION");
        Future future = Client.getInstance().future;
        if (future != null) {
            future.isDone();
        } else {
            System.out.println("future is null, could not trigger isDone()");
        }
        Client.getInstance().shutdownExecutorService();

        return true;
    }




}
