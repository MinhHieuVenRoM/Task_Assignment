'use strict'

//const auth = require('basic-auth')
const jwt = require('jsonwebtoken')

const user = require('../controllers/userController')
const password = require('../controllers/password')
const config = require('../config/config.json')
const project = require('../controllers/projectContronller')
const task = require('../controllers/taskController')
const authenticate  = require('../middleware/authenticate')

module.exports = router =>{
    router.get('/',(req,res)=> res.end('Welcome to task assignment apps'))

    router.post('/authenticate',(req,res)=>{
        // const credentials =  auth(req)

        // if(!credentials){
        //     res.status(400).json({message: 'Invalid request!'})
        // }else{
            user.loginUser(req.body.email, req.body.pass)
            .then(result =>{
				
				res.status(result.status).json({success: true ,message: result.message,data: result.data})
            })
            .catch(err=>res.status(err.status).json({success: false,message: err.message,data: {} }))
        //}
        
	})
	
    router.post('/users',authenticate, (req, res) => {

		const name = req.body.name
		const email = req.body.email
		const password = req.body.password
		const sex = req.body.sex
		const phone = req.body.phone
		const dob = req.body.dob

		if (!name || !email || !password || !name.trim() || !email.trim() || !password.trim()) {

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
		.then(result => res.json({success: true,message:"get users successfully",data: result}))

		.catch(err => res.status(err.status).json({success:false, message: err.message,data: {} }));
	})

    router.get('/users/:id',authenticate ,(req,res) => {

			user.getProfile(req.params.id)

			.then(result => res.json({success: true,message:"get profile user successfully",data: result}))

			.catch(err => res.status(err.status).json({success: false, message: err.message,data: {} }));

    })
    
    router.put('/users/:id', (req,res) => {

		if (checkToken(req)) {

			const oldPassword = req.body.password;
			const newPassword = req.body.newPassword;

			if (!oldPassword || !newPassword || !oldPassword.trim() || !newPassword.trim()) {

				res.status(400).json({ success:false, message: 'Invalid Request !' ,data: {}});

			} else {

				password.changePassword(req.params.id, oldPassword, newPassword)

				.then(result => res.status(result.status).json({ message: result.message }))

				.catch(err => res.status(err.status).json({ message: err.message }));

			}
		} else {

			res.status(401).json({ message: 'Invalid Token !' });
		}
    })
    
    router.post('/users/:id/password', (req,res) => {

		const email = req.params.id;
		const token = req.body.token;
		const newPassword = req.body.password;

		if (!token || !newPassword || !token.trim() || !newPassword.trim()) {

			password.resetPasswordInit(email)

			.then(result => res.status(result.status).json({ message: result.message }))

			.catch(err => res.status(err.status).json({ message: err.message }));

		} else {

			password.resetPasswordFinish(email, token, newPassword)

			.then(result => res.status(result.status).json({ message: result.message }))

			.catch(err => res.status(err.status).json({ message: err.message }));
		}
	});


    function checkToken(req) {

		const token = req.headers['x-access-token'];

		if (token) {

			try {

  				var decoded = jwt.verify(token, config.secret);

  				return decoded.message === req.params.id;

			} catch(err) {

				return false;
			}

		} else {

			return false;
		}
	}

	//Project route

	router.get('/project',authenticate ,(req,res) => {
		const user = req.user
		project.getAllProjects(user)

		.then(result => res.json({success:true,message:"Get list project successfully",data_project:result}))

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
	//Task route
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

}