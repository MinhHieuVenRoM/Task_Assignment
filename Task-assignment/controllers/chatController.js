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
      chat.find({room_id:room_id})

      .then(chats=> resolve(chats))
      .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
})
