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
    render() {
        return (
                <table>
                    <thead>
                        <tr>
                            <th>Type</th>
                            <th>Name</th>
                            <th>Amount</th>
                        </tr>
                    </thead>
                    <tbody>
                        {this.props.budgetItems.map((budgetItem, i) =>
                                        <tr key =  {i}>
                                            <td>{budgetItem.type}</td>
                                            <td>{budgetItem.name}</td>
                                            <td>{budgetItem.amount}</td>
                                        </tr>
                                    )}                
                    </tbody>
                </table>
                );
    }
}

render(<App/>, document.getElementById('app'));
