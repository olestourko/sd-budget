import curses


class View():
    def read_int(self, label):
        maxyx = self.screen.getmaxyx()
        self.screen.addstr(maxyx[0] - 1, 0, "Enter {}:".format(label), curses.A_REVERSE)
        value = self.screen.getstr(maxyx[0] - 1, len(label) + 8)
        return int(value)

    def draw(self, erase=True):
        if erase:
            self.screen.erase()

    def handle_input(self, input):
        raise NotImplementedError()
