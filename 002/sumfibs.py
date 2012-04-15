import itertools

def genfibs():
    a = 1
    b = 1
    while True:
        yield b
        c = a + b
        a = b
        b = c


# By considering the terms in the Fibonacci sequence whose values do not
# exceed four million

under4m = itertools.takewhile(lambda x: x <= 4000000,
        genfibs())
evens = (x for x in under4m if (x % 2 == 0))

print(sum(evens))
