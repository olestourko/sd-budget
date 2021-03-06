class Month():
    def __init__(self, revenues=0, expenses=0, opening_balance=0, adjustments=0, income_target=0):
        self.revenues = revenues
        self.expenses = expenses
        self.opening_balance = opening_balance
        self.adjustments = adjustments
        self.income_target = income_target
        self.transactions = [] # Used by the scratchpad

    def get_transactions_total(self):
        return sum([transaction['amount'] for transaction in self.transactions])