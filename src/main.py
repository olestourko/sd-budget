from __future__ import print_function

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

if __name__ == "__main__":
	pass
