#!/usr/bin/env python
import curses
import blinker
import logging
import datetime
from dateutil.relativedelta import relativedelta
from views.period import Period
from views.scratch_pad import ScratchPad

class Frontend:
    calculate_month_estimate = None

    def __init__(self, backend):
        self.backend = backend
        self.current_view = None
        self.month = backend.get_current_month()['month']
        self.date = datetime.date(backend.get_current_month()['year'], backend.get_current_month()['number'], 1)

    def read_int(self, label):
        maxyx = self.screen.getmaxyx()
        self.screen.addstr(maxyx[0] - 1, 0, "Enter {}:".format(label), curses.A_REVERSE)
        value = self.screen.getstr(maxyx[0] - 1, len(label) + 8)
        return int(value)

    def draw(self):
        # Renders UI that should be visible in every view
        maxyx = self.screen.getmaxyx()

        self.screen.addstr(maxyx[0] - 3, maxyx[1] - 16, self.date.strftime("%m-%Y"), curses.A_REVERSE)

        label = "[P] Previous Month"
        self.screen.addstr(maxyx[0] - 2, maxyx[1] - 20, label, curses.A_REVERSE)
        self.screen.chgat(maxyx[0] - 2, maxyx[1] - 20, -1, curses.A_REVERSE)
        label = "[N] Next Month"
        self.screen.addstr(maxyx[0] - 1, maxyx[1] - 20, label, curses.A_REVERSE)
        self.screen.chgat(maxyx[0] - 1, maxyx[1] - 20, -1, curses.A_REVERSE)

        if self.current_view == self.period_view:
            self.screen.addstr(self.screen.getmaxyx()[0] - 3, 0, "[M] Scratchpad", curses.A_NORMAL)
        else:
            self.screen.addstr(self.screen.getmaxyx()[0] - 3, 0, "[M] Period", curses.A_NORMAL)

        self.screen.addstr(self.screen.getmaxyx()[0] - 2, 0, "[X] Exit", curses.A_NORMAL)
        self.screen.addstr(self.screen.getmaxyx()[0] - 1, 0, "Command:", curses.A_REVERSE)
        self.screen.refresh()

    def toggle_view(self):
        if self.current_view == self.period_view:
            self.current_view = self.scratch_pad_view
        else:
            self.current_view = self.period_view

    def next_month(self):
        self.date = self.date + relativedelta(months=+1)
        self.month = self.backend.get_month(self.date.month, self.date.year)
        self.period_view.month = self.month
        self.scratch_pad_view.month = self.month

    def previous_month(self):
        self.date = self.date + relativedelta(months=-1)
        self.month = self.backend.get_month(self.date.month, self.date.year)
        self.period_view.month = self.month
        self.scratch_pad_view.month = self.month

    def run(self):
        def run_wrapped(screen):
            self.screen = screen
            # Views
            self.period_view = Period(
                self.screen,
                self.month,
                self.backend
            )
            self.scratch_pad_view = ScratchPad(self.screen, self.month)
            self.current_view = self.period_view

            while True:
                self.current_view.draw(erase=True)
                self.draw()
                input = chr(screen.getch())
                screen.refresh()

                if self.current_view.handle_input(input):
                    pass
                elif input == 'M':
                    self.toggle_view()
                elif input == 'P':
                    self.previous_month()
                elif input == 'N':
                    self.next_month()
                elif input == 'X':
                    break

                blinker.signal("ui updated").send(self)

            curses.endwin()

        curses.wrapper(run_wrapped)


if __name__ == "__main__":
    frontend = Frontend()
    frontend.run()
