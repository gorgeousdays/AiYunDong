import math
from fastdtw import fastdtw
import pandas as pd
import copy
import matplotlib.pyplot as plt
from scipy.spatial.distance import euclidean

#个数计算
def getTimes(pointXYlist, part, name):
    number = 0
    pointYList = []
    for i in range(len(pointXYlist)):
        pointYList.append(pointXYlist[i][part].y)  # 常规的动作一般为克服重力做功

    #print("Y坐标")
    #print(featureYP)
    # 绘图
    df = pd.DataFrame(pointYList, index=range(len(pointYList)))
    df.plot()
    #存图
    #ax = df.plot()
    #fig = ax.get_figure()
    #if os.path.exists(path+name) == False:
    #   os.mkdir(path+name)  # 创建目录
    #fig.savefig(path+ name + "/" + str(part) + ".jpg")
    #plt.close()
    mid = (max(pointYList) + min(pointYList)) / 2
    #print("中值")
    #print(mid)
    for i in range(len(pointYList) - 1):
        if (pointYList[i] - mid) * (pointYList[i + 1] - mid) < 0:  # 存在一个交点
            number = number + 1
    return number / 2









#动态规整，返回规整的结果
def fastDtw(nowList, standardList):
    distance, path = fastdtw(nowList, standardList, dist=euclidean)
    return path

def cal_avg(testList, start, end):
    result = [0, 0, 0, 0, 0, 0, 0, 0]#由于存在8个角度值，result的size为8
    div = end - start + 1
    while start <= end:
        for i in range(8):
            result[i] += testList[start][i]
        start += 1
    for i in range(8):
        result[i] = result[i] / div
    return result

# 根据path对两个list对齐
def alignedList(nowList, standardList, path):
    runningNowList = []
    runningStandardList = []
    ix = 0
    ixNext = 1
    while ixNext < len(path):
        if path[ix][0] != path[ixNext][0] and path[ix][1] != path[ixNext][1]:
            runningNowList.append(nowList[path[ix][0]])
            runningStandardList.append(standardList[path[ix][1]])
            ix += 1
            ixNext += 1
        elif path[ix][0] == path[ixNext][0]:
            #next一直往下取，直至找到存在不重复的
            while ixNext < len(path) and path[ix][0] == path[ixNext][0]:
                ixNext += 1
            #当前的List即填充对应值，标准列表由于存在跳变，计算中间的平均值
            runningNowList.append(nowList[path[ix][0]])
            runningStandardList.append(cal_avg(standardList, path[ix][1], path[ixNext - 1][1]))
            ix = ixNext
            ixNext += 1
        elif path[ix][1] == path[ixNext][1]: 
            while ixNext < len(path) and path[ix][1] == path[ixNext][1]:
                ixNext += 1
            runningNowList.append(cal_avg(nowList, path[ix][0], path[ixNext - 1][0]))
            runningStandardList.append(standardList[path[ix][1]])
            ix = ixNext
            ixNext += 1
    return runningNowList, runningStandardList

# 得到测试与标准间的距离用于画图
def getDisList(runningList, standardList):
    result = []
    for i in range(len(runningList)):
        result.append(getDis(runningList[i], standardList[i]))
    return result


# 计算当前帧与标准帧的差别
def getDis(now, standard):
    result = 0
    for i in range(len(now)):
        result += (now[i] - standard[i]) ** 2
    return math.sqrt(result)

#绘图代码
def DrawChart(times,nowList, standardList, path):
    # nowList与standardList长度不等，二者进行对齐
    runningNowList, runningStandardList = alignedList(nowList, standardList, path)
    # 开始画图
    disList = getDisList(runningNowList, runningStandardList)#得到全部帧与标准帧差别的List
    df = pd.DataFrame(disList, index=range(len(runningStandardList)))#=range(len(disList))
    df.plot()
    ax = df.plot()
    fig = ax.get_figure()
    fig.savefig("evaluationResult.jpg")
    plt.close()
    # 部位评价
    partEvaluation = [0, 0, 0, 0, 0, 0, 0, 0]
    for i in range(len(runningNowList[0])):
        for j in range(len(runningNowList)):
            partEvaluation[i] += abs(runningNowList[j][i] - runningStandardList[j][i])
    return partEvaluation

#评价函数 传入标准动作角度时间序列和当前动作时间序列以及当前动作执行次数 返回评价与建议
def evaluate(standardList, nowList, times):
    #先将标准动作拷贝运动次数便
    temp = copy.deepcopy(standardList)
    for i in range(times - 1):
        for j in range(len(temp)):
            standardList.append(temp[j])
    path = fastDtw(nowList, standardList) #当前序列在前，标准序列在后
    proposal = DrawChart(times, nowList, standardList, path)  # 返回建议
    return proposal


if __name__ == '__main__':
    #主函数

    #输入标准和当前两个json序列  输出评价矩阵 表示各个角的偏差
    stanard_anglist=get_alldata("./standard/")#获取标准动作
    now_anglist,now_xy_list,W=get_alldata("./now/",isRunning=True)#获取当前动作信息
    times = getTimes(now_xy_list,0,"test")#计算运动个数 由于采用头部计数 传入0 头部的X即为各个List的第一个值
    #print(times)
    proposal=evaluate(stanard_anglist,now_anglist,int(times))
    print(proposal)