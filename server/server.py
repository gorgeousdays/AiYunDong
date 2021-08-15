from flask import Flask, render_template, jsonify, request,Response
import time
import os
import base64
import json
import cv2

from getdata import get_alldata
from evaluate import getTimes,evaluate,fastDtw,alignedList
from jsoncreator import video_to_json
from saveresult import savereuslt
from savepicturefromvideo import cul_max_distance,find_max_distance_index,savePictureFromVideo

app = Flask(__name__)
#UPLOAD_FOLDER = 'C:\\Users\\Hp\\Desktop\\sever-posedemo\\uploadfile'
UPLOAD_FOLDER = './uploadfile'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
app.config['JSON_AS_ASCII'] = False #支持中文
basedir = os.path.abspath(os.path.dirname(__file__))
ALLOWED_EXTENSIONS = set(['txt', 'png', 'jpg', 'xls', 'JPG', 'PNG', 'xlsx', 'gif', 'GIF','mp4','avi'])


def judgeaction(new_filename,savePic):
    file_dir = os.path.join(basedir, app.config['UPLOAD_FOLDER'])
    video_to_json(os.path.join(file_dir, new_filename),"noimage","./jsonfile/now/",2)

    stanard_anglist=get_alldata("./jsonfile/standard/")#获取标准动作
    now_anglist,now_xy_list,W=get_alldata("./jsonfile/now/",isRunning=True)#获取当前动作信息
    times = getTimes(now_xy_list,0,"test")#计算运动个数 由于采用头部计数 传入0 头部的X即为各个List的第一个值 
    proposal=evaluate(stanard_anglist,now_anglist,int(times))
    print(proposal)
    print(times)
    print(W)
    if(savePic==True):
        path = fastDtw(now_anglist, stanard_anglist) #当前序列在前，标准序列在后
        runningNowList, runningStandardList = alignedList(now_anglist, stanard_anglist, path)
        #接下来求runningNowList中与runningStandardList差距最大的一帧，记录并匹配返回
        max_i,_,__=cul_max_distance(runningNowList,runningStandardList)#获取runningNowList中最大差距帧

        pic_index,_=find_max_distance_index(now_anglist,runningNowList,max_i)#获取now_anglist最大差距帧，即与runningNowList最接近的一帧
        savePictureFromVideo('uploadfile/'+new_filename,'imgfile/',new_filename,pic_index,False)#保存该帧

    return proposal,times,W

# 用于判断文件后缀
def allowed_file(filename):
    return '.' in filename and filename.rsplit('.', 1)[1] in ALLOWED_EXTENSIONS


# 上传文件
@app.route('/api/upload', methods=['POST'], strict_slashes=False)
def api_upload():
    file_dir = os.path.join(basedir, app.config['UPLOAD_FOLDER'])
    if not os.path.exists(file_dir):
        os.makedirs(file_dir)
    f = request.files['myfile']  # 从表单的file字段获取文件，myfile为该表单的name值
    if f and allowed_file(f.filename):  # 判断是否是允许上传的文件类型
        fname = f.filename
        print(fname)
        ext = fname.rsplit('.', 1)[1]  # 获取文件后缀
        unix_time = int(time.time())
        new_filename = str(unix_time) + '.' + ext  # 修改了上传的文件名
        f.save(os.path.join(file_dir, new_filename))  # 保存文件到upload目录
        print(new_filename)
        #token = base64.b64encode(new_filename)
        #print(token)

        #return jsonify({"errno": 0, "errmsg": "上传成功", "token": token})
        return jsonify({"errno": 0, "errmsg": "上传成功"})
    else:
        return jsonify({"errno": 1001, "errmsg": "上传失败"})


#上传文件并且判断结果
@app.route('/api/uploadandjudge', methods=['POST'], strict_slashes=False)
def api_uploadandjudge():
    file_dir = os.path.join(basedir, app.config['UPLOAD_FOLDER'])
    if not os.path.exists(file_dir):
        os.makedirs(file_dir)
    f = request.files['myfile']  # 从表单的file字段获取文件，myfile为该表单的name值
    if f and allowed_file(f.filename):  # 判断是否是允许上传的文件类型
        fname = f.filename
        print(fname)
        ext = fname.rsplit('.', 1)[1]  # 获取文件后缀
        unix_time = int(time.time())
        new_filename = str(unix_time) + '.' + ext  # 修改了上传的文件名
        f.save(os.path.join(file_dir, new_filename))  # 保存文件到upload目录
        print(new_filename)

        #添加动作代码
        proposal,times,W=judgeaction(new_filename,True)
        #token = base64.b64encode(new_filename)
        #print(token)
        savereuslt("./message/result.json",times,proposal,W,new_filename)#保存结果至json文件
        #return jsonify({"errno": 0, "errmsg": "上传成功", "token": token})
        return jsonify({"errno": 0, "errmsg": "上传成功","proposal":proposal,"times":times,"W":W})
    else:
        return jsonify({"errno": 1001, "errmsg": "上传失败"})


@app.route('/')
def index():
    # 这里是demo，实际这么返回响应字符串是不规范的
    return '<h1>Hello World!</h1>'

@app.route('/api/getmessage/<json_name>',methods=['GET'])
def api_getmessage(json_name):
    filename =  json_name + '.json'
    directory = "./message"  #json文件所在的目录路径
    try:
        with open(directory + '/' + filename,'r',encoding='utf8') as f:
            jsonStr = json.load(f)
            print(jsonStr)
            print("----------")
            print(json.dumps(jsonStr))
            return jsonify(jsonStr)
    except Exception as e:
        return jsonify({"message": "{}".format(e),"resultlist":"null"})

@app.route("/image/<image_name>",methods=['GET'])
def get_image(image_name):
    resp = Response(open("imgfile/"+image_name, 'rb'), mimetype="image/jpeg")
    return resp

if __name__ == '__main__':
    app.run(host='0.0.0.0')