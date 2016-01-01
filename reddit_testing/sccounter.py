import sys
import praw

sub = sys.argv[1]

user_agent = ("PyMuserBot 0.1")
r = praw.Reddit(user_agent=user_agent)

subreddit = r.get_subreddit(sub)
submissions = subreddit.get_top_from_week(limit=100)

sc_links = 0

for sub in submissions:
	if("soundcloud.com" in sub.domain):
		sc_links += 1

print sc_links