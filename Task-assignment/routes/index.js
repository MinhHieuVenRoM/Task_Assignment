var express = require('express');
var router = express.Router();

/* GET home page. */
// router.get('/', function(req, res, next) {
//   res.render('index', { title: 'Express' });
// });

// default route
router.get('/', function(req, res,next){
  res.json({"message": "Welcome to Task assignment apps"});
});

module.exports = router;
