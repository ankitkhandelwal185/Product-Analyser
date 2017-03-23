import pymongo
from flask import Flask, request, jsonify
from pymongo import MongoClient
import re
import requests
import json
from textblob import TextBlob

app = Flask(__name__)
client = MongoClient()
client = MongoClient("mongodb://localhost:27017")
db = client.mdb
collection = db.tblproducts
@app.route('/')
def index():
    return 'Hello Flask! %s '% request.method


#result = db.tbltweets.create_index([('tweetText' , pymongo.ASCENDING)], unique=True)

pnames = [{'pname': 'zukz1'}]
@app.route('/product', methods=['GET', 'POST'])
def ankit():
    if request.method == 'POST':

        pname = {'pname' : request.json['pname']}
        pnames.append(pname);
        dataset(pname)
        print pname
        return jsonify({'pnames':pnames})

    elif request.method == 'GET':
        dataset_nltk()
        #print(list(collection.find()))
        # close connection
        #client.close()
        return "Your get request done!!"

def receive(str):
    return str
def dataset(productname):
        print productname

        l = []
        def clean_tweet(tweet):
            return ' '.join(re.sub("(@[A-Za-z0-9]+)|([^0-9A-Za-z \t])|(\w+:\/\/\S+)", " ", tweet).split())

        db = client.mdb
        collection = db.tbltweets
        for post in collection.find({'productName': productname}, {'tweetText': 1, '_id': 0, '__v': 0, 'productName':0}).distinct("tweetText"):

            # print('for loop')
            # pprint.pprint(post)

            # print post
            l.append(clean_tweet(post))

            # file.write('/n')
            # file.write(json.dump(post,separators=(',', ': ')))
        # print l
        c = 0

        for i in range(0, len(l)):
            print l[i]
            # c=c+1
            db.tblfiltertweet.insert({'tweetfilter': str(l[i])})

        return "here"

def get_tweet_sentiment(tweet):
    analysis = TextBlob(tweet)

    if analysis.sentiment.polarity > 0:
        return 'positive'
    elif analysis.sentiment.polarity == 0:
        return 'neutral'
    else:
        return 'negative'

tweets_data = []
l = []

def dataset_nltk():
    #print("fun")
    for line in db.tblfiltertweet.find({}, {'tweetfilter': 1, '_id': 0}).distinct("tweetfilter"):
        try:
            #print("fun try")
            print line
            # x=line.split("tweet")
            # print line[13:-2]
            l.append(line)
            # global tweet
            # print a
            tweet = json.load(line)
            # print tweet

            tweets_data.append(tweet)
        # print(tweet)
        except:
            continue
            # print len(tweets_data)

    print len(l)
    # print l
    for i in l:
        # print "eneter"
        parsed_tweet = {}
        # a.append(set(i))
        a = []
        # print i
        a = i.split(" ")
        # print a
        a[-1] = a[-1].strip()
        b = set(a)
        # print b

        q = ["excellent" ,"DISCHARGE"]
        p = set(q)
        # print b
        if len(b & p) > 0:
            #print "enyer"
            print i
            parsed_tweet['sentiment'] = get_tweet_sentiment(i)
            d = get_tweet_sentiment(i)
            db.tbltweetresult.insert({ 'tweet': i, 'polarity': d})
            #print d

            # if q in a:
            # print a



if __name__ == '__main__':
    app.run(debug=True)


