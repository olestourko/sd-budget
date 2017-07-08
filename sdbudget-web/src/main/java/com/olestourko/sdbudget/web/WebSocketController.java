package com.olestourko.sdbudget.web;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.web.websocket.AddBudgetItemMessage;
import com.olestourko.sdbudget.web.websocket.AddBudgetItemResponse;
import com.olestourko.sdbudget.web.websocket.RemoveBudgetItemMessage;
import com.olestourko.sdbudget.web.websocket.RemoveBudgetItemResponse;
import com.olestourko.sdbudget.web.websocket.Response;
import com.olestourko.sdbudget.web.websocket.UpdateBudgetItemMessage;
import com.olestourko.sdbudget.web.websocket.UpdateBudgetItemResponse;
import java.math.BigDecimal;
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

    @MessageMapping("/get-month")
    @SendTo("/topic/example")
    public Month getMonth(String message) throws Exception {
        Month month = new Month();
        month.setNumber((short)7);
        month.setYear((short)2017);
        month.getRevenues().add(new BudgetItem("Salary 1", new BigDecimal(2000.00)));
        month.getRevenues().add(new BudgetItem("Salary 2", new BigDecimal(2000.00)));
        month.getExpenses().add(new BudgetItem("Rent", new BigDecimal(800.00)));
        month.setIsClosed(true);
        return month;
    }

    @MessageMapping("/add-budget-item")
    @SendTo("/topic/example")
    public AddBudgetItemResponse addBudgetItem(AddBudgetItemMessage message) throws Exception {
        return new AddBudgetItemResponse(Response.Status.SUCCESS, 0);
    }

    @MessageMapping("/update-budget-item")
    @SendTo("/topic/example")
    public UpdateBudgetItemResponse updateBudgetItem(UpdateBudgetItemMessage message) throws Exception {
        return new UpdateBudgetItemResponse(Response.Status.SUCCESS);
    }

    @MessageMapping("/remove-budget-item")
    @SendTo("/topic/example")
    public RemoveBudgetItemResponse removeBudgetItem(RemoveBudgetItemMessage message) throws Exception {
        return new RemoveBudgetItemResponse(Response.Status.SUCCESS);
    }
}
