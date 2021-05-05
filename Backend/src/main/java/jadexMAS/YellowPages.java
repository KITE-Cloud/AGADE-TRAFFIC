package jadexMAS;

import jadexMAS.agents.AbstractOWLAgent;
import jadexMAS.agents.IAgent;
import jadex.bridge.IInternalAccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YellowPages {

    private static Map<String, AbstractOWLAgent> ypParticipants = new HashMap<String, AbstractOWLAgent>();

    private static List<AbstractOWLAgent> ypParticipantsList = new ArrayList<AbstractOWLAgent>();

    private static IInternalAccess director;

    public static AbstractOWLAgent getParticipantByName(String name) {
        return ypParticipants.get(name);
    }

    public static void addParticipant(String name, AbstractOWLAgent agent) {
        ypParticipants.put(name, agent);
        ypParticipantsList.add(agent);
    }

    public static List<AbstractOWLAgent> getAllParticipants() {
        return ypParticipantsList;
    }

    public static void removeParticipant(IAgent agent) {
        ypParticipantsList.remove(ypParticipants.remove(agent.getAgentName()));
    }

    public static void addDirector(IInternalAccess director) {
        YellowPages.director = director;
    }

    public static IInternalAccess getDirector() {
        return director;
    }

    public static HashMap<String, String> getAllAgentsForRegistration() {
        HashMap<String, String> agentMap = new HashMap<>();
        for (AbstractOWLAgent p : ypParticipantsList) {
            agentMap.put(p.getAgentName(), p.getClass().getSimpleName());
        }
        return agentMap;
    }

    //TODO: Refactor!
    public static ArrayList<IAgent> getAllAgents() {
        ArrayList<IAgent> agentList = new ArrayList<IAgent>();
        agentList.addAll(ypParticipantsList);
        return agentList;
    }

    public static void reset(){
        for (IAgent agent :getAllAgents()){
            agent.kill();
        }
    }
}
