var mongoose = require('mongoose');  

var TaskSchema = new mongoose.Schema({  
  name: String,
  content: String,
  project_id: String,
  user_id: String,
  created_date: { type: Date, default: Date.now() },
  created_by: String,
  status: Number,
  end_date: { type: Date, default: Date.now() }

});

module.exports = mongoose.model('task', TaskSchema,'task');