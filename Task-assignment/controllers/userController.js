'use strict'

const user = require('../model/users')
const bcrypt = require('bcryptjs')
const jwt = require('jsonwebtoken')
const config = require('../config/config.json')


exports.loginUser = (email,password) => 
 new Promise((resolve,reject)=>{
    user.find({email:email,status:1})

        .then(users=>{
            if(users.length == 0){
                reject({ status: 404, message: 'User Not Found !' });
            } else {

				return users[0];

			}
        })
        .then(user=>{
            const hashed_password = user.hashed_password;
            if(bcrypt.compareSync(password,hashed_password)){
                const token = jwt.sign({ _id: user.id },config.secret)
                user.token = token
                user.save()
                resolve({status: 200, message: "login successfully", data: user})

            }else{
                reject({status: 401, message: 'invalid Credentials!'})
            }
        })

        .catch(err=>reject({status: 500, message: 'Internal Server Error !'})
        )
    })

exports.getProfile = email =>
    new Promise((resolve,reject)=>{
        //set 0 to exclude _id
        user.find({email: email},{_id: 0})

        .then(users=> resolve(users[0]))
        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })
exports.registerUser = (name,email,password,sex,phone,dob) =>
    new Promise((resolve,reject)=>{
        const salt = bcrypt.genSaltSync(10);
        const hash  = bcrypt.hashSync(password,salt)

        const newUser = new user({
            name: name,
            email: email.toLowerCase(),
            hashed_password: hash,
            dob: dob,
            phone: phone,
            sex: sex,
            created_at: new Date()
        });

        newUser.save()
        //delete newUser.hashed_password

        .then(()=>resolve({status: 201,message: 'User registered successfully!',data: newUser}))

        .catch(err => {
            if(err.code == 11000){
                reject({status: 409, message: 'User already registered!'})

            }else{
                reject({status: 500, message:  'Internal Server Error !'})
            }
        })
    })

exports.getListUser = () =>
    new Promise((resolve,reject)=>{
        user.find({},{hashed_password:0,token:0})

        .then(users=>resolve(users))

        .catch(err=> reject({status: 500, message: 'Internal Server Error !'}))
    })
