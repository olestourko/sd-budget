#!/usr/bin/env python
import curses
import blinker
from views.period import Period
from views.scratch_pad import ScratchPad

class Frontend:
    calculate_month_closing = None

    def __init__(self, month, calculate_month_closing_function):
        self.month = month
        self.calculate_month_closing = calculate_month_closing_function
        self.current_view = None

    def read_int(self, label):
        maxyx = self.screen.getmaxyx()
        self.screen.addstr(maxyx[0] - 1, 0, "Enter {}:".format(label), curses.A_REVERSE)
        value = self.screen.getstr(maxyx[0] - 1, len(label) + 8)
        return int(value)

    def draw(self):
        # Renders UI that should be visible in every view
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

    def run(self):
        def run_wrapped(screen):
            self.screen = screen
            # Views
            self.period_view = Period(self.screen, self.month, self.calculate_month_closing)
            self.scratch_pad_view = ScratchPad(self.screen)
            self.current_view = self.period_view

            while True:
                self.current_view.draw(erase=True)
                self.draw()
                ch = chr(screen.getch())
                screen.refresh()

                if self.current_view.handle_input(ch):
                    pass
                elif ch == 'M':
                    self.toggle_view()
                elif ch == 'X':
                    break

                blinker.signal("ui updated").send(self)

            curses.endwin()

        curses.wrapper(run_wrapped)


if __name__ == "__main__":
    frontend = Frontend()
    frontend.run()
