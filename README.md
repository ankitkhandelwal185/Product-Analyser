This project analyses products through twitter. 
Currently implemented for Mobile Phones. 
User Have to give Mobile name like MOtoG5Plus then user will see result as a piechart like how many positive, negative reviews that product have.
--------------------------------------------------------
Node.js server API's
Login api           http://localhost:1000/api/login
register api        http://localhost:1000/api/register
Product detail api  http://localhost:1000/api/product
--------------------------------------------------------
Flask server API's 
product analyse api http://127.0.0.1:5000/product
--------------------------------------------------------
MongoDB Database link localhost:27017/mdb
MongoDB database collection names
for login/register  'tblstatuses'
for saving tweets   'tbltweets'
for saving filter tweets 'tblfiltertweet'
for saving final result  'tbltweetresult'