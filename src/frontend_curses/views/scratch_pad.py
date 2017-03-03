import curses
from view import View


class ScratchPad(View):

    def __init__(self, screen):
        self.screen = screen
        self.transactions = []

    def draw(self, erase=True):
        View.draw(self, erase)
        self.screen.addstr(0, 0, "[+] Add Transaction", curses.A_NORMAL)
        self.screen.addstr(1, 0, "[-] Remove Transaction", curses.A_NORMAL)

        for index, transaction in enumerate(self.transactions):
            self.screen.addstr(3 + index, 0, "({}) {}".format(str(index), str(transaction['amount'])), curses.A_NORMAL)
            self.screen.addstr(3 + index, 30, str(transaction['description']), curses.A_NORMAL)



    def handle_input(self, input):
        if input == '+':
            curses.echo()
            amount = self.read_int("Enter Transaction Amount")
            description = self.read_string("Enter Transaction Description")
            self.transactions.append({
                'amount': amount,
                'description': description
            })
            curses.noecho()
            return True

        elif input == '-':
            curses.echo()
            id = self.read_int("Enter Transaction ID")
            del(self.transactions[id])
            curses.noecho()
            return True

        return False