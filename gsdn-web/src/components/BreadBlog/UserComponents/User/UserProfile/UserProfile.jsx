import {Card, Divider, Input, Form, Button, Upload,message} from "antd";
import { LeftOutlined, PlusOutlined } from "@ant-design/icons";
import {useNavigate} from "react-router-dom";
import CustomStorage from "@utils/StorageUtils/CustomStorage";
import UploadAvatar from "./UploadAvatar";
import TextArea from "antd/lib/input/TextArea";
import { updateUserMessage,get } from "@utils/RequestAxios/api/account";

const {User,pwd,Token} = CustomStorage.getAccount()

export default function UserProfile(props) {
	const navigator = useNavigate()
	let onFinish = async({avatar,username,intro}) => {
		// console.log(data)
		const token=localStorage.getItem('user');
		let message12;
		if (token) {
		    message12=JSON.parse(token);
		}
		const res=await updateUserMessage({
			avatar:avatar.length>0?avatar[0].response.data:User.avatar,
			username:username||message12.username,
			intro:intro||message12.intro
		})
		// console.log(res);
		
		if (res.code===200){
			message.success({content:'修改成功!',key:'loading'});
			localStorage.setItem('user',JSON.stringify(res.data));
			navigator('/user/home')
		}
		
	}

	const normFile = (e) => {
		if (Array.isArray(e)) {
			return e;
		}
		return e && e.fileList;
	}

	return (
		<div className='user-profile-container'>
			<div className='user-profile-header box-shadow'>
				<div className='user-profile-header-content' onClick={() => navigator('/user/home')}>
					<LeftOutlined/>
					<span>点击返回个人主页</span>
				</div>
			</div>
			<div className='user-profile-content-container box-shadow'>
				<Card title="个人资料">
					<div className='user-info-container'>
						<Form
							name="basic"
							onFinish={onFinish}
							labelCol={{ span: 4 }}
							wrapperCol={{ span: 20 }}
						>
							<Form.Item
								label="用户名"
								name="username"
								rules={[{ required: false, message: '请输入新的用户名!' },({getFieldValue }) => ({
									validator(_, value) {
										if (!value || getFieldValue('name') !== User) {
											return Promise.resolve();
										}
										return Promise.reject(new Error('请勿输入相同的用户名!'));
									},
								})]}
							>
								<Input placeholder='请输入新的姓名'/>
							</Form.Item>
							<Divider/>
							<Form.Item
								label="用户介绍"
								name="intro"
								rules={[{ required: false, message: '请输入密码!' },({getFieldValue }) => ({
									validator(_, value) {
										if (!value || getFieldValue('newpwd') !== pwd) {
											return Promise.resolve();
										}
										return Promise.reject(new Error('请勿输入相同密码!'));
									},
								})]}
							>
								<TextArea
									showCount
									maxLength={100}
									placeholder="开始你的介绍"
									style={{ height: 120, resize: 'none' }}
								/>
							</Form.Item>
							<Divider/>
							<Form.Item
								label='头像'
								getValueFromEvent={normFile}
								name='avatar'
							>
								{/* <UploadAvatar token={Token}/> */}
								<Upload 
									action="/file/uploadImg" 
									listType="picture-card"
									maxCount={1}
									// onPreview={handlePreview}
								>
									<button style={{ border: 0, background: 'none' }} type="button">
									<PlusOutlined />
									<div style={{ marginTop: 8 }}>Upload</div>
									</button>
								</Upload>
							</Form.Item>
							<Form.Item wrapperCol={{ offset: 4, span: 16 }}>
								<Button type="primary" htmlType="submit">
									更新信息
								</Button>
							</Form.Item>
						</Form>

					</div>
					{/* <div className='user-avatar-settings'>
						<UploadAvatar token={Token}/>
					</div> */}
				</Card>
			</div>

		</div>
	);
}
