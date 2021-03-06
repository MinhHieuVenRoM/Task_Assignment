var mongoose = require('mongoose');  

var RoomSchema = new mongoose.Schema({  
  room_name: String,
  users : [String], //[{ type : Array , "default" : [] }]
  created_by : String,
  last_message : String
});

module.exports = mongoose.model('room', RoomSchema,'room');