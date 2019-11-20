'use strict'

const task = require('../model/task')
const common = require('../common')

exports.getAllTask = () =>
    new Promise((resolve,reject)=>{
        task.aggregate([
            {
                "$addFields": {
                  "user_id": {
                    "$toObjectId": "$user_id"
                  }
                }
              },
            { $lookup:
                {
                  from: 'users',
                  localField: 'user_id',
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
                            "user_detail.name" : 1,
                            "project_id":1,
                            "content":1
                        } 
            }
        ])
        .then(tasks=> resolve(tasks))
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })
    
exports.getTaskOfProject = (project_id) =>
    new Promise((resolve,reject)=>{
        task.aggregate([
            {
                "$addFields": {
                  "user_id": {
                    "$toObjectId": "$user_id"
                  }
                }
              },
            { $lookup:
                {
                  from: 'users',
                  localField: 'user_id',
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
                            "user_detail.name" : 1,
                            "project_id":1,
                            "content":1
                        } 
            },
            { "$match": { "project_id":  project_id} },
        ])
        .then(tasks=> resolve(tasks))
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.createTask = (name,content,project_id,user_id,end_date,created_by) =>
    new Promise((resolve,reject)=>{
        if(new Date(end_date) < new Date()){
          reject({status: 409, message: 'Deadline must be greater than current day',data:{}})
        }
        var newTask = new task({
            name: name,
            content: content,
            end_date: end_date,
            project_id: project_id,
            user_id: user_id,
            status: 0,
            created_by: created_by
            //created by will be filled later
        });
        newTask.save()

        .then(()=>resolve({status: 201,message: 'New task has been created successfully!',data:newTask }))

        .catch(err => {
            if(err.code == 11000){
                reject({status: 409, message: 'task already created!'})

            }else{
                reject({status: 500, message:  'Internal Server Error !'})
            }
        })
    })

exports.editTaskById = (updates,body_val) =>
    new Promise((resolve,reject)=>{
        task.find({_id:body_val["_id"]})
        .then((tasks)=>{
          let task = tasks[0]
          updates.forEach((update) => task[update] = body_val[update])
          task.save();
          return task
        })

        .then((task)=>resolve({status: 201,message: 'The task has been updated successfully!',data: task }))

        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getTaskByDate = (date) =>
    new Promise((resolve,reject)=>{
      date = common.getDateFromGMT(date)
      console.log(date)

      task.aggregate([
            {
                "$addFields": {
                  "user_id": {
                    "$toObjectId": "$user_id"
                  }
                }
              },
            { $lookup:
                {
                  from: 'users',
                  localField: 'user_id',
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
                            "user_detail.name" : 1,
                            "project_id":1,
                            "content":1
                        } 
            },
            { "$match": { "end_date":  { $gte : date}} },
            
        ])

      .then(tasks=> resolve(tasks))
      .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getUserTaskByDate = (user_id,date) =>
    new Promise((resolve,reject)=>{
      date = common.getDateFromGMT(date)
      console.log(date)
      task.find({user_id:user_id,end_date:
        {
            $gte: date
        }
    })
      .then(tasks=> resolve(tasks))
      .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getUserTask = (user_id) =>
    new Promise((resolve,reject)=>{
      task.find({user_id:user_id,})
      .then(tasks=> resolve(tasks))
      .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getUserProjectTask = (user_id,project_id) =>
    new Promise((resolve,reject)=>{
      task.find({user_id:user_id,project_id:project_id})
      .then(tasks=> resolve(tasks))
      .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getUserProjectTask = (user_id,project_id) =>
    new Promise((resolve,reject)=>{
      task.find({user_id:user_id,project_id:project_id})
      .then(tasks=> resolve(tasks))
      .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })