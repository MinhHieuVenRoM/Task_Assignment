var mongoose = require('mongoose');  

var LocationSchema = new mongoose.Schema({  
  name: String,
  localX: Number,
  localY: Number,
});

module.exports = mongoose.model('location', LocationSchema,'location');