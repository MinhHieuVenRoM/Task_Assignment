var mongoose = require('mongoose');  

var TaskSchema = new mongoose.Schema({  
  name: String,
  content: String,
});

module.exports = mongoose.model('task', TaskSchema,'task');