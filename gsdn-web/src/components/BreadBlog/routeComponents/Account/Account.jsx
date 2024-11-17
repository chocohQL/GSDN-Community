/**
 * @description 用户注册与登录
 */
import {useEffect, useState} from 'react';
import {useNavigate} from "react-router-dom";
import {Layout, message} from "antd";
import background from '../../../../static/bg.jpg'
// import CustomStorage from "@utils/StorageUtils/CustomStorage";
// import UserOperationRequest from "@utils/RequestUtils/Operation/UserOperationRequest";
import { isLogin,isRegister,getUserMessage,isLoginStatus } from '@utils/RequestAxios/api/account';
const {Content} = Layout
export default function Login(){
	const navigator = useNavigate()

	useEffect(() => {
		isLoginPre();
	},[])

	let [userInfo,setUserInfo] = useState({username:'', password:''})
	let [active,setActive] = useState("login")

	let changeState = state => {
		return () => {
			setActive(state)
			setUserInfo({username:'',password:''})
		}
	}

	const isLoginPre=async ()=>{
		let check = await isLoginStatus()
		if (check.code==200&&check.data){
			message.success('您已经登录!');
			navigator('/user/home')
		}
	}

	const setTokens=async function(token){
		localStorage.setItem('token',token);
		const res=await getUserMessage();
		// console.log('meaasge',res,);
		if(res.code==200){
			localStorage.setItem('user',JSON.stringify(res.data));
		}else{
			message.error(res.msg);
		}
	}

	let handleForm = () =>{
		return async () => {

			let {username,password} = userInfo
			if (!(username && password)){
				message.warn("请输入用户名和密码")
				return;
			}
			let result;
			message.loading({content:'请稍后...',key:'loading',duration:10})

			switch (active){
				case "login" :
					// 进行登录接口请求
					result = await isLogin({username,password})
					if (result.code==200){
						message.success({content:'登录成功!',key:'loading'})
						setTokens(result.data);
						navigator('/user/home')
					}else {
						message.error({content:result.msg,key:'loading'})
					}
					break;
				case "register" :
					// 进行注册接口请求
					result = await isRegister({username,password})	
					if (result.code==200){
						message.success({content:"注册成功!",key:'loading'})
						setTokens(result.data);
						navigator('/user/home')
					}else {
						message.error({content:result.msg,key:'loading'})
					}
					break;
			}

		}

	}
	return(
		<div className="User_Container shangshou">
			<Layout className='layout'>
				<Content>
					<div className='site-layout-content'>
							<div className="container">
								<img src={background}/>
								<div className="panel">
									<div className="content login">
										<div className="switch">
											<span onClick={changeState('login')} className={`${(active === "login" && "active") || ""}`}>登陆</span>
											<span>/</span>
											<span onClick={changeState('register')} className={`${(active === "register" && "active") || ""}`}>注册</span>
										</div>
										<div className='form'>
											<div>
												<div className="input">
													<input maxLength={20} type="text" onKeyPress={({code}) => code === 'Enter' && handleForm()()} value={userInfo.username} className={`${(userInfo.username && "hasValue") || ""}`} onChange={({target:{value}}) => setUserInfo({...userInfo,username:value}) } />
													<label>用户名</label>
												</div>
												<div className="input">
													<input maxLength={20} type="password" onKeyPress={({code}) => code === 'Enter' && handleForm()()}  value={userInfo.password} className={`${(userInfo.password && "hasValue") || ""}`} onChange={({target:{value}})=> setUserInfo({...userInfo,password:value})}/>
													<label>密码</label>
												</div>
											</div>
											<button type="button" onClick={handleForm()}>{(active === "login" && "登录") || "注册"}</button>
											<button type="button" onClick={() => navigator('/')}>回到主页</button>
										</div>
									</div>
								</div>
							</div>
						</div>
				</Content>
			</Layout>
		</div>
	)
}