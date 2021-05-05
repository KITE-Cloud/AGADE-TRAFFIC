package jadexMAS.agents;

import constants.Constants;
import controllers.OntologyLoader;
import exceptions.PlanNotFoundException;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IExecutionFeature;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentCreated;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.AgentKilled;
import jadexMAS.YellowPages;
import jadexMAS.goals.EquipAgentsWithOntologyGoal;
import jadexMAS.goals.ProcessActingPhaseGoal;
import model.properties.PropertyDAO;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import uk.ac.manchester.cs.owl.owlapi.alternateimpls.ThreadSafeOWLReasoner;
import utility.OntoUtil;
import utility.OntoUtilBasics;
import utility.ReasonerConfiguration;
import utility.steps.DataPropValueStep;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;

@Agent
public abstract class AbstractOWLAgent implements IAgent {

    /** The agent. */
    @Agent protected IInternalAccess agent;

    /** The bdi api.*/
    @AgentFeature protected IBDIAgentFeature bdi;

    // Ontology
    @Belief
    protected OntologyLoader ontologyLoader;
  /*  @Belief
    protected OWLOntologyManager manager;
    @Belief
    protected DefaultPrefixManager pm;
    @Belief
    protected OWLDataFactory df;
    @Belief
    protected OWLOntology ontology;
    @Belief
    protected OWLReasoner reasoner; */
    @Belief
    protected OntoUtil ontoUtil;
    @Belief
    protected OntoUtilBasics ontoUtilBasics;

    private int roundCounter = 0;

    @Override
    @Belief
    public IInternalAccess getAgent() {
        return agent;
    }

    @Plan
    public abstract IFuture<Boolean> processCalculationPhasePlan();

    // Ontology Components
    @Override
    public OWLOntology getOntology() {
        //return ontology;
        return ontologyLoader.getOntology();
    }

    @Override
    public OWLOntologyManager getManager() {
        //return manager;
        return ontologyLoader.getManager();
    }

    @Override
    public DefaultPrefixManager getPrefixManager() {
        //return pm;
        return ontologyLoader.getPm();
    }

    @Override
    public OWLDataFactory getDataFactory() {
        //return df;
        return ontologyLoader.getDf();
    }

    @Override
    public OWLReasoner getReasoner() {
        //return reasoner;
        return ontologyLoader.getReasoner();
    }

    @Override
    public OntoUtil getOntoUtil() {
        return ontoUtil;
    }

    @Override
    public OntologyLoader getOntologyLoader() {
        return ontologyLoader;
    }

    @Override
    public OntoUtilBasics getOntoUtilBasics() {
        return ontoUtilBasics;
    }

    @AgentCreated
    public void init() {
        //Thread.currentThread().setName(agent.getComponentIdentifier().getLocalName());
        //LoggerUtil.configureLogger(agent);
        //acquaintances = new ArrayList<MyEdgeInfo>();
        YellowPages.addParticipant(agent.getComponentIdentifier().getLocalName(), this);
        //ontologyFilePath = getAgent().getExternalAccess().getArguments().get().get("ontologyFile").toString();

        //agent.getLogger().log(Level.INFO, agent.getComponentIdentifier().getLocalName() + " created. " + "I'm a " + getOntologyName() + "!");
    }

    // Plans for both Seller and Participant
    @Override
    public <T> T runPlan(String plan, Class<T> type) {
        try {
            Class<?> planClass = Class.forName("jadexMAS.plans." + plan);
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan(
                    planClass.getConstructor(AbstractOWLAgent.class)
                            .newInstance(this)).get();
        } catch (ClassNotFoundException e) {
            // e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (plan.equalsIgnoreCase(Constants.plnProcessActingPhasePlan)) {
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan(Constants.plnProcessActingPhasePlan);
        }if (plan.equalsIgnoreCase(Constants.plnProcessSocializationPhasePlan)) {
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan(Constants.plnProcessSocializationPhasePlan);
        }if (plan.equalsIgnoreCase(Constants.plnProcessCalculationPhasePlan)) {
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan(Constants.plnProcessCalculationPhasePlan);
        }if (plan.equalsIgnoreCase(Constants.plnRoundFinishedPlan)) {
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan(Constants.plnRoundFinishedPlan);
        }if (plan.equalsIgnoreCase(Constants.plnEquipParticipantsWithOntologyPlan)) {
            return (T) agent.getComponentFeature(IBDIAgentFeature.class).adoptPlan(Constants.plnEquipParticipantsWithOntologyPlan);
        }throw new PlanNotFoundException("Plan not found: " + plan + ", agent: " + getAgent().getComponentIdentifier().getLocalName());
    }

    public IFuture<Boolean> setGoal(String goalName) {
        try {
            Class<?> clazz = Class.forName("jadexMAS.goals." + goalName);
            Constructor<?> ctor = clazz.getConstructor();
            Object goal = ctor.newInstance();
           // boolean bool = (boolean) this.agent.getComponentFeature(IBDIAgentFeature.class).dispatchTopLevelGoal(goal).get();
        //    System.out.println(bool + " von Agent: " + this.getAgentName() + " für Goal: " + goalName);
            return this.agent.getComponentFeature(IBDIAgentFeature.class).dispatchTopLevelGoal(goal);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        //this.agent.getComponentFeature0(IBDIAgentFeature.class).dispatchTopLevelGoal(new DriveToGoal());

        return new Future<Boolean>(false);
    }

    @Override
    public IFuture<Void> synchroniseReasoner() {
        // reasoners should get latest ontology changes out of buffer

        try {
            // TODO: Look for a new reasoner version
            // Currently we have to recreate the reasoner, because flush() have
            // no effect since OWL-API 4.0.0
            //reasoner.dispose();
            //reasoner = new ThreadSafeOWLReasoner(
            //ReasonerConfiguration.createReasoner(ontology));
            //reasoner.dispose();
            //reasoner = null;
            //
            if (ontologyLoader.getReasoner() != null) {
                ontologyLoader.getReasoner().dispose();
            }
            ontologyLoader.setReasoner(new ThreadSafeOWLReasoner(ReasonerConfiguration.createReasoner(ontologyLoader.getOntology())));
            //reasoner = new ThreadSafeOWLReasoner(ReasonerConfiguration.createReasoner(ontology));
            //synchronized (ontology){
            //    synchronized (reasoner){
            //       reasoner.flush();
            //      reasoner.precomputeInferences();
            //    }
            //}


        } catch (Exception e) {
            agent.getLogger().warning("Reasoner crashed");
            System.out.println("Reasoner crashed " + agent);
            ontologyLoader.setReasoner(ReasonerConfiguration.createReasoner(ontologyLoader.getOntology()));
            //reasoner = ReasonerConfiguration.createReasoner(ontology);
        }

        // reasoner.dispose();
        // reasoner = ReasonerConfiguration.createReasoner(ontology);

        return IFuture.DONE;
    }

    @Plan(trigger = @Trigger(goals = EquipAgentsWithOntologyGoal.class))
    protected IFuture<Boolean> equipParticipantsWithOntologyPlan() {

        //System.out.println("Ontology wird dem Agent hinzugefügt");
        this.ontoUtil = new OntoUtil(this);
        this.ontologyLoader = new OntologyLoader();
        /*this.manager = ontologyLoader.getManager();
        this.pm= ontologyLoader.getPm();
        this.df = ontologyLoader.getDf();
        this.ontology = ontologyLoader.getOntology();
        this.reasoner= ontologyLoader.getReasoner();*/

        agent.getComponentFeature(IExecutionFeature.class).scheduleStep(new DataPropValueStep<Boolean>(Constants.ontINDMyself, "hasArrivedValue", false, this));

        return new Future<Boolean>(true);
    }

    @Plan
    protected IFuture<Boolean> roundFinishedPlan() {
        agent.getComponentFeature(IExecutionFeature.class).scheduleStep(
                new DataPropValueStep<Integer>(Constants.ontINDMyself, Constants.ontDPCurrentRound, ++roundCounter, this));
        return new Future<>(true);
    }

    public int getRoundCounter() {
        return roundCounter;
    }


    @Plan(trigger = @Trigger(goals = ProcessActingPhaseGoal.class))
    public IFuture<Boolean> processActingPhasePlan() {
        //System.out.println("Agent definiert nächste Aktion");

        //Ontology returns a plan that should be executed
        //runPlan("DriveToPlan", new Future<Boolean>().getClass());

        //Ontology returns a goal that should be fulfilled
        //setGoal("DriveToGoal");

        //Ontology returns a goal that should be fulfilled
        //Returns the returned result of the goal
        //return setGoal("DriveToGoal");

        try {
            // ask for next actions to do by this agent
            Set<OWLNamedIndividual> actionsSet = ontoUtil
                    .getObjectPropertyValues(Constants.ontINDMyself,
                            "hasNextAction");

            if (actionsSet.isEmpty()) {
                agent.getLogger().info(getAgentName() + ": No next action found!");
                System.out.println(getAgentName() + ": No next action found!");
                return new Future<Boolean>(false);
            } else {

                //System.out.println(getAgentName() + ": Action found!");
                Iterator<OWLNamedIndividual> it = actionsSet.iterator();
                PriorityQueue<OWLNamedIndividual> actionQueue =
                        new PriorityQueue<OWLNamedIndividual>(
                                PropertyDAO.getInstance().load().getNumberOfAgents(),
                                new Comparator<OWLNamedIndividual>() {
                                    @Override
                                    public int compare(OWLNamedIndividual ind1,
                                                       OWLNamedIndividual ind2) {
                                        Set<OWLLiteral> prioSet1 = ontoUtil
                                                .getDataPropertyValues(ind1,
                                                        Constants.ontDPHasPriority);
                                        Set<OWLLiteral> prioSet2 = ontoUtil
                                                .getDataPropertyValues(ind2,
                                                        Constants.ontDPHasPriority);

                                        if (prioSet1.size() != 1
                                                || prioSet2.size() != 1) {
                                            throw new RuntimeException(
                                                    "There is not exactly one hasPriority value of an agent action: " + getAgentName());
                                        }
                                        return prioSet1.iterator().next()
                                                .parseInteger()
                                                - prioSet2.iterator().next()
                                                .parseInteger();
                                    }
                                });
                while (it.hasNext()) {
                    actionQueue.add(it.next());
                }
                String nextAction = actionQueue.poll().getIRI().getShortForm();
                agent.getLogger().info(getAgentName() + ": Action " + nextAction + " chosen.");
                //System.out.println(getAgentName() + ": Action " + nextAction + " chosen.");
                //runPlan(nextAction, new Future<Boolean>().getClass());
                setGoal(nextAction);
                // System.out.println(getAgent().getAgentName() +": about to return true");
                return new Future<Boolean>(true);
            }
        } catch (RuntimeException re) {
            re.printStackTrace();
        }


        return new Future<Boolean>(true);
    }

    @AgentKilled
    public void kill() {
        this.getAgent().getLogger().info("Good bye! I have been killed!");
        YellowPages.removeParticipant(this);
        //TODO Inform the server about this!!

    }

    @Plan
    public abstract IFuture<Boolean> processSocializationPhasePlan();

    public Set<OWLLiteral> getOWLLiteralFromDataProperty(String subjectName, String dataPropName) {

        return ontoUtil.getDataPropertyValues(subjectName, dataPropName);

    }

    public Set<OWLNamedIndividual> getOWLNamedIndividualFromObjectProperty(String subjectName, String objPropName) {
        return ontoUtil.getObjectPropertyValues(subjectName, objPropName);
    }


    public abstract void setUpAgent();

}
