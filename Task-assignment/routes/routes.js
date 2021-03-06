'use strict'

//const auth = require('basic-auth')
const jwt = require('jsonwebtoken')
const validator = require('validator')

const user = require('../controllers/userController')
const password = require('../controllers/password')
const config = require('../config/config.json')
const project = require('../controllers/projectContronller')
const task = require('../controllers/taskController')
const authenticate  = require('../middleware/authenticate')
const attendance = require('../controllers/attendanceContronller')
const room =  require('../controllers/roomContronller')
const chat = require('../controllers/chatController')
const location = require('../controllers/locationContronller')

module.exports = router =>{
    router.get('/',(req,res)=>  res.sendFile('F://Android/Android-project/Task_Assignment_Git/Task-assignment' + '/index.html')) //res.end('Welcome to task assignment apps'))

    router.post('/authenticate',(req,res)=>{
        // const credentials =  auth(req)

        // if(!credentials){
        //     res.status(400).json({message: 'Invalid request!'})
        // }else{
			if(req.body.email =='' || req.body.pass =='' || !validator.isEmail(req.body.email)){
				res.status(400).json({success: false,message: 'Invalid Request !',data:{}});
			}
            user.loginUser(req.body.email, req.body.pass)
            .then(result =>{
				
				res.status(result.status).json({success: true ,message: result.message,data: result.data})
            })
            .catch(err=>res.status(err.status).json({success: false,message: err.message,data: {} }))
        //}
        
	})
	
    router.post('/users', (req, res) => {

		const name = req.body.name
		const email = req.body.email
		const password = req.body.password
		const sex = req.body.sex
		const phone = req.body.phone
		const dob = req.body.dob

		if (!name || !email || !password || !name.trim() || !email.trim() || !password.trim() || !validator.isEmail(email)) {

			res.status(400).json({success: false,message: 'Invalid Request !',data:{}});

		} else {

			user.registerUser(name, email, password,sex,phone,dob)

			.then(result => {

				res.setHeader('Location', '/users/'+email);
				res.status(result.status).json({ success:true,message:result.message,data: result.data })
			})

			.catch(err => res.status(err.status).json({success:false, message: err.message, data: {}}));
		}
	})

    router.get('/users/listUsers',authenticate,(req,res)=>{
		user.getListUser()
		.then(result => res.json({success: true,message:"Get users successfully",data: result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message,data: {} }));
	})

    router.get('/users/:id',authenticate ,(req,res) => {

			user.getProfile(req.params.id)

			.then(result => res.json({success: true,message:"Get profile user successfully",data: result}))

			.catch(err => res.status(err.status).json({success: false, message: err.message,data: [{}] }));

    })

	router.put('/users/edit_user',authenticate,(req,res)=>{
		const updates = Object.keys(req.body)
		const body_val = req.body

		user.editUserById(updates,body_val)

		.then(result => res.json({success:true,message:result.message ,data_users:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_users: {} }));
	})

	router.put('/users/ad/reset_password',authenticate,(req,res)=>{
		const email = req.body.email
		user.resetPassword(email)

		.then(result => res.json({success:true,message:result.message ,data_users:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_users: {} }));
	})

	router.post('/users/profile',authenticate,(req,res)=>{
		const email = req.body.email
		user.getProfile(email)
		.then(result => res.json({success: true,message:"get profile successfully",data: result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message,data: [{}] }));
	})
	//Project route

	router.get('/project',authenticate ,(req,res) => {
		const user = req.user
		project.getAllProjects(user)

		.then(result => res.json({success:true,message:"Get list project successfully",data_project:result}))

		.catch(err => res.status(err.status).json({success: false, message: err.message, data_project: {} }));
	})
	
	router.post('/project/get_user_project',authenticate ,(req,res) => {
		const user_id = req.body.user_id
		project.getUserProject(user_id)

		.then(result => res.json({success:true,message:"Get list user project successfully",data_project:result}))

		.catch(err => res.status(err.status).json({success: false, message: err.message, data_project: {} }));
	})

	router.get('/project/:id',authenticate ,(req,res) => {


		project.getProjectById(req.params.id)

		.then(result => res.json({success:true,message: "get project successfully",data_project:result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message,data_project: {}}));
	})
	
	router.post('/create_project',authenticate, (req, res) => {

		const name = req.body.name;
		const endDate = req.body.endDate;
		const user_id = req.user._id
		if (!name || !endDate) {

			res.status(400).json({success:false, message: 'Invalid Request !',data_project: {}});

		} else {

			project.createProject(name, endDate,user_id)

			.then(result => {

				res.setHeader('Location', '/create_project/'+name);
				res.status(result.status).json({success:true, message: result.message,data_project: result.data })
			})

			.catch(err => res.status(err.status).json({success:false, message: err.message,data_project: {} }));
		}
	})

	router.put('/project/edit_project',authenticate,(req,res)=>{
		const updates = Object.keys(req.body)
		const body_val = req.body
		project.editProjectById(updates,body_val)

		.then(result => res.json({success:true,message:result.message ,data_project:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_project: {} }));
	})
	//Task route
	router.put('/task/edit_task',authenticate,(req,res)=>{
		const updates = Object.keys(req.body)
		const body_val = req.body
		task.editTaskById(updates,body_val)

		.then(result => res.json({success:true,message:result.message ,data_task:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_task: {} }));
	})
	
	router.get('/task', authenticate,(req,res) => {

		task.getAllTask()

		.then(result => res.json({success:true,message:"successfully" ,data_task:result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_task: {} }));
	})

	router.post('/task/get_project_tasks', authenticate,(req,res) => {
		const project_id = req.body.project_id
		task.getTaskOfProject(project_id)

		.then(result => res.json({success:true,message:"successfully" ,data_task:result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_task: {} }));
	})

	router.post('/task/get_user_project_tasks', authenticate,(req,res) => {
		const project_id = req.body.project_id
		const user_id = req.body.user_id
		task.getUserProjectTask(user_id,project_id)

		.then(result => res.json({success:true,message:"successfully" ,data_task:result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_task: {} }));
	})

	router.post('/task/create_task',authenticate, (req, res) => {

		const name = req.body.name
		const end_date = req.body.end_date
		const project_id = req.body.project_id
		const content = req.body.content
		const created_by = req.user._id
		const user_id = req.body.user_id
		if (!name || !end_date) {

			res.status(400).json({success:false, message: 'Invalid Request !',data_task: {}});

		} else {

			task.createTask(name,content,project_id,user_id,end_date,created_by)

			.then(result => {

				res.setHeader('Location', '/create_task/'+name);
				res.status(result.status).json({success:true, message: result.message,data_task: result.data })
			})

			.catch(err => res.status(err.status).json({success:false, message: err.message,data_task: {} }));
		}
	})

	router.post('/task/get_task_by_date', authenticate,(req,res) => {
		const date = req.body.date
		task.getTaskByDate(date)

		.then(result => res.json({success:true,message:"successfully" ,data_task:result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_task: {} }));
	})

	router.post('/task/get_user_task_by_date', authenticate,(req,res) => {
		const date = req.body.date
		const user_id = req.user._id
		task.getUserTaskByDate(user_id,date)

		.then(result => res.json({success:true,message:"successfully" ,data_task:result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_task: {} }));
	})

	router.post('/task/ad/get_user_task_by_date', authenticate,(req,res) => {
		const date = req.body.date
		const user_id = req.body.user_id
		task.getUserTaskByDate(user_id,date)

		.then(result => res.json({success:true,message:"successfully" ,data_task:result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_task: {} }));
	})

	router.post('/task/get_user_task', authenticate,(req,res) => {
		const user_id = req.user._id
		task.getUserTask(user_id)

		.then(result => res.json({success:true,message:"successfully" ,data_task:result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_task: {} }));
	})
	router.post('/task/ad/get_user_task', authenticate,(req,res) => {
		const user_id = req.body.user_id
		task.getUserTask(user_id)

		.then(result => res.json({success:true,message:"successfully" ,data_task:result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_task: {} }));
	})
	router.post('/task/get_nearby_task', authenticate,(req,res) => {
		const user_id = req.body.user_id
		task.getNearbyTask(user_id)

		.then(result => res.json({success:true,message:"successfully" ,data_task:result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data_task: {} }));
	})
//attendance

	router.put('/attendance/check_in',authenticate,(req,res)=>{
		const user_id = req.user._id
		const date = req.body.date
		attendance.checkIn(user_id,date)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})

	router.put('/attendance/check_out',authenticate,(req,res)=>{
		const user_id = req.user._id
		const date = req.body.date
		attendance.checkOut(user_id,date)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
	router.post('/attendance/get_list_attendance', authenticate,(req,res) => {
		const date = req.body.date
		attendance.getListAttendanceByDate(date)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
	router.post('/attendance/ad/get_attendance_by_user', authenticate,(req,res) => {
		const user_id = req.body.user_id
		attendance.getListAttendanceByUser(user_id)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})

	router.post('/attendance/get_attendance_by_user', authenticate,(req,res) => {
		const user_id = req.user._id
		attendance.getListAttendanceByUser(user_id)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
	//month
	router.post('/attendance/ad/get_attendance_user_by_month', authenticate,(req,res) => {
		const user_id = req.body.user_id
		const date = req.body.date
		attendance.getListAttendanceUserByMonth(user_id,date)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})

	router.post('/attendance/ad/get_attendance_by_month', authenticate,(req,res) => {
		const date = req.body.date
		attendance.getListAttendanceByMonth(date)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})

	router.post('/attendance/get_attendance_user_by_month', authenticate,(req,res) => {
		const user_id = req.user._id
		const date = req.body.date
		attendance.getListAttendanceUserByMonth(user_id,date)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
	//year
	router.post('/attendance/get_attendance_user_by_year', authenticate,(req,res) => {
		const user_id = req.user._id
		const date = req.body.date
		attendance.getListAttendanceUserByYear(user_id,date)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})

	router.post('/attendance/ad/get_attendance_by_year', authenticate,(req,res) => {
		const date = req.body.date
		attendance.getListAttendanceByYear(date)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})

	router.post('/attendance/ad/get_attendance_user_by_year', authenticate,(req,res) => {
		const user_id = req.body.user_id
		const date = req.body.date
		attendance.getListAttendanceUserByYear(user_id,date)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})

	router.post('/attendance/check_attendance', authenticate,(req,res) => {
		const user_id = req.user._id
		const date = req.body.date
		attendance.checkAttendance(user_id,date)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
	//Room

	router.post('/room/create_room',authenticate, (req, res) => {

		const room_name = req.body.room_name
		const user_ids = req.body.users
		const user_create = req.body.user_create
		if (!room_name) {

			res.status(400).json({success:false, message: 'Invalid Request !',data_room: {}});

		} else {

			room.addRoom(user_ids,room_name,user_create)

			.then(result => {

				res.setHeader('Location', '/create_room/'+room_name);
				res.status(result.status).json({success:true, message: result.message,data_room: result.data })
			})

			.catch(err => res.status(err.status).json({success:false, message: err.message,data_room: {} }));
		}
	})

	router.post('/room/search_room',authenticate, (req, res) => {
		const user_ids = req.body.users
		const is_single =  req.body.is_single
		const room_name =  req.body.room_name
		room.searchRoom(user_ids,is_single,room_name)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
	router.post('/room/get_list_group',authenticate, (req, res) => {
		const user_id = req.body.user_id
		room.getListGroups(user_id)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
	router.post('/room/get_list_room',authenticate, (req, res) => {
		const user_id = req.body.user_id
		room.getListRoomOfUser(user_id)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
	router.put('/room/edit_room',authenticate,(req,res)=>{
		const updates = Object.keys(req.body)
		const body_val = req.body
		room.editUserInGroup(updates,body_val)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
	//no use this path
	router.put('/room/add_last_message',authenticate,(req,res)=>{
		const last_message = req.body.last_message
		const room_id = req.body.room_id
		room.addLastMessage(last_message,room_id)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
	router.post('/room/get_single_room_and_last_chat',authenticate, (req, res) => {
		const user_id = req.body.user_id
		user.getUsersAndLastMessage(user_id)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
	//chat
	router.post('/chat/get_history_chat',authenticate, (req, res) => {
		const room_id = req.body.room_id
		chat.getHistoryChat(room_id)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
	//location
	router.post('/location/get_location_by_name', (req, res) => {
		const name = req.body.name
		location.getLocation(name)

		.then(result => res.json({success:true,message:result.message ,data:result.data}))

		.catch(err => res.status(err.status).json({success:false, message: err.message, data: {} }));
	})
}

