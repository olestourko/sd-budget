def calculate_period_estimate(revenue=None, expenses=None, adjustments=None, income_target=None, opening=None):
    """
    Returns the net income, new account balance, and surplus or defecit for an arbitrary time period.
    """
    net_income = revenue - expenses + adjustments
    new_balance = opening + net_income
    expected_balance = opening + income_target
    surplus = new_balance - expected_balance

    return {
        'net_income': net_income,
        'new_balance': new_balance,
        'surplus': surplus
    }


def calculate_period_closing(income_target=None, opening_balance=None, closing_balance=None):
    closing_balance_target = opening_balance + income_target
    closing_surplus = closing_balance - closing_balance_target #TODO: Carry over surplus from the previous month
    closing_adjustment = closing_surplus

    return {
        'closing_adjustment': closing_adjustment,
        'closing_surplus': closing_surplus
    }
