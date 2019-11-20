var mongoose = require('mongoose');  

var AttendanceSchema = new mongoose.Schema({  
  user_id: String,
  attendance_date: { type: Date, default: Date.now() },
  check_in: Boolean,
  check_out: Boolean,
  check_out_time: Date,
  token: String,
  status: { type: Number, default: 0 } 
});

module.exports = mongoose.model('attendance', AttendanceSchema,'attendance');