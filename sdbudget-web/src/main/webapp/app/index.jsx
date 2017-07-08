// Compile: ./node_modules/.bin/webpack -d

import React from 'react';
import {render} from 'react-dom';

class App extends React.Component {
    constructor() {
        super();
        this.state = {};

        this.onResponse = this.onResponse.bind(this);
    }

    render() {
        return (
                <div>
                    <MonthTable ref = {(ref) => {
                            this.monthTable = ref
                                }}/>
                    <hr/>
                    <WebSocketControls onResponse={this.onResponse}/>
                </div>
                    );
    }

    onResponse(response) {
        // TODO
        // Update BudgetItem list appropriately
        var month = JSON.parse(response.body);
        var component = this;
        month.revenues.forEach(function (v, i) {
            component.monthTable.addBudgetItem("Revenue", v.name, v.amount);
        });
        month.expenses.forEach(function (v, i) {
            component.monthTable.addBudgetItem("Expense", v.name, v.amount);
        });
        month.adjustments.forEach(function (v, i) {
            component.monthTable.addBudgetItem("Adjustment", v.name, v.amount);
        });
        component.monthTable.addBudgetItem("Debt Repayments", month.debtRepayments.name, month.debtRepayments.amount);
        component.monthTable.addBudgetItem("Investment Outflows", month.investmentOutflows.name, month.investmentOutflows.amount);
        component.monthTable.addBudgetItem("Net Income Target", month.netIncomeTarget.name, month.netIncomeTarget.amount);
        component.monthTable.addBudgetItem("Opening Balance", month.openingBalance.name, month.openingBalance.amount);
        component.monthTable.addBudgetItem("Opening Surplus", month.openingSurplus.name, month.openingSurplus.amount);
        component.monthTable.addBudgetItem("Closing Surplus", month.closingSurplus.name, month.closingSurplus.amount);
        component.monthTable.addBudgetItem("Closing Balance Target", month.closingBalanceTarget.name, month.closingBalanceTarget.amount);
        component.monthTable.addBudgetItem("Estimated Closing Balance", month.estimatedClosingBalance.name, month.estimatedClosingBalance.amount);
        component.monthTable.addBudgetItem("Closing Balance", month.closingBalance.name, month.closingBalance.amount);

        component.monthTable.setState({
            isClosed: true
        });
    }
}

class WebSocketControls extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            stompClient: null,
            responses: []
        }

        this.handleConnect = this.handleConnect.bind(this);
        this.handleDisconnect = this.handleDisconnect.bind(this);
    }

    handleConnect(e) {
        var socket = new SockJS('/gs-guide-websocket');
        var state = this.state;
        var component = this;
        state.stompClient = Stomp.over(socket);
        state.stompClient.connect({}, function (frame) {
            state.stompClient.subscribe('/topic/example', function (response) {
                var nextState = state;
                nextState.responses.push(response.body);
                component.setState(nextState);

                // Propogate the event
                if (component.props.onResponse != undefined) {
                    component.props.onResponse(response);
                }
//            showResponse(JSON.parse(response.body).content);
            });
            state.stompClient.send("/app/get-month", {}, JSON.stringify({}));
        });
    }

    handleDisconnect(e) {
        var state = this.state;
        if (state.stompClient != null) {
            state.stompClient.disconnect();
        }
    }

    render() {
        return (
                <div>
                    <div>
                        <button onClick={this.handleConnect}>Connect</button>
                        <button onClick={this.handleDisconnect}>Disconnect</button>
                    </div>
                    <div id="responses">
                        {this.state.responses.map((response, i) =>
                                        <div key={i} >
                                            {response}
                                        </div>
                                    )}                
                    </div>
                </div>
                )
    }
}

class MonthTable extends React.Component {
    constructor(props) {
        super(props);
        this.addBudgetItem = this.addBudgetItem.bind(this);

        this.state = {
            budgetItems: [],
            isClosed: false
        }

        this.addBudgetItem = this.addBudgetItem.bind(this);
        this.addBudgetItemButtonHandler = this.addBudgetItemButtonHandler.bind(this);
    }

    render() {
        return (
                <div>
                    <table>
                        <thead>
                            <tr>
                                <th>Type</th>
                                <th>Name</th>
                                <th>Amount</th>
                            </tr>
                        </thead>
                        <tbody>
                            {this.state.budgetItems.map((budgetItem, i) =>
                                        <MonthTableEntry key={i} type={budgetItem.type} name={budgetItem.name} amount={budgetItem.amount}/>
                                    )}                
                        </tbody>
                    </table>
                    <label>
                        Close Month
                        <input type="checkbox" name="isClosed" checked={this.state.isClosed}/>
                    </label>
                    <button onClick={this.addBudgetItemButtonHandler}>Add BudgetItem</button>
                </div>
                );
    }

    addBudgetItem(type, name, amount) {
        var nextState = this.state;
        nextState.budgetItems.push({
            type: type,
            name: name,
            amount: amount
        });
        this.setState(nextState);
    }

    addBudgetItemButtonHandler() {
        this.addBudgetItem("", "", 0.00);
    }
}

class MonthTableEntry extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            data: props
        }

        this.updateState = this.updateState.bind(this);
    }

    updateState(e) {
        this.setState({data: e.target.value})
    }

    render() {
        return (
                <tr>
                    <td><input type="text" value={this.state.data.type} onChange={this.updateState}/></td>
                    <td><input type="text" value={this.state.data.name} onChange={this.updateState}/></td>
                    <td><input type="text" value={this.state.data.amount} onChange={this.updateState}/></td>
                </tr>
                )
    }
}

render(<App/>, document.getElementById('app'));


/*
 * Month: int
 * Type: "revenue", "expense", "adjustment"
 */
function addBugetItem(month, name, amount, type) {
    stompClient.send("/app/add-budget-item", {}, JSON.stringify({
        month: month,
        name: name,
        amount: amount,
        type: type
    }));
}

function updateBudgetItem(id, name, amount) {
    stompClient.send("/app/update-budget-item", {}, JSON.stringify({
        id: id,
        name: name,
        amount: amount
    }));
}

function removeBudgetItem(id) {
    stompClient.send("/app/remove-budget-item", {}, JSON.stringify({
        id: id
    }));
}