from __future__ import print_function

import logging
import blinker

from frontend.frontend import Frontend

revenue = 0
expenses = 0
adjustments = 0
income_target = 0
last_balance = 0

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

def set_revenue_handler(sender, value):
	global revenue
	revenue = value
	logging.debug('revenue set: %s', value)

def set_expense_handler(sender, value):
	global expenses
	expenses = value

def set_adjustments_handler(sender, value):
	global adjustments
	adjustments = value

def set_income_target_handler(sender, value):
	global income_target
	income_target = value

def set_last_balance_handler(sender, value):
	global last_balance
	last_balance = value

def get_period_data():
	return {
		'revenue': revenue,
		'expenses': expenses,
		'adjustments': adjustments,
		'income_target': income_target,
		'last_balance': last_balance
	}

if __name__ == "__main__":
	# Listen to events
	logging.basicConfig(filename='output.log', level=logging.DEBUG)
	blinker.signal("revenue set").connect(set_revenue_handler)
	blinker.signal("expenses set").connect(set_expense_handler)
	blinker.signal("adjustments set").connect(set_adjustments_handler)
	blinker.signal("income target set").connect(set_income_target_handler)
	blinker.signal("last balance set").connect(set_last_balance_handler)

	frontend = Frontend(get_period_data)
	frontend.run()


