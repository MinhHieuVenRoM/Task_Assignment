// // get dependencies
// const express = require('express');
// const bodyParser = require('body-parser');

// Configuring the database
const dbConfig = require('./config/config.js');
const mongoose = require('mongoose');

// const app = express();

// // parse requests
// app.use(bodyParser.urlencoded({ extended: false }))
// app.use(bodyParser.json())

// // default route
// app.get('/', (req, res) => {
//     res.json({"message": "Welcome to Task assignment app"});
// });

// // listen on port 3000
// app.listen(3000, () => {
//     console.log("Server is listening on port 3000");
// });

mongoose.Promise = global.Promise;

// Connecting to the database
mongoose.connect(dbConfig.url, {
    useNewUrlParser: true
}).then(() => {
    console.log("Successfully connected to the database");    
}).catch(err => {
    console.log('Could not connect to the database. Exiting now...', err);
    process.exit();
});
