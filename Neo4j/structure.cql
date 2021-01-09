-- Structure
-- persons
create (philip: Person {name: 'Philip'}) return philip
create (mark: Person {name: 'Mark'}) return mark
create (bruno: Person {name: 'Bruno'}) return bruno

-- restaurants
create (isushi: Restaurant {name: 'iSushi'}) return isushi
create (zushi_zam: Restaurant {name: 'Zuzhi Zam'}) return zushi_zam

-- dish
create (sushi: Dish {cuisine: 'Sushi'}) return sushi

-- cities
create (ny: City {location: 'New York'}) return ny

-- relations
-- persons -> persons
MATCH (p:Person), (m:Person)
WHERE p.name = 'Philip' AND m.name = 'Mark'
CREATE (p)-[r:IS_FRIEND_OF]->(m)
RETURN p, m

MATCH (p:Person), (b:Person)
WHERE p.name = 'Philip' AND b.name = 'Bruno'
CREATE (p)-[r:IS_FRIEND_OF]->(b)
RETURN p, b

-- persons -> dish
MATCH (b:Person), (p:Restaurant)
WHERE b.name = 'Bruno' AND p.name = 'iSushi'
CREATE (b)-[r:LIKES]->(p)
RETURN b, p

MATCH (m:Person), (p:Restaurant)
WHERE m.name = 'Mark' AND p.name = 'Zuzhi Zam'
CREATE (m)-[r:LIKES]->(p)
RETURN m, p

-- dish -> restaurant
MATCH (p:Restaurant), (r:Dish)
WHERE p.name = 'iSushi' AND r.cuisine = 'Sushi'
CREATE (p)-[s:SERVES]->(r)
RETURN p, r

MATCH (p:Restaurant), (r:Dish)
WHERE p.name = 'Zuzhi Zam' AND r.cuisine = 'Sushi'
CREATE (p)-[s:SERVES]->(r)
RETURN p, r

-- dish -> city
MATCH (p:Restaurant), (c:City)
WHERE p.name = 'iSushi' AND c.location = 'New York'
CREATE (p)-[r:LOCATED_IN]->(c)
RETURN p, r

MATCH (p:Restaurant), (c:City)
WHERE p.name = 'Zuzhi Zam' AND c.location = 'New York'
CREATE (p)-[r:LOCATED_IN]->(c)
RETURN p, r

-- --------------------------------------
-- --------------------------------------
-- Querys
-- Exercise 2
match (n) return n

-- Exercise 3
match
(r:Restaurant)
<-[:SERVES]-
(d:Dish {cuisine: 'Sushi'})
<-[:LIKES]-
(pp:Person)
<-[:IS_FRIEND_OF]-
(p:Person {name: 'Philip'})
return pp

-- Exercise 4
match
(r:Restaurant)
where r.name contains 'ushi'
return r

-- Exercise 5
match
(r:Restaurant)
<-[:SERVES]-
(d:Dish {cuisine: 'Sushi'})
return d

-- Exercise 6
create
(parrillada_paraside:Restaurant {name: 'ParrilladaParadise'})
return parrillada_paraside

create
(parrillada:Dish {cuisine: 'Parrillada'})
return parrillada

MATCH (r:Restaurant), (c:City)
WHERE r.name = 'ParrilladaParadise' AND c.location = 'New York'
CREATE (r)-[:LOCATED_IN]->(c)
RETURN r, c

MATCH (r:Restaurant), (d:Dish)
WHERE r.name = 'ParrilladaParadise' AND d.cuisine = 'Parrillada'
CREATE (r)-[:LOCATED_IN]->(d)
RETURN r, d

-- Exercise 7
match (n:Restaurant {name: 'Zuzhi Zam'})
set n.name = 'Zuzhi'
return n

-- Exercise 8
match (n:City {name: 'New York'})
set n.country = 'USA'
return n

-- Exercise 9
create (d:Dish {cuisine: 'Sashimi'})
return d

match
(r:Restaurant)
<-[:LOCATED_IN]:
(c:City {location: 'New York'})
<-[:SERVES]-
(d:Dish {cuisine: 'Sushi'})
<-[:LIKES]-
(pp:Person)
<-[:IS_FRIEND_OF]-
(p:Person {name: 'Philip'}),
(dd:Dish {cuisine: 'Sashimi'})
CREATE (r)-[:SERVES]->(dd)
return r, dd

-- Exercise 10
MATCH (n)
RETURN n
SKIP 3
LIMIT 1

-- Exercise 11
MATCH (n:City { location: 'New York' })
OPTIONAL MATCH (n)-[r]-() 
DELETE n, r