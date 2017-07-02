package org.olestourko;

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
}
