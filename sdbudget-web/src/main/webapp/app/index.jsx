// Compile: ./node_modules/.bin/webpack -d

import React from 'react';
import {render}
from 'react-dom';
class App extends React.Component {
    constructor() {
        super();
        this.state = {
            data:
                    [
                        {
                            type: 'Revenue',
                            name: 'Salary 1',
                            amount: 2000.00,
                            id: 1
                        },
                        {
                            type: 'Revenue',
                            name: 'Salary 2',
                            amount: 2000.00,
                            id: 2
                        },
                        {
                            type: 'Expense',
                            name: 'Rent',
                            amount: 950.00,
                            id: 1
                        }
                    ]
        };
    }

    render() {
        return (
                <div>
                    <MonthTable budgetItems = {this.state.data}/>
                </div>
                );
    }
}

class MonthTable extends React.Component {
    constructor(props) {
        super(props);
        this.addBudgetItem = this.addBudgetItem.bind(this);
        this.state = {
            budgetItems: this.props.budgetItems
        }

        this.addBudgetItem = this.addBudgetItem.bind(this);
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
                    <button onClick={this.addBudgetItem}>Add BudgetItem</button>
                </div>
                );
    }

    addBudgetItem() {
        var nextState = this.state;
        nextState.budgetItems.push({
            type: "Expense",
            name: "Food",
            amount: 250.00
        });
        this.setState(nextState);
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
