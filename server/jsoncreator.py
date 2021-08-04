#视频到json文件如下
import json
from tencentcloud.common import credential
from tencentcloud.common.profile.client_profile import ClientProfile
from tencentcloud.common.profile.http_profile import HttpProfile
from tencentcloud.common.exception.tencent_cloud_sdk_exception import TencentCloudSDKException
from tencentcloud.bda.v20200324 import bda_client, models
import base64
import cv2
import os

#实现np图片转base64 并加base64头
def image_to_base64(image_np):
 
    image = cv2.imencode('.jpg',image_np)[1]
    image_code = str(base64.b64encode(image))[2:-1]
    image_base64="data:image/jpg;base64,"+image_code
    #print(image_code[0:100])
    #print(image_base64[0:100])
 
    return image_base64

#输入图片的base64和json文件名 在指定文件位置生成json
def jsoncreator(img_base64,i,output):
    try: 
        #修改cred 腾讯云api官网申请
        cred = credential.Credential("", "") 
        httpProfile = HttpProfile()
        httpProfile.endpoint = "bda.tencentcloudapi.com"

        clientProfile = ClientProfile()
        clientProfile.httpProfile = httpProfile
        client = bda_client.BdaClient(cred, "", clientProfile) 

        req = models.DetectBodyJointsRequest()
        params = {
            #"Url":test
            #"Url":"testapi.jpg"
            #"Url":"https://z3.ax1x.com/2021/07/22/WDTrdS.png"
            "Image":img_base64
        }
        req.from_json_string(json.dumps(params))   
        resp = client.DetectBodyJoints(req)
        #print(resp)
        resp_string=resp.to_json_string()
        #print(resp_string)
        resp_dict = json.loads(resp_string)
        #print(resp_dict)
        with open(output+str(i)+".json",'w',encoding='utf-8') as json_file:
            json.dump(resp_dict,json_file,ensure_ascii=False)

    except TencentCloudSDKException as err: 
            print(err) 

#视频生成json文件 输入视频位置 输出图片文件夹 以num帧为单位处理一次 json文件将存储在standard文件夹下
def video_to_json(i_video, o_video,o_json,num):#input_video_path; output_video_path; output_json_path; working_num
    cap = cv2.VideoCapture(i_video)
    num_frame = cap.get(cv2.CAP_PROP_FRAME_COUNT)#获取视频总帧数
    expand_name = '.jpg'
    if not cap.isOpened():
        print("Please check the path.")
    cnt = 0 #当前帧
    count = 0  #处理帧数
    while 1:
        ret, frame = cap.read()
        cnt += 1
        if cnt % num == 0:
            count += 1
            #取消注释即写入图片
            #cv2.imwrite(os.path.join(o_video, str(count) + expand_name), frame)
            img_base64=image_to_base64(frame)
            jsoncreator(img_base64,count,o_json)
        if not ret:
            break
            
if __name__ == "__main__":         
    video_to_json('standard_chinning.avi',"C:/Users/Hp/Desktop/posedemo/image/","./standard/",2)