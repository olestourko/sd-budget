import core_functions
import datetime

class Backend:
    def __init__(self):
        self.months = {}

    def set_month(self, number, year, month):
        self.months[(number, year)] = month

    def get_month(self, number, year):
        return self.months[(number, year)]

    def set_current_month(self, month):
        today = datetime.date.today()
        self.months[(today.month, today.year)] = month

    def get_current_month(self):
        today = datetime.date.today()
        return self.months[(today.month, today.year)]

    def get_next_month(self, month):
        pass

    def get_previous_month(self, month):
        pass

    def calculate_month_estimate(self, month):
        return core_functions.calculate_period_estimate(month.revenues, month.expenses, month.adjustments,
                                                        month.income_target,
                                                        month.opening_balance)

    def calculate_month_closing(self, month, closing_balance):
        return core_functions.calculate_period_closing(month.income_target, month.opening_balance, closing_balance)
