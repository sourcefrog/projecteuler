# http://projecteuler.net/problem=9

"""There exists exactly one Pythagorean triplet for which a + b + c = 1000.
Find the product abc.

a + b + c = 1000
aa + bb = cc
"""

# Cheesy brute-force solution.

limit = 1000
for a in range(1, limit):
    for b in range(a, limit):
        c = limit - a - b
        if a*a + b*b == c*c:
            print a, b, c
            print a * b * c
            break
    else:
        continue
    break
