const jwt = require('jsonwebtoken')
const User = require('../model/users')
const config = require('../config/config.json')

const authenticate = async (req,res,next) => {
    try {
        const token = req.header('Authorization').replace('Bearer', '').trim()
        
        const decoded  = jwt.verify(token, config.secret)
       
        const user  = await User.findOne({ _id:decoded._id, 'token': token})

        if(!user){
            throw new Error()
        }
        req.token = token
        req.user = user
        next()
    } catch (error) {
        console.log(error)
        res.status(401).send({sucess:false,message:'Please authenticate!',data:{}})
    }
}

module.exports = authenticate