var express = require('express');
var mongoose = require('mongoose');
var bodyParser = require('body-parser');
var TwitterPackage = require('twitter');
var Twit = require('twit');
var nodemailer = require('nodemailer');
mongoose.Promise = global.Promise;
mongoose.connect('mongodb://localhost:27017/mdb');

var app = express();
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

//database for login/signup
var statusSchema = new mongoose.Schema({
  name: String,
  password: String,
  email: String
});
var user = mongoose.model('tblstatus', statusSchema);
//database for product details
var productSchema = new mongoose.Schema({
  email: String,
  cname: String,
  pname: String
});
var product = mongoose.model('tblproducts', productSchema);

//database to store tweets 
var tweetSchema = new mongoose.Schema({
  productName:  String,
  tweetText: String
});
var tweet = mongoose.model('tbltweet', tweetSchema);

var T = new Twit({
    consumer_key: 'x0dkQ3zWtu9Rc7oDqYpk9xPbu',
    consumer_secret: 'jgFFJOTKu48rKIK2Y3xun5dS3xv6CPdMwZLOZEGCWDxdE9DS6M',
    access_token: '2945178732-46KQwauByeY2A3vuLpwe8jHbvvoZZor3VJB60j1',
    access_token_secret: '613zjxmcuTAEULGbXjmSxOV42v8KOqlt1RuyP1pBCrkuA'
});
//var Twitter = new TwitterPackage(secret);
var stream = T.stream('statuses/filter', { track: 'lenovozukz1' });

app.post('/api/login', function(request, responce){
        console.log('login '+request.method);
        user.find(({name:request.body.username,password: request.body.password})/*.count()==0*/,  function(err, docs){
          responce.json(docs);
        });
});
var tweetresult = new mongoose.Schema({
  polarity : String,
  tweet: String
})

var result = mongoose.model('tbltweetresult', tweetresult)
app.get('/api/result', function(request, responce){
  result.find(({}),  function(err, docs){
          responce.json(docs);
        });
});

app.post('/api/register', function(request, responce){
        
        console.log('status get '+request.method);
          new user({
              name:request.body.name,
              password: request.body.password,
              email: request.body.email
              
        }).save(function(err, doc){
          if(err) responce.json(err);
          else responce.redirect('/thankyou');
        });
});

app.get('/api/product', function(request, responce){
        console.log('product get '+request.method);
         product.find( function(err, docs){
          responce.json(docs);
        });
});

app.post('/api/product', function(request, responce){
 // handleSayHello(request, responce);
        console.log('product request '+request.method);
        
        var createAscendingIndex = function(db, callback) {
              // Get the users collection
              var collection = db.collection('tbltweets');
              // Create the index
              collection.createIndex(
                { tweetText : 1 }, function(err, result) {
                console.log("index "+result);
                callback(result);
              });
            };

          new product({
              email: request.body.email,
              cname: request.body.cname,
              pname: request.body.pname
              
        }).save(function(err, doc){
          if(err) responce.json(err);
          else {
            responce.send('/thankyou');
           // stream.on('tweet', function(tweet){
            //  console.log(tweet.text);
            
            //});

            //find tweet which have these parameters 
            var params = {
              q: request.body.pname,
              count: 1000,
              result_type: 'recent',
              lang: 'en'
            }
            T.get('search/tweets', params, function(err, data, response) {
              //console.log(data)
              var tweets = data.statuses;
              for(var i=0; i<tweets.length; i++){
                //console.log(tweets[i].text);
                var twt = JSON.stringify(tweets[i].text);
                console.log(twt);
                console.log("\n");
                console.log("product name "+request.body.pname);
                      new tweet({
                        productName: request.body.pname,
                        tweetText: twt
                    }).save(function(err, doc){
                      if(err) responce.json(err);
                      //else responce.redirect('Error while saving tweets ');
                    });
              }
            })

            //track -> streaming api twitter

            /*Twitter.stream('statuses/filter', {track: '#iPhone7Plus'}, function(stream) {
              stream.on('data', function(tweet) {
                console.log(tweet.text);
                //console.log("Here track");
              });

              stream.on('error', function(error) {
                console.log(error);
              });
            });*/
            

          }
        });
});


function handleSayHello(req, res) {
    // Not the movie transporter!
    console.log("sending mail....")
    var transporter = nodemailer.createTransport({
        service: 'Gmail',
        auth: {
            user: 'ankitkhandelwal185@gmail.com', // Your email id
            pass: '!@#ANKIT185' // Your password
        }
    });
  
  var text = 'Welcome to product Analyser \n\n';

  var mailOptions = {
      from: '"Ankit Khandelwal :)"<ankitkhandelwal185@gmail.com>', // sender address
      to: 'iwayankit@gmail.com', // list of receivers
      subject: 'Welcome From Team IWAYINFO.IN', // Subject line
      text: text //, // plaintext body
      // html: '<b>Hello world ✔</b>' // You can choose to send an HTML body instead
  };

  transporter.sendMail(mailOptions, function(error, info){
      if(error){
          console.log(error);
          res.json({yo: 'error'});
      }else{
          console.log('Message sent: ' + info.response);
          res.json({yo: info.response});
      };
  });


}


app.listen(1000);
console.log('Server is running on port 1000');
console.log("******************************");
