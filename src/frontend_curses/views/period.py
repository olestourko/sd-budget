import curses
from view import View


class Period(View):
    def __init__(self, screen, month, backend):
        self.screen = screen
        self.month = month
        self.backend = backend
        self.closing_balance = None

    def draw(self, erase=True):
        View.draw(self, erase)

        closing_values = self.backend.calculate_month_estimate(self.month)
        self.screen.addstr(0, 0, "[1] Revenues", curses.A_NORMAL)
        self.screen.addstr(0, 40, str(self.month.revenues), curses.A_NORMAL)
        self.screen.addstr(1, 0, "[2] Expenses", curses.A_NORMAL)
        self.screen.addstr(1, 40, str(self.month.expenses), curses.A_NORMAL)

        if self.closing_balance is not None:
            closing_adjustment = self.backend.calculate_month_closing(self.month, self.closing_balance)["closing_adjustment"]
            self.screen.addstr(2, 4, "Adjustments", curses.A_NORMAL)
            self.screen.addstr(2, 40, str(closing_adjustment), curses.A_BOLD)
        else:
            self.screen.addstr(2, 4, "Adjustments", curses.A_NORMAL)
            self.screen.addstr(2, 40, str(self.month.get_transactions_total()), curses.A_NORMAL)

        self.screen.addstr(4, 0, "[4] Net Income Target", curses.A_NORMAL)
        self.screen.addstr(4, 40, str(self.month.income_target), curses.A_NORMAL)
        self.screen.addstr(5, 0, "[5] Opening Account Balance", curses.A_NORMAL)
        self.screen.addstr(5, 40, str(self.month.opening_balance), curses.A_NORMAL)

        expected_closing_balance = self.month.opening_balance + self.month.income_target
        self.screen.addstr(6, 4, "Closing Balance Target", curses.A_NORMAL)
        self.screen.addstr(6, 40, str(expected_closing_balance), curses.A_BOLD)

        self.screen.addstr(8, 4, "Closing Account Balance (Estimated)", curses.A_NORMAL)
        self.screen.addstr(8, 40, str(closing_values['new_balance']), curses.A_BOLD)
        self.screen.addstr(9, 4, "Surplus or Defecit (Estimated)", curses.A_NORMAL)
        self.screen.addstr(9, 40, str(closing_values['surplus']), curses.A_BOLD)

        self.screen.addstr(11, 0, "[6] Closing Balance (Actual)", curses.A_NORMAL)
        if self.closing_balance is not None:
            self.screen.addstr(11, 40, str(self.closing_balance), curses.A_NORMAL)
        else:
            self.screen.addstr(11, 40, "", curses.A_NORMAL)

        self.screen.addstr(12, 4, "Surplus or Defecit (Actual)", curses.A_NORMAL)
        if self.closing_balance is not None:
            closing_surplus = self.backend.calculate_month_closing(self.month, self.closing_balance)["closing_surplus"]
            self.screen.addstr(12, 40, str(closing_surplus), curses.A_BOLD)
        else:
            self.screen.addstr(12, 40, "", curses.A_BOLD)


    def handle_input(self, input):
        # Returns True if input matched and handled, False otherwise
        if input == '1':
            curses.echo()
            label = "Revenues"
            self.month.revenues = self.read_int(label)
            curses.noecho()
            return True

        elif input == '2':
            curses.echo()
            label = "Expenses"
            self.month.expenses = self.read_int(label)
            curses.noecho()
            return True

        elif input == '4':
            curses.echo()
            label = "Net Income Target"
            self.month.income_target = self.read_int(label)
            curses.noecho()
            return True

        elif input == '5':
            curses.echo()
            label = "Opening Account Balance"
            self.month.opening_balance = self.read_int(label)
            curses.noecho()
            return True

        elif input == '6':
            curses.echo()
            value = self.read_string("This will close out the period. Continue? (Y/N)")

            if value == "Y":
                self.screen.deleteln()
                value = self.read_int("Closing Balance")
                self.closing_balance = value

            curses.noecho()
            return True

        return False