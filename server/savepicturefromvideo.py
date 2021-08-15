import copy
from scipy.spatial.distance import euclidean
import math
import cv2
import matplotlib.pyplot as plt
import time

from getdata import get_alldata
from evaluate import getTimes,evaluate,fastDtw,alignedList
from jsoncreator import video_to_json
from saveresult import savereuslt



#保存视频中差距最大的图像至指定位置
def cul_max_distance(runningNowList,runningStandardList):#计算两个序列中最大的差距值的i值 即runningNowList[i][j]与 runningStandardList[i][j]相差最大
    #只判断上半身的差距
    max=0
    max_i=0
    max_j=0
    for i in range(len(runningNowList)):
        if i<2:continue   #不计算前两个，可能还没准备好
        if runningNowList[i][1]>0:continue #表示手臂未大弯曲状态
        temp=0
        for j in range(4):#len(runningNowList[0])-4 上半身四个点
            temp+=abs(runningNowList[i][j]-runningStandardList[i][j])
        if temp>max:
            max=temp
            max_i=i
            max_j=j
    return max_i,max_j,max#返回最大差距信息


def find_max_distance_index(now_anglist,runningNowList,max_i):#找到原视频中相差最大的一帧的下标，传入全列表,动态规整后列表和最大值所在的位置i，j
    num=2#这里的num即为opencv分割视频的num
    
    min=100
    min_i=0
    for i in range(len(now_anglist)):
        #由于runningNowList可能为填充值，所以找最小差距帧
        temp=0
        for j in range(len(runningNowList[0])):
            if runningNowList[max_i][0]==now_anglist[i][0]:
                return i*num#有相等的就直接返回
            if now_anglist[i][1]>0:
                continue
            temp+=abs(runningNowList[max_i][j]-now_anglist[i][j])
            if(temp<min):
                min=temp
                min_i=i
    return min_i*2,min #返回对应帧，与差距

def savePictureFromVideo(video_path,save_path,save_name,pic_index,ifdraw):#传入视频位置、保存位置、保存名称、截取图片下标、是否画点
    cap = cv2.VideoCapture(video_path)
    num_frame = cap.get(cv2.CAP_PROP_FRAME_COUNT)#获取视频总帧数
    count=-1
    while 1:
        ret, frame = cap.read()
        count+=1
        if pic_index== count:
            #plt.figure()
            #plt.imshow(frame)
            #plt.show()
            if ifdraw==True:
                #画图代码见draw，还需读对应josn结合一下，有点麻烦，懒的写了
                pass
            cv2.imwrite(save_path+save_name+'.jpg', frame) 
        if not ret:
            break
            
if __name__ == "__main__":
    stanard_anglist=get_alldata("./jsonfile/standard/")#获取标准动作
    now_anglist,now_xy_list,W=get_alldata("./jsonfile/now/",isRunning=True)#获取当前动作信息
    times = getTimes(now_xy_list,0,"test")#计算运动个数 由于采用头部计数 传入0 头部的X即为各个List的第一个值 
    #proposal=evaluate(stanard_anglist,now_anglist,int(times))
    path = fastDtw(now_anglist, stanard_anglist) #当前序列在前，标准序列在后
    runningNowList, runningStandardList = alignedList(now_anglist, stanard_anglist, path)
   
    #接下来求runningNowList中与runningStandardList差距最大的一帧，记录并匹配返回
    max_i,_,__=cul_max_distance(runningNowList,runningStandardList)#获取runningNowList中最大差距帧

    pic_index,_=find_max_distance_index(now_anglist,runningNowList,max_i)#获取now_anglist最大差距帧，即与runningNowList最接近的一帧
    savePictureFromVideo('chinning.avi','imgfile/',str(int(time.time())),pic_index,False)#保存该帧