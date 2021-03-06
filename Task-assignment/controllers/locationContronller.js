'use strict'

const location = require('../model/location')

exports.getLocation = (name) =>
new Promise((resolve,reject)=>{
    //get all group that constain user_id
    location.find({name:name})
        .then(locations=> {
            resolve({status: 201,message: 'List locations',data:locations[0]})
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
})
