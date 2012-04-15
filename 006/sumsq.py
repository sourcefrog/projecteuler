# Find the difference between the sum of the squares of the first one
# hundred natural numbers and the square of the sum.
#

nums = range(1, 101)

sumsq = sum(x**2 for x in nums)
sqsum = sum(nums) ** 2
print sqsum - sumsq
