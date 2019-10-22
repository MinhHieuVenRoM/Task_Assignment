'use strict'

const task = require('../model/task')

exports.getAllTask = () =>
    new Promise((resolve,reject)=>{
        //task.find({},{_id: 0})
        task.findById('5daeada277feb0456d4c09f3')
        .then(tasks=> resolve(tasks))
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })
