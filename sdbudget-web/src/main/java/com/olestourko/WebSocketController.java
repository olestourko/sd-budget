package com.olestourko;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 *
 * @author oles
 */
@Controller
public class WebSocketController {

    @MessageMapping("/example")
    @SendTo("/topic/example")
    public ExampleWebSocketResponse greeting(ExampleWebSocketMessage message) throws Exception {
        return new ExampleWebSocketResponse("(Response) " + message.getContent());
    }
    
    @MessageMapping("/add-budget-item")
    @SendTo("/topic/example")
    public String addBudgetItem(AddBudgetItemMessage message) throws Exception {
        return "Ok";
    }
}
