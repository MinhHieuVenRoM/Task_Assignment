'use strict'

const project = require("../model/project")
const task = require("../model/task")
const users = require("../model/users")

function conditionalChaining(user){
    if(user.role == 0){
        return project.aggregate([
            {
                "$addFields": {
                  "created_by": {
                    "$toObjectId": "$created_by"
                  }
                }
              },
            { $lookup:
                {
                  from: 'users',
                  localField: 'created_by',
                  foreignField: '_id',
                  as: 'user_detail'
                }
            },
            { "$unwind": "$user_detail" },
            // Project only wanted fields. In this case, exclude "task_detail"
            { "$project": { "_id":1,
                            "name":1,
                            "created_date" :1,
                            "end_date":1,
                            "status":1,
                            "user_detail.name" : 1
                        } 
            }
        ])
    } 
    else{
        //return project.find({},{_id: 0})
        return project.aggregate([
            {
                "$addFields": {
                  "_id": {
                    "$toString": "$_id"
                  },
                  "created_by": {
                    "$toObjectId": "$created_by"
                  }
                }
              },
            { $lookup:
                {
                  from: 'task',
                  localField: '_id',
                  foreignField: 'project_id',
                  as: 'task_detail'
                }
            },
            { "$unwind": "$task_detail" },
            // Now filter the condition on task_detail
            { "$match": { "task_detail.user_id":  user._id.toString()} },

            { $lookup:
                {
                  from: 'users',
                  localField: 'created_by',
                  foreignField: '_id',
                  as: 'user_detail'
                }
            },
            { "$unwind": "$user_detail" },
            // Project only wanted fields. In this case, exclude "task_detail"
            { "$project": { "_id":1,
                            "name":1,
                            "created_date" :1,
                            "end_date":1,
                            "status":1,
                            "user_detail.name" : 1
                             } }
            //{ "$project": { "task_detail":0}}
        ])
    }
}

exports.getAllProjects = (user) =>
    new Promise((resolve,reject)=>{
        conditionalChaining(user)

        .then(projects=> resolve(projects))

        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getProjectById = id =>
    new Promise((resolve,reject)=>{
        project.find({_id:id})

        .then(projects=> resolve(projects[0]))
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.createProject = (name,endDate,user_id) =>
    new Promise((resolve,reject)=>{
        var newProject = new project({
            name: name,
            end_date: endDate,
            status: 0,
            created_by: user_id
            //created by will be filled later
        });
        newProject.save()

        .then(()=>resolve({status: 201,message: 'New project has been created successfully!',data:newProject }))

        .catch(err => {
            if(err.code == 11000){
                reject({status: 409, message: 'Project already created!'})

            }else{
                reject({status: 500, message:  'Internal Server Error !'})
            }
        })
    })

exports.editProjectById = (updates,body_val) =>
    new Promise((resolve,reject)=>{
        project.find({_id:body_val["_id"]})
        .then((projects)=>{
          let project = projects[0]
          updates.forEach((update) => project[update] = body_val[update])
          project.save();
          return project
        })

        .then((project)=>resolve({status: 201,message: 'New project has been updated successfully!',data: project }))

        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })