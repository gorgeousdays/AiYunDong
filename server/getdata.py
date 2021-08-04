#整合上述代码 实现封装
import os
import json
import numpy as np
import glob
import time
import math

class standardPoint:
    def __init__(self, x = None, y = None):
        self.x = x
        self.y = y
		
    def __sub__(self, other):
        return standardPoint(self.x - other.x, self.y - other.y)
        
    def norm(self):
        return math.sqrt(self.x ** 2 + self.y ** 2)
        
    def cos_points(self, other):#求两线的cos角，即两个肢体部位
        if self.norm()*other.norm() != 0:
            return (self.x * other.x + self.y * other.y) / (self.norm()*other.norm())
        else:
            return 0

#对一个动作（即一帧）进行标准化处理 输入一帧的XY序列 返回标准化后结果
def standardAction(Action):
    standardList = []
    coorcnt = 0
    x = 0.0
    y = 0.0

    for cor in Action:
        if 0 == coorcnt:
            x = cor
            coorcnt += 1
            continue
        if 1 == coorcnt:
            y = cor
            coorcnt =0
            standardList.append(standardPoint(x, y))
            continue
    return standardList

#输入一个标准化动作 返回该动作中蕴含的有效角度信息
def cul_angle(aStdAction):
    #print(aStdAction[24].x)
    Ang_List = []
    body = []
    # 部位向量值
    body.append(aStdAction[0] - aStdAction[1])  # 头颈
    body.append(aStdAction[5] - aStdAction[2])  # 肩膀
    body.append(aStdAction[2] - aStdAction[3])  # 右上臂
    body.append(aStdAction[3] - aStdAction[4])  # 右前臂    
    body.append(aStdAction[5] - aStdAction[6])  # 左上臂
    body.append(aStdAction[6] - aStdAction[7])  # 左前臂  
    
    body.append(aStdAction[8] - aStdAction[11])  # 髋部
    body.append(aStdAction[8] - aStdAction[9])  # 右大腿
    body.append(aStdAction[9] - aStdAction[10])  # 右小腿
    body.append(aStdAction[11] - aStdAction[12])  # 左大腿
    body.append(aStdAction[12] - aStdAction[13])  # 左小腿
    # 计算部位之间角度
    Ang_List.append(body[2].cos_points(body[1]))  # 右肩臂
    Ang_List.append(body[3].cos_points(body[2]))  # 右肘
    Ang_List.append(body[4].cos_points(body[1]))  # 左肩臂
    Ang_List.append(body[5].cos_points(body[4]))  # 左肘
    
    Ang_List.append(body[7].cos_points(body[6]))  # 右胯
    Ang_List.append(body[8].cos_points(body[7]))  # 右膝
    Ang_List.append(body[9].cos_points(body[6]))  # 左胯
    Ang_List.append(body[10].cos_points(body[9]))  # 左膝

    return Ang_List

#计算做功
def getCountW(meanposls, judgeState):
    W = 0
    framecnt = len(meanposls)
    # 计算做功
    for t in range(1, framecnt):
        W += np.abs(meanposls[t] - meanposls[t - 1])
    return W

#输入全部帧 返回每一帧的XY标准化序列集合  即[[][]......]
def get_all_XY(orglist):
    standard_XY_list = []
    for aAction in orglist:
        standard_XY_list.append(standardAction(aAction))
    return standard_XY_list

#输入全部帧 返回每一帧的有效角度信息
def get_all_angle(orglist):
    Angle_List = []
    for aAction in orglist:
        #print(aAction)
        Angle_List.append(cul_angle(standardAction(aAction)))
    return  Angle_List

#为了实现读取到的文件按照时间排序
def search_all_files_return_by_time_reversed(path, reverse=False):
    return sorted(glob.glob(os.path.join(path,'*')), key=lambda x: time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(os.path.getctime(x))), reverse=reverse)


# 现在采用头部计数，如果用其它部位计数，则修改judgeState并写判断语句
# isRunning用来判断调用这个函数的json是否是要用于评价的，如果不是，则不需要getCount得到个数和能量，类似重用
def get_alldata(runningPath, judgeState="", isRunning=False):
    origList = []#存储全部帧的点集合
    countList = []
    files = search_all_files_return_by_time_reversed(runningPath)
    for file in files:
        if not os.path.isdir(file) and file.endswith(".json"):
            with open(file, mode="r",encoding='utf-8') as fd:
                temp_dict = json.load(fd)
                temp_list=[]
                for i in range(14):
                    temp_list.append(temp_dict["BodyJointsResults"][0]["BodyJoints"][i]["X"])
                    temp_list.append(temp_dict["BodyJointsResults"][0]["BodyJoints"][i]["Y"])
                origList.append(temp_list)
                countList.append(temp_list[0])#通过头部值来计数
    if isRunning:
        W = getCountW(countList, judgeState)#计算做功
        return get_all_angle(origList), get_all_XY(origList), W
    else:
        #print(origList[0])
        #print(standardAction(origList[0])[0].x)
        #print(countList)
        return get_all_angle(origList)  # 返回各帧的部位角度组
    
    
if __name__ == "__main__":
    stanard_anglist=get_alldata("./standard/")