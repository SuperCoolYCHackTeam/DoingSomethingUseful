var fs = require('fs');
var express = require('express');
var app = express();
var server = require('http').createServer(app);
var io = require('socket.io')(server);
io.listen(server);

var lastImage;


app.get('/', function (req, res) {
  res.set('Access-Control-Allow-Origin', '*');
  res.sendfile(__dirname + '/index.html');
});

app.get('/image', function(req, res) {
  if (!lastImage) {
    res.send(500).end();
    return;
  }
  res.set('Content-Type', 'image/jpeg');
  res.set('Content-Length', lastImage.length);
  res.send(lastImage);
});

io.on('connection', function (socket) {
  socket.on('image', function(msg) {
    lastImage = msg.data;
  });
});


app.use(express.static(__dirname + '/public'));
server.listen(process.env.PORT || 8000);
