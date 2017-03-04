import unittest
from src.backend.backend import Backend
from src.backend.month import Month
from datetime import datetime
from dateutil.relativedelta import relativedelta

class TestCalculatePeriod(unittest.TestCase):
    def setUp(self):
        self.backend = Backend()

    def test_current_month_getters(self):
        """Test current month getter"""
        month = Month(revenues=2000, expenses=1000, adjustments=0, income_target=500, opening_balance=0)
        self.backend.set_current_month(month)
        self.assertEqual(self.backend.get_current_month()['month'], month)

    def test_current_month_getters(self):
        """Test current month getter"""
        today = datetime.today()
        # try getting one month ahead
        later = today + relativedelta(months=+1)
        self.assertIsNotNone(self.backend.get_month(later.month, later.year))

if __name__ == '__main__':
    unittest.main()