import curses
from view import View


class Period(View):
    def __init__(self, screen, month, calculate_month_closing_function):
        self.screen = screen
        self.month = month
        self.calculate_month_closing = calculate_month_closing_function

    def draw(self, erase=True):
        View.draw(self, erase)

        closing_values = self.calculate_month_closing(self.month)
        self.screen.addstr(0, 0, "[1] Revenues", curses.A_NORMAL)
        self.screen.addstr(0, 30, str(self.month.revenues), curses.A_NORMAL)
        self.screen.addstr(1, 0, "[2] Expenses", curses.A_NORMAL)
        self.screen.addstr(1, 30, str(self.month.expenses), curses.A_NORMAL)
        self.screen.addstr(2, 0, "[3] Adjustments", curses.A_NORMAL)
        self.screen.addstr(2, 30, str(self.month.adjustments), curses.A_NORMAL)
        self.screen.addstr(4, 0, "[4] Net Income Target", curses.A_NORMAL)
        self.screen.addstr(4, 30, str(self.month.income_target), curses.A_NORMAL)
        self.screen.addstr(5, 0, "[5] Opening Account Balance", curses.A_NORMAL)
        self.screen.addstr(5, 30, str(self.month.opening_balance), curses.A_NORMAL)
        self.screen.addstr(6, 4, "Closing Account Balance", curses.A_NORMAL)
        self.screen.addstr(6, 30, str(closing_values['new_balance']), curses.A_REVERSE)
        self.screen.addstr(7, 4, "Surplus (Defecit)", curses.A_NORMAL)
        self.screen.addstr(7, 30, str(closing_values['surplus']), curses.A_REVERSE)

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

        elif input == '3':
            curses.echo()
            label = "Adjustments"
            self.month.adjustments = self.read_int(label)
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
            return False
