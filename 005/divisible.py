# What is the smallest positive number that is evenly divisible by all of
# the numbers from 1 to 20?
#
# You might think we can just see if it's divisble, and if not then
# multiply, but it's not quite that simple, because we might need to just
# multiply by a prime factor to get there.
#
# Perhaps easiest is to decompose them all in to primes and then make sure
# there are "at least enough" of each prime....
#
# Or, perhaps, for such a small number, just brute force will do... It's
# probably only around 400.

a = 20
while True:
    ok = True
    for b in range(20, 1, -1):
        if (a % b) != 0:
            # print '%d not divisible by %d' % (a, b)
            ok = False
            break
    if ok:
        break
    else:
        a += 20
print a
