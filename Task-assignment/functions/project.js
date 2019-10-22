'use strict'

const project = require("../model/project")

exports.getAllProjects = () =>
    new Promise((resolve,reject)=>{
        project.find({status:0},{_id: 0})

        .then(projects=> resolve(projects))
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getProjectById = id =>
    new Promise((resolve,reject)=>{
        project.find({_id:id},{_id: 0})

        .then(projects=> resolve(projects[0]))
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

