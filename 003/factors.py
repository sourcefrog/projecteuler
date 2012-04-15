# http://projecteuler.net/problem=3

# What is the largest prime factor of the number 600851475143 ?

a = 600851475143L

# Euclid's method... hm

# Count up through the integers: try to divide a by each one, until we
# end up dividing it away entirely.


b = 2
while a > 1:
    d, m = divmod(a, b)
    if m != 0:
        # not actually divisible by b
        b += 1
        continue
    else:
        print '%d / %d ==> %d' % (a, b, d)
        last_b = b
        a = d
        continue
