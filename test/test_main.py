import unittest
import src.main as main


class TestCalculatePeriod(unittest.TestCase):
    def test_1(self):
        """Expecting surplus"""
        end_values = main.calculate_period_estimate(
            revenue=2000,
            expenses=1000,
            adjustments=-250,
            income_target=500,
            last_balance=0
        )
        self.assertEqual(end_values["net_income"], 750)
        self.assertEqual(end_values["new_balance"], 750)
        self.assertEqual(end_values["surplus"], 250)

    def test_2(self):
        """Expecting deficit"""
        end_values = main.calculate_period_estimate(
            revenue=2000,
            expenses=1500,
            adjustments=-500,
            income_target=500,
            last_balance=0
        )
        self.assertEqual(end_values["net_income"], 0)
        self.assertEqual(end_values["new_balance"], 0)
        self.assertEqual(end_values["surplus"], -500)

    def test_3(self):
        end_values = main.calculate_period_closing(
            income_target=500,
            last_balance=0,
            closing_balance=600
        )
        self.assertEqual(end_values["closing_surplus"], 100)
        self.assertEqual(end_values["closing_adjustment"], 100)

    def test_4(self):
        end_values = main.calculate_period_closing(
            income_target=500,
            last_balance=500,
            closing_balance=750
        )
        self.assertEqual(end_values["closing_surplus"], -250)
        self.assertEqual(end_values["closing_adjustment"], -250)

if __name__ == '__main__':
    unittest.main()
