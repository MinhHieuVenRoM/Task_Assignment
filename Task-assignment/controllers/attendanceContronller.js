'use strict'

const attendance = require("../model/attendance")
const moment = require('moment')

function getDateFromGMT(date){
    var temp = new Date(date)
    console.log(date)
    var gmtTime = temp.toLocaleString("en-US", {timeZone: "Europe/London"});
    var newDate = new Date(gmtTime)
    return newDate
}

exports.checkIn = (user_id,date) =>
    new Promise((resolve,reject)=>{
        date = getDateFromGMT(date)
        var next_date = new Date(date)
        console.log(date)
        next_date.setDate(next_date.getDate() + 1)
        //console.log(next_date)
        attendance.find({user_id:user_id,attendance_date:
            {
                $gte: date,
                $lt: next_date
            }
        })
        .then(attendances=>{
            if(attendances.length == 0){

                var newAttendance = new attendance({
                    user_id: user_id,
                    check_in: true,
                    check_out: false,
                    attendance_date: new Date()
                    //created by will be filled later
                });
                newAttendance.save()

                resolve({status: 201,message: 'New attendance for this user has been created successfully. Check in successfully!',data:newAttendance })
            } else {

				return attendances[0];
			}
        })
        .then(attendance=>{
            if(attendance.check_in){
                reject({ status: 409, message: 'user already checkout' });
            }
            attendance.check_in = true;
            attendance.save()
            resolve({status: 200, message: "check in successfully", data: attendance})
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })


exports.checkOut = (user_id,date) =>
    new Promise((resolve,reject)=>{
        date = getDateFromGMT(date)
        var next_date = new Date(date)
        //console.log(date)
        next_date.setDate(next_date.getDate() + 1)
        
        attendance.find({user_id:user_id,attendance_date:
            {
                $gte: date,
                $lt: next_date
            }
        })
        .then(attendances=>{
            if(attendances.length == 0){
                reject({ status: 404, message: 'Please check in before checking out!' });
            } else {
				return attendances[0];
			}
        })
        .then(attendance=>{
            if(attendance.check_out){
                reject({ status: 409, message: 'user already checkout' });
            }
            attendance.check_out = true
            attendance.check_out_time = new Date()
            attendance.save()
            resolve({status: 200, message: "check out successfully", data: attendance})
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getListAttendanceByDate = (date) =>
    new Promise((resolve,reject)=>{
        date = getDateFromGMT(date)
        var next_date = new Date(date)
        next_date.setDate(next_date.getDate() + 1)
        attendance.aggregate([
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
            // Project only wanted fields.
            { "$project": { "_id":1,
                            "attendance_date":1,
                            "status" :1,
                            "check_in":1,
                            "check_out":1,
                            "user_detail.name" : 1,
                            "check_out_time":1
                        } 
            },
            { "$match": {attendance_date:
                {
                    $gte: date,
                    $lt: next_date
                }
            } },
        ])
        .then(attendances=>{
            resolve({status: 200, message: "get list attendance user by date successfully", data: attendances})
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getListAttendanceByUser  = (user_id) =>
    new Promise((resolve,reject)=>{
        attendance.find({user_id: user_id})
        .then(attendances=>{
            resolve({status: 200, message: "get list attendance of this user successfully", data: attendances})
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getListAttendanceUserByMonth  = (user_id,date) =>
    new Promise((resolve,reject)=>{
        //console.log(new Date(date))
        //date = getDateFromGMT(date)
        date = new Date(date)
        var next_month = new Date(date)
        next_month.setMonth(next_month.getMonth() + 1)
        console.log(date)
        console.log(next_month)
        attendance.find({user_id:user_id,attendance_date:
            {
                $gte: date,
                $lt: next_month
            }
        })
        .then(attendances=>{
            resolve({status: 200, message: "get list attendance of this user by month successfully", data: attendances})
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getListAttendanceByMonth = (date) =>
    new Promise((resolve,reject)=>{
        date = new Date(date)
        var next_month = new Date(date)
        next_month.setMonth(next_month.getMonth() + 1)
        console.log(date)
        console.log(next_month)
        attendance.aggregate([
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
            // Project only wanted fields.
            { "$project": { "_id":1,
                            "attendance_date":1,
                            "status" :1,
                            "check_in":1,
                            "check_out":1,
                            "user_detail.name" : 1,
                            "check_out_time":1
                        } 
            },
            { "$match": {attendance_date:
                {
                    $gte: date,
                    $lt: next_month
                }
            } },
        ])
        .then(attendances=>{
            resolve({status: 200, message: "get list attendance user by month successfully", data: attendances})
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getListAttendanceUserByYear  = (user_id,date) =>
    new Promise((resolve,reject)=>{
        //console.log(new Date(date))
        //date = getDateFromGMT(date)
        date = new Date(date)
        var next_year = new Date(date)
        next_year.setFullYear(next_year.getFullYear() + 1)
        console.log(date)
        console.log(next_year)
        attendance.find({user_id:user_id,attendance_date:
            {
                $gte: date,
                $lt: next_year
            }
        })
        .then(attendances=>{
            resolve({status: 200, message: "get list attendance of this user by year successfully", data: attendances})
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.getListAttendanceByYear = (date) =>
    new Promise((resolve,reject)=>{
        date = new Date(date)
        var next_year = new Date(date)
        next_year.setFullYear(next_year.getFullYear() + 1)
        console.log(date)
        console.log(next_year)
        attendance.aggregate([
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
            // Project only wanted fields.
            { "$project": { "_id":1,
                            "attendance_date":1,
                            "status" :1,
                            "check_in":1,
                            "check_out":1,
                            "user_detail.name" : 1,
                            "check_out_time":1
                        } 
            },
            { "$match": {attendance_date:
                {
                    $gte: date,
                    $lt: next_year
                }
            } },
        ])
        .then(attendances=>{
            resolve({status: 200, message: "get list attendance user by year successfully", data: attendances})
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })

exports.checkAttendance = (user_id,date) =>
    new Promise((resolve,reject)=>{
        date = getDateFromGMT(date)
        var next_date = new Date(date)
        next_date.setDate(next_date.getDate() + 1)
        attendance.find({user_id:user_id,attendance_date:
            {
                $gte: date,
                $lt: next_date
            }
        })
        .then(attendances=>{
            if(attendances.length > 0){
                resolve({status: 200, message: "True", data: true})
            }else{
                resolve({status: 200, message: "False", data: false})
            }
                
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })