import myAxios from "../myAxios";

//登录情况
interface LoginType{
    username:string;
    password:string;
}
export const isLogin=function(target:LoginType){
    return myAxios({
        method:'post',
        url:'/user/login',
        data:target
    })
}

//注册情况
export const isRegister=function(target:LoginType){
    return myAxios({
        method:'post',
        url:'/user/register',
        data:target
    })
}

//退出登录
export const isLogOut=function(){
    return myAxios({
        method:'post',
        url:'/user/logout'
    })
}

//获取用户信息
export const getUserMessage=function(){
    return myAxios({
        url:"/user/me",
        method:'get',
    })
} 

// /判断是否登录
export const isLoginStatus=function(){
    return myAxios({
        url:"/user/isLogin",
        method:'get',
    })
}


//更新用户消息
interface UserageMessage{
    avatar:string;
    username:string;
    intro:string;
}

export const updateUserMessage=function(target:UserageMessage){
    return myAxios({
        url:"/user/modify/userInfo",
        method:'post',
        data:target
    })
}