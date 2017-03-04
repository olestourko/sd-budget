import core_functions
import datetime
from month import Month


class Backend:
    def __init__(self):
        self.months = {}

    def set_month(self, number, year, month):
        self.months[(number, year)] = month

    def get_month(self, number, year, create_if_missing=True):
        if (number, year) in self.months:
            return self.months[(number, year)]
        elif create_if_missing:
            month = Month()
            self.set_month(number, year, month)
            return month
        else:
            return None

    def set_current_month(self, month):
        today = datetime.date.today()
        self.months[(today.month, today.year)] = month

    def get_current_month(self):
        today = datetime.date.today()
        return {
            'number': today.month,
            'year': today.year,
            'month': self.months[(today.month, today.year)]
        }

    def calculate_month_estimate(self, month):
        return core_functions.calculate_period_estimate(
            month.revenues,
            month.expenses,
            month.get_transactions_total(),
            month.income_target,
            month.opening_balance
        )

    def calculate_month_closing(self, month, closing_balance):
        return core_functions.calculate_period_closing(
            month.income_target,
            month.opening_balance,
            closing_balance
        )
