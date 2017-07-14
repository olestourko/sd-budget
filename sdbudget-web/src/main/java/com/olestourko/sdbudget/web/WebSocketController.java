package com.olestourko.sdbudget.web;

import com.olestourko.sdbudget.core.models.BudgetItem;
import com.olestourko.sdbudget.core.models.Month;
import com.olestourko.sdbudget.core.repositories.MonthRepository;
import com.olestourko.sdbudget.web.websocket.AddBudgetItemMessage;
import com.olestourko.sdbudget.web.websocket.AddBudgetItemResponse;
import com.olestourko.sdbudget.web.websocket.RemoveBudgetItemMessage;
import com.olestourko.sdbudget.web.websocket.RemoveBudgetItemResponse;
import com.olestourko.sdbudget.web.websocket.Response;
import com.olestourko.sdbudget.web.websocket.UpdateBudgetItemMessage;
import com.olestourko.sdbudget.web.websocket.UpdateBudgetItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

/**
 *
 * @author oles
 */
@Controller
public class WebSocketController {

    @Autowired
    private MonthRepository monthRepository;

    @MessageMapping("/get-month")
    @SendTo("/topic/get-month")
    public Month getMonth(String message) throws Exception {
        return monthRepository.getFirst();
    }

    @MessageMapping("/add-budget-item")
    @SendTo("/topic/add-budget-item")
    public AddBudgetItemResponse addBudgetItem(AddBudgetItemMessage message) throws Exception {
        BudgetItem budgetItem = new BudgetItem();
        budgetItem.setName(message.getName());
        budgetItem.setAmount(message.getAmount());
        switch (message.getType()) {
            case "Revenue":
                monthRepository.getFirst().getRevenues().add(budgetItem);
                break;
            case "Expense":
                monthRepository.getFirst().getExpenses().add(budgetItem);
                break;
            case "Adjustment":
                monthRepository.getFirst().getAdjustments().add(budgetItem);
                break;
        }

        return new AddBudgetItemResponse(Response.Status.SUCCESS, 0);
    }

    @MessageMapping("/update-budget-item")
    @SendTo("/topic/update-budget-item")
    public UpdateBudgetItemResponse updateBudgetItem(UpdateBudgetItemMessage message) throws Exception {
        return new UpdateBudgetItemResponse(Response.Status.SUCCESS);
    }

    @MessageMapping("/remove-budget-item")
    @SendTo("/topic/remove-budget-item")
    public RemoveBudgetItemResponse removeBudgetItem(RemoveBudgetItemMessage message) throws Exception {
        return new RemoveBudgetItemResponse(Response.Status.SUCCESS);
    }
}
