package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.ServerHandler;
import it.polimi.ingsw.Network.Messages.toClient.ActionPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.CharacterPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.JoiningPhase.*;
import it.polimi.ingsw.Network.Messages.toClient.MessageToClient;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.CloudsUpdateMessage;
import it.polimi.ingsw.Network.Messages.toClient.PlanningPhase.UsedAssistantMessage;

public class MessageTask implements Runnable {

    private final Object message;
    private ServerHandler serverHandler;
    private Phase currPhase;

    MessageTask(Object message, ServerHandler serverHandler, Phase currPhase) {
        this.message = message;
        this.serverHandler = serverHandler;
        this.currPhase = currPhase;
    }

    @Override
    public void run() {
        processMessage();
    }

    private void processMessage() {
        try {
            if (message instanceof NickResponseMessage) {
                if (currPhase.equals(Phase.PickingNickname)) {
                    ((NickResponseMessage) message).process(this.serverHandler);
                }
            } else if (message instanceof SendMatchesMessage) {
                if (currPhase.equals(Phase.ChoosingGameMode) || currPhase.equals(Phase.JoiningGame1)) {
                    ((SendMatchesMessage) message).process(this.serverHandler);
                }
            } else if (message instanceof ConfirmJoiningMessage) {
                if (currPhase.equals(Phase.CreatingGame) ||
                        currPhase.equals(Phase.JoiningGame1) ||
                        currPhase.equals(Phase.JoiningGame2)) {
                    ((ConfirmJoiningMessage) message).process(this.serverHandler);
                }
            } else if (message instanceof SendAvailableWizardsMessage) {
                if (currPhase.equals(Phase.JoiningGame1) ||
                        currPhase.equals(Phase.JoiningGame2)) {
                    ((SendAvailableWizardsMessage) message).process(this.serverHandler);
                }
            } else if (message instanceof ChangeTurnMessage) {
                ((ChangeTurnMessage) message).process(this.serverHandler);
            } else if (message instanceof ConfirmMovementFromEntranceMessage) {
                ((ConfirmMovementFromEntranceMessage) message).process(this.serverHandler);
            } else if (message instanceof MoveMothernatureMessage) {
                ((MoveMothernatureMessage) message).process(this.serverHandler);
            } else if (message instanceof MoveProfessorMessage) {
                ((MoveProfessorMessage) message).process(this.serverHandler);
            } else if (message instanceof DenyMovementMessage) {
                ((DenyMovementMessage) message).process(this.serverHandler);
            } else if (message instanceof ConfirmMovementMessage) {
                ((ConfirmMovementMessage) message).process(this.serverHandler);
            } else if (message instanceof ConfirmCloudMessage) {
                ((ConfirmCloudMessage) message).process(this.serverHandler);
            } else if(message instanceof EndMessage) {
                ((EndMessage) message).process(this.serverHandler);
            } else if (message instanceof GameModelUpdateMessage) {
                ((GameModelUpdateMessage) message).process(this.serverHandler);
            } else if (message instanceof CloudsUpdateMessage) {
                ((CloudsUpdateMessage) message).process(this.serverHandler);
            } else if (message instanceof UsedAssistantMessage) {
                ((UsedAssistantMessage) message).process(this.serverHandler);
            } else if (message instanceof CharacterUsedMessage) {
                ((CharacterUsedMessage) message).process(this.serverHandler);
            } else if (message instanceof CharacterEntranceSwappedMessage){
                ((CharacterEntranceSwappedMessage) message).process(this.serverHandler);
            } else if (message instanceof EntranceTableSwappedMessage){
                ((EntranceTableSwappedMessage) message).process(this.serverHandler);
            } else if (message instanceof IslandChosenMessage){
                ((IslandChosenMessage) message).process(this.serverHandler);
            } else if (message instanceof NoEntryMovedMessage){
                ((NoEntryMovedMessage) message).process(this.serverHandler);
            } else if (message instanceof StudentColorChosenMessage){
                ((StudentColorChosenMessage) message).process(this.serverHandler);
            } else if (message instanceof StudentMovedFromCharacterMessage){
                ((StudentMovedFromCharacterMessage) message).process(this.serverHandler);
            } else if (message instanceof StudentMovedToTableMessage){
                ((StudentMovedToTableMessage) message).process(this.serverHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    public Phase getCurrPhase() {
        return currPhase;
    }

    public void setCurrPhase(Phase currPhase) {
        this.currPhase = currPhase;
    }

}
