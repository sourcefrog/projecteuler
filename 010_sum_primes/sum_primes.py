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


def _is_prime(i, primes):
    for x in primes:
        if i % x == 0:
            return False
        if x * x >= i:
            break
    return True


def generate_primes():
    yield 2
    primes = [2]
    # Tiny optimization, consider only odd numbers
    i = 3
    while True:
        if _is_prime(i, primes):
            primes.append(i)
            yield i
        i += 2


limit = 2000000


sum = 0
for i in generate_primes():
    if i < limit:
        sum += i
    else:
        break
print sum
