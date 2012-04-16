# https://projecteuler.net/problem=10
# Martin Pool

"""The sum of the primes below 10 is 2 + 3 + 5 + 7 = 17.

Find the sum of all the primes below two million.
"""

# See http://en.wikibooks.org/wiki/Efficient_Prime_Number_Generating_Algorithms
#
# By pure brute force (checking against all previously known primes) this
# takes 130s.
#
# Checking only up to the square root, takes just 0.6s cpu time.
#
# We could do better by knocking-out numbers known not to be prime, which
# might avoid needing to do division.


import itertools


def generate_primes(limit):
    yield 2
    composites = set()
    for i in xrange(3, limit+1, 2):
        if i in composites:
            continue
        for j in xrange(i + i, limit+1, i):
            composites.add(j)
        yield i


print sum(generate_primes(2000000))
