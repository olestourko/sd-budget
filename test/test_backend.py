import unittest
from src.backend.backend import Backend
from src.backend.month import Month


class TestCalculatePeriod(unittest.TestCase):
    def test_month_getters(self):
        backend = Backend()
        month = Month(revenues=2000, expenses=1000, adjustments=0, income_target=500, opening_balance=0)
        backend.set_current_month(month)

        self.assertEqual(backend.get_current_month(), month)


if __name__ == '__main__':
    unittest.main()