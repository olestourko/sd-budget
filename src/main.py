from __future__ import print_function

import logging
import blinker
import datetime

from backend.month import Month
from backend.backend import Backend
from frontend_curses.frontend import Frontend

if __name__ == "__main__":
    # Listen to events
    logging.basicConfig(filename='output.log', level=logging.DEBUG)

    backend = Backend()
    today = datetime.date.today()
    month = Month(revenues=2000, expenses=1000, adjustments=0, income_target=500, opening_balance=0)
    backend.set_month(today.month, today.year, month)

    frontend = Frontend(backend)
    frontend.run()
