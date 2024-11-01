import axios, { AxiosRequestConfig } from "axios";
// import { baseURL } from "./baseURL";
const baseURL='http://localhost:3000'

interface customConfigType{
    loadingParams?:boolean;
    cancelParams?:boolean;
}

export interface MyResponseType {
    code?: number;
    status?: number;
    message?: string;
    data: any;
}

//请求队列
const pendingQueue:any={};
//loading状态
const LoadingInstance={
    _target:document.getElementById('spin-loading-mask') as HTMLElement,
    _count:0
}

//获取请求队列中的某个请求
function getPending(config:AxiosRequestConfig){
    const {url,method,data,params}=config;
    return [url,method,data,params].join('&')
}

//添加请求到队列中
function addPending(config:AxiosRequestConfig){
    const pendingKey=getPending(config);
    const controller=new AbortController();
    config.signal=controller.signal;
    if(!pendingQueue[pendingKey]){
        pendingQueue[pendingKey]=controller
    }
}

function removePending(config:AxiosRequestConfig){
    const pendingKey=getPending(config);
    if(pendingQueue[pendingKey]){
        const controller=pendingQueue[pendingKey];
        // console.log(controller);
        controller.abort(pendingKey);
        delete pendingQueue[pendingKey]
    }
}

//展示loading
function showLoading(){
    if(LoadingInstance._count==0){
        LoadingInstance._target.style.display='block'
    }
    LoadingInstance._count++
}

//隐藏loading
function hideLoading(){
    LoadingInstance._count--
    if(LoadingInstance._count<=0){
        LoadingInstance._target.style.display='none'
    }
}

async function myAxios(options:AxiosRequestConfig,customConfig?:customConfigType) {
    const service=axios.create({
        baseURL,
        timeout:60000,
        withCredentials:true
    })

    const _customConfig:customConfigType=Object.assign({
        loadingParams:false,
        cancelParams:false
    },customConfig)

    //请求拦截器
    service.interceptors.request.use((config:any)=>{
        const token = 'Bearer ' + window.localStorage.getItem('token');
        if (token) config.headers.authorization = token;
        addPending(config);
        _customConfig.cancelParams&&removePending(config);
        _customConfig.loadingParams&&showLoading();
        return config
    },error=>{
        _customConfig.loadingParams&&hideLoading();
        return Promise.reject(error)
    })

    //响应拦截器
    service.interceptors.response.use(responce=>{
        removePending(responce.config);
        _customConfig.loadingParams&&hideLoading();
        return responce
    },error=>{
        error.config&&removePending(error.config);
        _customConfig.loadingParams&&hideLoading();
        return Promise.reject(error)
    })

    try {
        const res = await service.request<MyResponseType>(options);
        return res.data;
      } catch (err: any) {
        const message: string = err.message || '请求失败';
        // console.log(message); // 失败消息提示
        return {
          code: -1 as -1,
          status: -1 as -1,
          message,
          data: null,
        };
      }
}


export default myAxios