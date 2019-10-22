var mongoose = require('mongoose');  

var UserSchema = new mongoose.Schema({  
  name: String,
  email: String,
  hashed_password: String,
  created_at : String,
  temp_password: String,
  temp_password_time: String
});

module.exports = mongoose.model('users', UserSchema);