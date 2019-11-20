'use strict'

const project = require("../model/project")
const task = require("../model/task")
const users = require("../model/users")
const common = require('../common')

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

function sortProject(projects) {
  return projects.sort((a,b)=> -(a.end_date-b.end_date))
  //return projects.sort(compareProjects)
  //console.log(common.dateDiffInDays(projects[0].end_date,new Date()))
  // return projects.sort((a)=>{
  //   if(common.dateDiffInDays(a.end_date,new Date()) <= 0){
  //     console.log(-common.dateDiffInDays(a.end_date,new Date()))
  //     return -common.dateDiffInDays(a.end_date,new Date())
  //   }
  //   if(common.dateDiffInDays(a.end_date,new Date()) > 0){
  //     return -Math.pow(2,-common.dateDiffInDays(a.end_date,new Date())/365)
  //   }
  // } )
}


exports.getAllProjects = (user) =>
    new Promise((resolve,reject)=>{
        conditionalChaining(user)
        .then(projects=>{
          //console.log( common.dateDiffInDays(projects[0].end_date,projects[1].end_date))
          return sortProject(common.getUnique(projects,'_id'))
        })
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
        if(new Date(endDate) < new Date()){
          reject({status: 409, message: 'Deadline must be greater than current day',data:{}})
        }
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

        .then((project)=>resolve({status: 201,message: 'Project has been updated successfully!',data: project }))

        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getUserProject = (user_id) =>
    new Promise((resolve,reject)=>{
      project.aggregate([
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
        { "$match": { "task_detail.user_id":  user_id} },

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

        .then(projects=> resolve(common.getUnique(projects,'_id')))

        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })
