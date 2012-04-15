s = 0
# _below_ 1000, ie not including 1000
for i in xrange(1, 1000):
    if (i % 3 == 0) or (i % 5 == 0):
        s += i
print s 
