import unittest
import src.main as main

class TestExampleFunction(unittest.TestCase):
	def test_1(self):
		self.assertEqual(main.example_function(), 'OK')

if __name__ == '__main__':
	unittest.main()
