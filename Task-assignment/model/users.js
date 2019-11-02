var mongoose = require('mongoose');  
const validator = require('validator')

var UserSchema = new mongoose.Schema({  
  name: String,
  email: {
    type: String,
    required: true,
    unique:true,
    trim: true,
    validate(value){
        if(!validator.isEmail(value)){
            throw new Error('Email is invalid!')
        }
    }

},
  hashed_password: String,
  token: String,
  role: {
    type: Number,
    default: 1
},
  dob: Date,
  phone: String,
  sex: Number,
  status:{
    type: Number,
    default: 0
},
  created_at : String,
  temp_password: String,
  temp_password_time: String
});

module.exports = mongoose.model('users', UserSchema);