#!/usr/bin/env python
import curses
import blinker


class Frontend:
    calculate_month_closing = None

    def __init__(self,
                 month,
                 calculate_month_closing_function
                 ):
        self.month = month
        self.calculate_month_closing = calculate_month_closing_function

    def draw_screen(self, erase=True):
        if erase:
            self.screen.erase()

        closing_values = self.calculate_month_closing(self.month)
        self.screen.addstr(0, 0, "[R] Revenues", curses.A_NORMAL)
        self.screen.addstr(0, 30, str(self.month.revenues), curses.A_NORMAL)
        self.screen.addstr(1, 0, "[E] Expenses", curses.A_NORMAL)
        self.screen.addstr(1, 30, str(self.month.expenses), curses.A_NORMAL)
        self.screen.addstr(2, 0, "[A] Adjustments", curses.A_NORMAL)
        self.screen.addstr(2, 30, str(self.month.adjustments), curses.A_NORMAL)
        self.screen.addstr(4, 0, "[N] Net Income Target", curses.A_NORMAL)
        self.screen.addstr(4, 30, str(self.month.income_target), curses.A_NORMAL)
        self.screen.addstr(5, 0, "[L] Opening Account Balance", curses.A_NORMAL)
        self.screen.addstr(5, 30, str(self.month.opening_balance), curses.A_NORMAL)
        self.screen.addstr(6, 4, "Closing Account Balance", curses.A_NORMAL)
        self.screen.addstr(6, 30, str(closing_values['new_balance']), curses.A_REVERSE)
        self.screen.addstr(7, 4, "Surplus (Defecit)", curses.A_NORMAL)
        self.screen.addstr(7, 30, str(closing_values['surplus']), curses.A_REVERSE)
        self.screen.addstr(9, 0, "[X] Exit", curses.A_NORMAL)
        self.screen.addstr(self.screen.getmaxyx()[0] - 1, 0, "Command:", curses.A_REVERSE)
        self.screen.refresh()

    def read_int(self, label):
        maxyx = self.screen.getmaxyx()
        self.screen.addstr(maxyx[0] - 1, 0, "Enter {}:".format(label), curses.A_REVERSE)
        value = self.screen.getstr(maxyx[0] - 1, len(label) + 8)
        return int(value)

    def run(self):

        def run_wrapped(screen):
            self.screen = screen

            while True:
                self.draw_screen()
                ch = chr(screen.getch())
                screen.refresh()

                if ch == 'R':
                    curses.echo()
                    label = "Revenues"
                    self.month.revenues = self.read_int(label)
                    curses.noecho()
                elif ch == 'E':
                    curses.echo()
                    label = "Expenses"
                    self.month.expenses = self.read_int(label)
                    curses.noecho()
                elif ch == 'A':
                    curses.echo()
                    label = "Adjustments"
                    self.month.adjustments = self.read_int(label)
                    curses.noecho()
                elif ch == 'N':
                    curses.echo()
                    label = "Net Income Target"
                    self.month.income_target = self.read_int(label)
                    curses.noecho()
                elif ch == 'L':
                    curses.echo()
                    label = "Opening Account Balance"
                    self.month.opening_balance = self.read_int(label)
                    curses.noecho()
                elif ch == 'X':
                    break
                else:
                    break

                blinker.signal("ui updated").send(self)

            curses.endwin()

        curses.wrapper(run_wrapped)


if __name__ == "__main__":
    frontend = Frontend()
    frontend.run()
