'use strict'

const chat = require('../model/chat')

exports.saveChat = (message,user_id,room_id) =>
    new Promise((resolve,reject)=>{

        var chatMessage = new chat({
            message: message,
            user_id: user_id,
            room_id: room_id,
        });
        chatMessage.save()

        .then(()=>resolve({status: 201,message: 'Chat has been created successfully!',data:chat }))

        .catch(err => {
              reject({status: 500, message:  'Internal Server Error !'})
        })
    })

exports.getHistoryChat = room_id =>
    new Promise((resolve,reject)=>{
      //chat.find({room_id:room_id})
      chat.aggregate([
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
        // Now filter the condition on task_detail

        { "$project": { "_id":1,
                        "message":1,
                        "user_id" :1,
                        "room_id":1,
                        "user_detail.name" : 1
                         } },
        { "$match": { "room_id":  room_id} }
        ])

      .then(chats=> resolve({status: 201,message: 'Chat has been loaded successfully!',data:chats}))
      .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
})
