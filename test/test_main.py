import unittest
import src.main as main

class TestCalculatePeriod(unittest.TestCase):
	def test_1(self):
		"""Expecting surplus"""
		end_values = main.calculate_period(
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
		end_values = main.calculate_period(
			revenue=2000,
			expenses=1500,
			adjustments=-500,
			income_target=500,
			last_balance=0
		)

		self.assertEqual(end_values["net_income"], 0)
		self.assertEqual(end_values["new_balance"], 0)
		self.assertEqual(end_values["surplus"], -500)

if __name__ == '__main__':
	unittest.main()
