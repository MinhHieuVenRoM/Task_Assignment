'use strict'

const user = require('../model/users')

exports.getProfile = email =>
    new Promise((resolve,reject)=>{
        //set 0 to exclude _id
        user.find({email: email},{name: 1, email: 1, created_at: 1,_id: 0})

        .then(users=> resolve(users[0]))
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })
