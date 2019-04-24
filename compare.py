import sys
import sqlite3

if len(sys.argv) < 3:
    print("Usage: python compare.py IA1 IA2")
    exit(1)

ia1 = sys.argv[1]
ia2 = sys.argv[2]

path = "match.sqlite"
conn = sqlite3.connect(path)

c = conn.cursor()
 
c.execute("select count(*) from match where winner='{}' and loser='{}'".format(ia1, ia2))
win1 = int(c.fetchone()[0])

c.execute("select count(*) from match where winner='{}' and loser='{}'".format(ia2, ia1))
win2 = int(c.fetchone()[0])
 
totalmatches = win1 + win2

conn.close()

print("Comparison {} VS {}:".format(ia1, ia2))
print("Total matches : {}".format(totalmatches))
print("{} : {} wins ({} %)".format(ia1, win1, 100 * win1 / float(totalmatches)))
print("{} : {} wins ({} %)".format(ia2, win2, 100 * win2 / float(totalmatches)))