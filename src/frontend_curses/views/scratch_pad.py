import curses
from view import View


class ScratchPad(View):
    def __init__(self, screen):
        self.screen = screen

    def draw(self, erase=True):
        View.draw(self, erase)

        self.screen.addstr(0, 0, "A lis that you can add and remove transactions from. ", curses.A_NORMAL)

    def handle_input(self, input):
        return False