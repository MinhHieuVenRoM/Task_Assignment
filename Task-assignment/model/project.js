var mongoose = require('mongoose');  

var ProjectSchema = new mongoose.Schema({  
  name: String,
  created_date: { type: Date, default: Date.now() },
  end_date: Date,
  status: Number,
  created_by: []
});

module.exports = mongoose.model('project', ProjectSchema,'project');