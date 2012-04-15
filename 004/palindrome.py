# Find the largest palindrome made from the product of two 3-digit numbers.

# There might be an easier way but perhaps we can just brute-force it,
# since there's only a moderate number of combinations.

max = 999

def is_palindrome(n):
    # cheesy but works.  can't use reversed() directly on strings
    na = str(n)
    return list(str(n)) == list(reversed(str(n)))

best = 1
for a in range(1, max+1):
    for b in range(1, a+1):
        n = a * b
        if n > best and is_palindrome(n):
            print '%6d = %3d x %3d' % (n, a, b)
            best = n

print 'best:', best

