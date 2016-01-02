#! /usr/bin/env python
import sys
import praw

data = sys.argv[1]

user_agent = ("PyMuserBot 0.1")
r = praw.Reddit(user_agent=user_agent)


def sc_count(r, sub):
	try:
		subreddit = r.get_subreddit(sub)
		submissions = subreddit.get_top_from_week(limit=100)

		sc_links = 0

		for sub in submissions:
			if("soundcloud.com" in sub.domain):
				sc_links += 1

		return sc_links
	except Exception, e:
		return -1
	

with open(data) as f:
	for line in f:
		sub = line.strip()
		print "%d: %s" % (sc_count(r, sub), sub)




