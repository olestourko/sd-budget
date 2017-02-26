#!/usr/bin/env python
import curses
import blinker

class Frontend:

	def __init__(self, data_provider_function):
		""" data_provider_function must return a dict with values for all UI fields """
		self.data_provider = data_provider_function

	def draw_screen(self, screen, erase=True):
		if erase:
			screen.erase()

		screen.addstr(0, 0, "[R] Revenue", curses.A_NORMAL)
		screen.addstr(0, 30, str(self.data_provider()['revenue']), curses.A_NORMAL)
		screen.addstr(1, 0, "[E] Expenses", curses.A_NORMAL)
		screen.addstr(1, 30, str(self.data_provider()['expenses']), curses.A_NORMAL)
		screen.addstr(2, 0, "[A] Adjustments", curses.A_NORMAL)
		screen.addstr(2, 30, str(self.data_provider()['adjustments']), curses.A_NORMAL)
		screen.addstr(4, 0, "[N] Net Income Target", curses.A_NORMAL)
		screen.addstr(4, 30, str(self.data_provider()['income_target']), curses.A_NORMAL)
		screen.addstr(5, 0, "[L] Last Account Balance", curses.A_NORMAL)
		screen.addstr(5, 30, str(self.data_provider()['last_balance']), curses.A_NORMAL)
		screen.addstr(7, 0, "[X] Exit", curses.A_REVERSE)
		screen.addstr(screen.getmaxyx()[0]-1, 0, "Command:", curses.A_REVERSE)
		screen.refresh()

	def run(self):

		def run_wrapped(screen):

			while True:
				self.draw_screen(screen)
				ch = chr(screen.getch())
				maxyx = screen.getmaxyx()
				screen.addch(maxyx[0]-1, 9, ch)
				screen.refresh()

				if ch == 'R':
					curses.echo()
					item = "Revenue"
					screen.addstr(maxyx[0]-1, 0, "Enter {}:".format(item), curses.A_REVERSE)
					value = screen.getstr(maxyx[0]-1, len(item) + 8)
					curses.noecho()
					blinker.signal("revenue set").send(self, value=value)
				elif ch == 'E':
					curses.echo()
					item = "Expenses"
					screen.addstr(maxyx[0]-1, 0, "Enter {}:".format(item), curses.A_REVERSE)
					value = screen.getstr(maxyx[0]-1, len(item) + 8)
					curses.noecho()
					blinker.signal("expenses set").send(self, value=value)
				elif ch == 'A':
					curses.echo()
					item = "Adjustments"
					screen.addstr(maxyx[0]-1, 0, "Enter {}:".format(item), curses.A_REVERSE)
					value = screen.getstr(maxyx[0]-1, len(item) + 8)
					curses.noecho()
					blinker.signal("adjustments set").send(self, value=value)
				elif ch == 'N':
					curses.echo()
					item = "Net Income Target"
					screen.addstr(maxyx[0]-1, 0, "Enter {}:".format(item), curses.A_REVERSE)
					value = screen.getstr(maxyx[0]-1, len(item) + 8)
					curses.noecho()
					blinker.signal("income target set").send(self, value=value)
				elif ch == 'L':
					curses.echo()
					item = "Last Account Balance"
					screen.addstr(maxyx[0]-1, 0, "Enter {}:".format(item), curses.A_REVERSE)
					value = screen.getstr(maxyx[0]-1, len(item) + 8)
					curses.noecho()
					blinker.signal("last balance set").send(self, value=value)
				elif ch == 'X':
					break
				else:
					break

			curses.endwin()
		
		curses.wrapper(run_wrapped)

if __name__ == "__main__":
	frontend = Frontend()
	frontend.run()
