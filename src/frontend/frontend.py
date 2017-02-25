#!/usr/bin/env python
import curses

if __name__ == "__main__":
	screen = curses.initscr()

	curses.noecho()
	curses.cbreak()
	curses.curs_set(0)

	screen.addstr("$2000.0", curses.A_NORMAL)
	screen.chgat(-1, curses.A_NORMAL)
	screen.addstr(screen.getmaxyx()[0]-1, 0, "Enter anything:", curses.A_REVERSE)

	screen.refresh()
	screen.getch()

	curses.endwin()
