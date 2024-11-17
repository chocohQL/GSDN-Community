class LocalStorage{
    static getParams(params:string){
        const result=localStorage.getItem(params)||null;
        if(!result)return null;
        return JSON.parse(result);
    }
    static setParams(params:string,value:any){
        localStorage.setItem(params,JSON.stringify(value));
    }
    static deleteParams(params:string){
        localStorage.removeItem(params);
    }
    static deleteAllParams(){
        localStorage.remove()
    }
}

export default LocalStorage;