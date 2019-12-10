var mongoose = require('mongoose');  

var ChatSchema = new mongoose.Schema({  
  message: String,
  user_id: String,
  room_id: String
});

module.exports = mongoose.model('chat', ChatSchema,'chat');