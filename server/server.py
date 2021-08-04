from flask import Flask, render_template, jsonify, request
import time
import os
import base64

from getdata import get_alldata
from evaluate import getTimes,evaluate
from jsoncreator import video_to_json

app = Flask(__name__)
UPLOAD_FOLDER = 'C:\\Users\\Hp\\Desktop\\sever-posedemo\\uploadfile'
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
basedir = os.path.abspath(os.path.dirname(__file__))
ALLOWED_EXTENSIONS = set(['txt', 'png', 'jpg', 'xls', 'JPG', 'PNG', 'xlsx', 'gif', 'GIF','mp4','avi'])


def judgeaction(new_filename):
    file_dir = os.path.join(basedir, app.config['UPLOAD_FOLDER'])
    video_to_json(os.path.join(file_dir, new_filename),"noimage","./jsonfile/now/",2)

    stanard_anglist=get_alldata("./jsonfile/standard/")#获取标准动作
    now_anglist,now_xy_list,W=get_alldata("./jsonfile/now/",isRunning=True)#获取当前动作信息
    times = getTimes(now_xy_list,0,"test")#计算运动个数 由于采用头部计数 传入0 头部的X即为各个List的第一个值 
    proposal=evaluate(stanard_anglist,now_anglist,int(times))
    print(proposal)
    print(times)
    return proposal,times


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


@app.route('/api/uploadandjudge', methods=['POST'], strict_slashes=False)
def api_uploadandjudeg():
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
        proposal,times=judgeaction(new_filename)
        #token = base64.b64encode(new_filename)
        #print(token)

        #return jsonify({"errno": 0, "errmsg": "上传成功", "token": token})
        return jsonify({"errno": 0, "errmsg": "上传成功","proposal":proposal,"times":times})
    else:
        return jsonify({"errno": 1001, "errmsg": "上传失败"})


@app.route('/')
def index():
    # 这里是demo，实际这么返回响应字符串是不规范的
    return '<h1>Hello World!</h1>'

if __name__ == '__main__':
    app.run(host='0.0.0.0')