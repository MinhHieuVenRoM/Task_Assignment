'use strict'

const room = require('../model/room')

exports.addRoom = (user_ids,room_name,user_create) =>
new Promise((resolve,reject)=>{

    var newRoom = new room({
        room_name: room_name,
        users: user_ids,
        created_by: user_create
    });
    newRoom.save()

    .then(()=>resolve({status: 201,message: 'Room has been created successfully!',data:newRoom._id }))

    .catch(err => {
          reject({status: 500, message:  'Internal Server Error !'})
    })
})

exports.searchRoom = (user_ids,is_single) =>
new Promise((resolve,reject)=>{
    if(is_single == 1){
        room.find({ $and:[{users:{ $all: user_ids }},{users:{$size:2}}]})
        .then(rooms=> {
            if(rooms.length == 0){
                var newRoom = new room({
                    room_name: "The Room with only 2 people",
                    users: user_ids,
                    //created by will be filled later
                });
                newRoom.save()
                resolve({status: 201,message: 'New room has been created successfully',data:newRoom._id })
            } else {

				resolve({status: 201,message: 'This is the room you are looking for',data:rooms[0]._id })

            }
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    }else{
        reject({status: 500, message: 'Internal Server Errors !'})
    }
    
})

exports.getListGroups = (user_id) =>
new Promise((resolve,reject)=>{
    //get all group that constain user_id
    room.find({$and:[{users:{ $all: user_id }},{"users.2": { $exists: true }}]})
        .then(rooms=> {
            resolve({status: 201,message: 'List group of this user',data:rooms})
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))

})

exports.getListRoomOfUser = (user_id) =>
new Promise((resolve,reject)=>{
    //get all group that constain user_id
    room.find({$and:[{users:{ $all: user_id }},{users:{$size:2}}]})
        .then(rooms=> {
            resolve({status: 201,message: 'List room of this user',data:rooms})
        })
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))

})

exports.editUserInGroup = (updates,body_val) =>
new Promise((resolve,reject)=>{
    room.find({_id:body_val["_id"]})
        .then(rooms=> {
            let room = rooms[0]
            updates.forEach((update) => room[update] = body_val[update])
            return room.save()
        })
        .then((room)=>resolve({status: 201,message: 'room has been updated successfully',data:room}))
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))

})

exports.addLastMessage = (mess,room_id)=>
new Promise((resolve,reject)=>{
    room.find({_id:room_id})
    .then(rooms=>{
        if(rooms.length == 0){
            reject({status: 404, message: 'Room not found !'})
        }
        console.log(rooms[0])
        let room = rooms[0]
        room['last_message'] = mess
        return room.save()
    })
    .then((room)=>resolve({status: 201,message: 'room has been updated successfully',data:room}))
    .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
})
