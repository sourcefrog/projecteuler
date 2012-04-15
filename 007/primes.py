# What is the 10 001st prime number?

primes = [2]


def is_prime(a, primes):
    b = a
    for x in primes:
        d, m = divmod(b, x)
        if m == 0:
            return False
    else:
        return True


a = 3
while len(primes) <= 10001:
    # There's something faster than just checking all of them, but this
    # will do for now.
    if is_prime(a, primes):
        primes.append(a)
        print a
    a += 1


print primes[10000]
