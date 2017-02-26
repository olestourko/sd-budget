#!/usr/bin/env python
import curses
import time

screen = None

def draw_screen(erase=True):
	global screen

	if screen is None:
		screen = curses.initscr()
		curses.noecho()
		curses.cbreak()
		curses.curs_set(0)

	if erase:
		screen.erase()

	screen.addstr(0, 0, "[R] Revenue", curses.A_NORMAL)
	screen.addstr(1, 0, "[E] Expenses", curses.A_NORMAL)
	screen.addstr(2, 0, "[A] Adjustments", curses.A_NORMAL)
	screen.addstr(4, 0, "[N] Net Income Target", curses.A_NORMAL)
	screen.addstr(5, 0, "[L] Last Account Balance", curses.A_NORMAL)
	screen.addstr(7, 0, "[X] Exit", curses.A_NORMAL)
	screen.addstr(screen.getmaxyx()[0]-1, 0, "Command:", curses.A_REVERSE)
	screen.refresh()

if __name__ == "__main__":
	draw_screen()

	while True:
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
		elif ch == 'E':
			curses.echo()
			item = "Expenses"
			screen.addstr(maxyx[0]-1, 0, "Enter {}:".format(item), curses.A_REVERSE)
			value = screen.getstr(maxyx[0]-1, len(item) + 8)
			curses.noecho()
		elif ch == 'A':
			curses.echo()
			item = "Adjustments"
			screen.addstr(maxyx[0]-1, 0, "Enter {}:".format(item), curses.A_REVERSE)
			value = screen.getstr(maxyx[0]-1, len(item) + 8)
			curses.noecho()
		elif ch == 'N':
			curses.echo()
			item = "Net Income Target"
			screen.addstr(maxyx[0]-1, 0, "Enter {}:".format(item), curses.A_REVERSE)
			value = screen.getstr(maxyx[0]-1, len(item) + 8)
			curses.noecho()
		elif ch == 'L':
			curses.echo()
			item = "Last Account Balance"
			screen.addstr(maxyx[0]-1, 0, "Enter {}:".format(item), curses.A_REVERSE)
			value = screen.getstr(maxyx[0]-1, len(item) + 8)
			curses.noecho()
		elif ch == 'X':
			break
		else:
			break

		draw_screen()

	curses.endwin()
