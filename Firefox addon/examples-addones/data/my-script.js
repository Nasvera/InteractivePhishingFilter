self.port.on("drawBorder", function(color) {
  document.body.style.border = "5px solid " + color;
  console.log(document.body);
});

//var sourcecode = document.body;
//worker.port.emit(sourcecode);