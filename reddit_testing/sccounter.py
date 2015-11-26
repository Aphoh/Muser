import sys
import requests

sub = sys.argv[1]
r = requests.get("http://www.reddit.com/r/%s/top.json?t=week" % sub)
result = r.json()['data']['children']
size = len(
	filter(lambda x:'soundcloud.com' in x['domain'],
		map(lambda x:x['data'], result)))
print size