-- Project Euler 'Blood Test' <http://projecteuler.net/problem=352>
-- Author: Martin Pool <mbp@sourcefrog.net>
--
-- Discussion: 
--
-- The problem has optimal substructure: if we know the best way to
-- determine which of N sheep is infected, that's true regardless of
-- whether N is the whole flock or any subset of it.  
--
-- Conjecture: it's always worth running a test across a larger group
-- before subdividing it, regardless of the chance of a positive or
-- negative result from the larger test.  If the first test turns out
-- negative, it will save us needing to do any further testing at all.
-- If it turns out positive, we will still need to test inside it.  In
-- some cases we can skip the last of the internal tests: if we find all the
-- earlier ones are uninfected, then the last one must be infected.


-- |Probability that any one sheep is infected.
p_infected = 0.02

p_not_infected = 1 - p_infected

-- |Probability that at least one sheep from a group of 'n' is infected.
p_any_infected :: Integer -> Float
p_any_infected n = 1 - p_none_infected n

p_none_infected :: Integer -> Float
p_none_infected n = p_not_infected ** (fromIntegral n)

-- |Expected cost (number of tests) to determine which of 'n' sheep 
-- is infected.
cost :: Integer -> Float

-- Trivially, for one sheep, we must just test it.
-- cost 1 = 1

-- For any number of sheep, when we don't know if any of them are
-- infected: the cost is, firstly one test to see if any of them are
-- infected, then in the case that there are any infected sheep, the cost
-- of searching inside the flock to find the specific problems.
cost n = 1.0 + (p_any_infected n) * (inside n)

-- |Cost to search inside a subset of sheep, give that we know at least
-- one is infected.
inside :: Integer -> Float

-- For just one, if we know it's infected, that's it.
inside 1 = 0

-- For a subset of two sheep, given we know at least one is infected: we
-- must check the first one.  If it's not infected, then we know the
-- second one is , without needing to check.  If it is infected,
-- the second one might be as well, and we need to check that too.
-- inside_a 2 = (cost 1) + p_infected * (cost 1)


-- For four sheep, given we know at least one is infected: we must check
-- the first two; if neither of the first two is infected then we know
-- one of the second two must be infected and we must check them.
-- Otherwise, we don't know if the second two are infected and we must
-- check.
--
-- More generally, to examine any number of sheep, we'll split them into
-- two parts, and test each.  The first can be one larger.
-- If none of the first group is infected, then we know there must be
-- at least one infected in the second group, and we can proceed to 
-- search inside it.  
--
-- On the other hand if there is one infected in the first group, we
-- still need to search the second group, without knowing whether we're
-- going to find anything or not.
--
-- So the cost is: 
-- test a
-- if a is hot, test inside a (given it's hot), plus the whole of b,
-- plus if it's hot inside b
-- if a is hot, test the whole of b, and if that's hot, inside b
-- if a is not hot, b is definitely hot, test inside b

inside n = 
    minimum [inside_split a n | a <- [1..n-1]]

half_inside n =
    let a = toInteger $ ceiling (fromIntegral n / 2)
    in inside_split a n

inside_split a n = 
    let
	b = n - a
	p_a = p_subgroup_infected a n
	p_b = p_subgroup_infected b n
        in
    1.0
    + p_a * (inside a + 1.0 + p_b * inside b)
    + (1 - p_a) * (inside b)  


-- Ah, so the key problem is that the probability of any of A being
-- affected is different if we know A is chosen from a larger N which
-- contains at least one infected item.  
--
-- Specifically, by Bayes' Theorem, P(A|N) = P(N|A) * P(A) / P(B)
-- and P(N|A) = 1
--
-- Typically for a half-way split with even distribution and a low
-- chance of individual infection, this will be about 0.5 - there's like
-- to be just one infected sheep and it's equally likely to be in
-- either.  As the infection rate rises and the size of the total group
-- rises this will go up, as there's more chance there'll be one or more
-- infected sheep in both sides.

-- So 'inside 2' is: there are two sheep, and three possibilities: A, B,
-- or AB, but AB is much less likely (0.02**2).  We must test A.  There
-- is a slightly over 0.5 chance it's infected.  If it is, we don't need
-- to (can't) subdivide A or B any more.  We do need to test B, with
-- just one more test.  On the other hand if A is negative then B must
-- be positive and we don't need to test anything else.  So that's one
-- test for sure, and a slightly over 1/2 chance we need to test B, and
-- the calculated value 1.5050 is about right.

-- So assuming this is valid for a two-way split, we can in fact take 
-- different decisions at each level depending on how we expect the
-- balance to work out, finding the value of a that gives a p_a that
-- gives the minimum cost.  It's not necessarily the middle spit.
--
-- I wonder if the easiest way is to just try all possible splits and
-- see which one works, though for 10,000 that might get expensive.
--
-- So it turns out, yes, it is quite expensive; I guess Haskell isn't
-- caching the intermediate computations (and I'm not sure how to make
-- it.)

p_subgroup_infected a n = (p_any_infected a) / (p_any_infected n)

p_subgroup_not_infected a n = 1 - p_subgroup_infected a n

cost5 5 = 
    1.0
    + (p_none_infected 5) * 0
    + (p_any_infected 5) * 5

-- | 'main' runs the main program
main :: IO ()
main = print $ cost 25

-- query: must 'inside n < cost n'?  I think there is something confused
-- about the prior probabilities here.  In some ways, the cost of
-- searching inside ought to be less because we have more information
-- about the thing we have to search.  However, that information makes
-- it more likely the search will have to go deeper.  
--
-- So inside(n) is really, E(cost|infected), whereas cost(n) is just
-- E(cost), and it's reasonable the cost would be higher then.
--
-- But I think this is where the bug is.

-- vim: tw=72
