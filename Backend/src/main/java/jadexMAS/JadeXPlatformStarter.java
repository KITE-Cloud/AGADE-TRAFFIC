package jadexMAS;

import controllers.SimulationResultBuilder;
import jadex.base.Starter;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.service.RequiredServiceInfo;
import jadex.bridge.service.search.SServiceProvider;
import jadex.bridge.service.types.cms.CreationInfo;
import jadex.bridge.service.types.cms.IComponentManagementService;
import jadex.commons.future.IFuture;
import jadex.commons.future.ThreadSuspendable;
import model.AgentPropertiesEnum;

import java.util.HashMap;
import java.util.Map;


public class JadeXPlatformStarter {

    private CreationInfo ci;
    private IComponentManagementService cms;
    private ThreadSuspendable sus;

    public JadeXPlatformStarter() {
        String[] args = new String[]{"-gui", "false", "-welcome", "false", "-cli", "false", "-printpass",
                "false", "-debugfutures", "false"};

        IFuture<IExternalAccess> platfut = Starter.createPlatform(args);

        cms = SServiceProvider.getService(platfut.get(), IComponentManagementService.class, RequiredServiceInfo.SCOPE_PLATFORM).get();
        ci = new CreationInfo();

    }

    /*public static void main(String[] args) {
        JadeXPlatformStarter jadeXPlatformStarter = new JadeXPlatformStarter();
    }*/


    public ThreadSuspendable getThreadSuspendable() {
        return sus;
    }

    /**
     * Possible Agent Properties see AgentPropertiesEnum
     **/
    public void deployAgent(String agentName, String classFile, Map<AgentPropertiesEnum, Object> agentProperties) {

        Map<String, Object> argumentMap = new HashMap<>();
        for (AgentPropertiesEnum key : agentProperties.keySet()) {
            argumentMap.put(key.name(), agentProperties.get(key));
        }
        SimulationResultBuilder.getInstance().registerAgent();
        ci.setArguments(argumentMap);
        cms.createComponent(agentName, classFile, ci).getFirstResult();
    }

    private void removeAgent(IComponentIdentifier ici) {
        cms.destroyComponent(ici).get();
    }

  /*  public void setUpParticipatingAgents() {

        int agentNumber = 1;
        PropertyDTO propertyDTO = PropertyDAO.getInstance().load();
        Map<String, Map<String, Integer>> map = propertyDTO.getAgentIDLAssociation();

        for (String agentType : map.keySet()) {
            Map<String, Integer> idlIntMap = map.get(agentType);
            for (String idlOntology : idlIntMap.keySet()) {
                int number = idlIntMap.get(idlOntology);
                System.out.println(agentType.split("BDI")[0]);
                for (int i = 0; i < number; i++) {
                    Map<AgentPropertiesEnum, Object> agentProperties = new HashMap<>();
                    //agentProperties.put(AgentPropertiesEnum.agentType, agentType);

                    deployAgent(getAgentNameFromFullPathString(agentType) + "_C" + Constants.clientNumber + "_" + (agentNumber++),
                            agentType, agentProperties);
                }
            }
        }



    private String getAgentNameFromFullPathString(String fullPathString) {
        if (fullPathString.indexOf(".") > 0) {
            return fullPathString.substring(fullPathString.lastIndexOf(File.separator) + 1,
                    fullPathString.lastIndexOf(".")).split("BDI")[0];
        } else
            return fullPathString.substring(fullPathString.lastIndexOf(File.separator) + 1
            ).split("BDI")[0];
    }*/

}
