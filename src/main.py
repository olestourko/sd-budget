from __future__ import print_function

import logging
import blinker

from backend.month import Month
from frontend_curses.frontend import Frontend

def calculate_period(revenue=None, expenses=None, adjustments=None, income_target=None, last_balance=None):
	"""
	Returns the net income, new account balance, and surplus or defecit for an arbitrary time period. 
	"""
	net_income = revenue - expenses + adjustments
	new_balance = last_balance + net_income
	expected_balance = last_balance + income_target
	surplus = new_balance - expected_balance 

	return {
			'net_income': net_income,
			'new_balance': new_balance,
			'surplus': surplus
	}

def calculate_month_closing(month):
	return calculate_period(month.revenues, month.expenses, month.adjustments, month.income_target, month.opening_balance)

if __name__ == "__main__":
	# Listen to events
	logging.basicConfig(filename='output.log', level=logging.DEBUG)

	month = Month(revenues=2000, expenses=1000, adjustments=0, income_target=500, opening_balance=0)
	frontend = Frontend(month, calculate_month_closing)
	frontend.run()

