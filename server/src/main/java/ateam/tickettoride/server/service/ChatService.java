package ateam.tickettoride.server.service;

import ateam.tickettoride.common.requests.ChatRequest;
import ateam.tickettoride.common.responses.Response;
import ateam.tickettoride.server.model.ServerModelFacade;

public class ChatService {
    public Response performService(ChatRequest request) {
        System.out.println("Sending chat message...");

        ServerModelFacade serverModelFacade = ServerModelFacade.getInstance();

        return serverModelFacade.sendChatMessage(request.getAuthToken(), request.getGameID(), request.getMessage());
    }
}
