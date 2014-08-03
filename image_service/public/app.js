var IMAGE_HEIGHT = 500;
var IMAGE_WIDTH = 500;

var takeSnapshot, videoEl;

function main() {
  videoEl = document.querySelector('video');
  var socket = io.connect('/');

  var sendBlob = function(blob) {
    socket.emit('image', { data: blob });
  };
  socket.on('connect', function() {
    setInterval(_.compose(sendBlob, function() {
      if (takeSnapshot) return takeSnapshot();
    }), 1000);
  });
  navigator.webkitGetUserMedia({ video: true }, function(stream) {
    videoEl.src = window.URL.createObjectURL(stream);
    takeSnapshot = snapshot();
  }, console.log.bind(console));
}

function snapshot() {
  var canvas = document.createElement('canvas');
  canvas.width = IMAGE_HEIGHT;
  canvas.height = IMAGE_WIDTH;
  return function() {
    var ctx = canvas.getContext('2d');
    ctx.drawImage(videoEl, 0, 0, IMAGE_HEIGHT, IMAGE_WIDTH);
    var image = new Image(IMAGE_HEIGHT, IMAGE_WIDTH);
    var dataURL = canvas.toDataURL('image/jpeg', 0.5);
    return dataURItoBlob(dataURL);
  };
}

function dataURItoBlob(dataURL) {
  var parts, contentType, raw, rawLength;
  var BASE64_MARKER = ';base64,';
  if (dataURL.indexOf(BASE64_MARKER) == -1) {
    parts = dataURL.split(',');
    contentType = parts[0].split(':')[1];
    raw = decodeURIComponent(parts[1]);

    return new Blob([raw], {type: contentType});
  }

  parts = dataURL.split(BASE64_MARKER);
  contentType = parts[0].split(':')[1];
  raw = window.atob(parts[1]);
  rawLength = raw.length;

  var uInt8Array = new Uint8Array(rawLength);

  for (var i = 0; i < rawLength; ++i) {
    uInt8Array[i] = raw.charCodeAt(i);
  }

  return new Blob([uInt8Array], {type: contentType});
}


document.addEventListener("DOMContentLoaded", function() {
  setTimeout(main, 2000);
});
