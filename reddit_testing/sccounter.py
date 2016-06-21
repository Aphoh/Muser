#! /usr/bin/env python
import sys
import csv
import praw

data = sys.argv[1]

user_agent = ("PyMuserBot 0.1")
r = praw.Reddit(user_agent=user_agent)


def sc_count(r, subreddit_name):
	try:
		subreddit = r.get_subreddit(subreddit_name)
		submissions = subreddit.get_hot(limit=100)

		sc_links = 0
		scores = []

		for sub in submissions:
			if("soundcloud.com" in sub.domain):
				sc_links += 1
				scores.append(sub.score)
		print subreddit_name
		avg_score = 0 if len(scores) == 0 else sum(scores)/len(scores)
		return (subreddit_name, sc_links, avg_score)
	except Exception, e:
		print e
		return ('error', 'error', 'error')
	

with open(data) as f:
	result = open("result.csv", 'a')
	writer = csv.writer(result)
	writer.writerow(('Subreddit', 'Soundcloud Count', 'Average Score'))
	for line in f:
		sub = line.strip()
		writer.writerow(sc_count(r, sub))




