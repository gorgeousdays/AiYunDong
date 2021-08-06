# Server-code

## jsoncreator

输入视频，调用腾讯的api输出json序列，还可以输出视频分隔后的图片

#### 函数

```python
def video_to_json(i_video, o_video,o_json,num):#input_video_path; output_video_path; output_json_path; working_num
```

参数：视频地址、输出图片地址、输出json序列地址，以num帧为单位处理一次

无返回值

#### 调用案例

```python
video_to_json('standard_chinning.avi',"C:/Users/Hp/Desktop/posedemo/image/","./standard/",2)
```

目前代码取消输出图片

## GetData

从json序列中获取各帧的角度信息、各帧XY信息(采用类存储)、做功W（即某一部位的移动距离）

#### 函数

```python
# 现在采用头部计数，如果用其它部位计数，则修改judgeState并写判断语句
# isRunning用来判断调用这个函数的json是否是要用于评价的，如果不是，则不需要getCount得到个数和能量，类似重用
def get_alldata(runningPath, judgeState="", isRunning=False):
    '''
    ......
    ......
    '''
    if isRunning:
        W = getCountW(countList, judgeState)#计算做功
        return get_all_angle(origList), get_all_XY(origList), W
    else:
        return get_all_angle(origList)  # 返回各帧的部位角度组
```

参数：json序列地址

返回值：返回各帧的部位角度组、各帧XY信息、做功W

#### 调用案例

```python
stanard_anglist=get_alldata("./standard/")#获取标准动作
now_anglist,now_xy_list,W=get_alldata("./now/",isRunning=True)#获取当前动作信息
```

## Evaluate

主要有两个接口 

* getTimes：通过GetData获取到的运行点XY信息以及评判部位

  返回运动次数

  逻辑即计算运动的中心（以评判部位的最高与最低计算），根据两帧过中点即运动一次

* evaluate：输入标准动作序列的角度，当前动作序列的角度以及运动次数

  返回各个角与标准动作的差（即建议）。包括作图函数保存评价结果与服务器，对客户端无影响。

  逻辑即将标准动作序列复制运动次数变，对标准运动序列以及当前运动序列做动态规整。

输入标准序列的json和当前序列的json

#### 函数

```python
def getTimes(pointXYlist, part, name):#输入XY序列、评判部位、存储图像的名字（以注释存图代码，随便传参即可）
    '''
    ......
    ......
    '''
    return number / 2 #返回运动次数


def evaluate(standardList, nowList, times):#标准序列角度、当前序列角度、当前运动次数
    return proposal#返回一个list,记录各个角的偏差信息，一共8个角
```

#### 调用案例

```
    #输入标准和当前两个json序列  输出评价矩阵 表示各个角的偏差
    stanard_anglist=get_alldata("./standard/")#获取标准动作
    now_anglist,now_xy_list,W=get_alldata("./now/",isRunning=True)#获取当前动作信息
    times = getTimes(now_xy_list,0,"test")#计算运动个数 由于采用头部计数 传入0 头部的X即为各个List的第一个值
    #print(times)
    proposal=evaluate(stanard_anglist,now_anglist,int(times))
    print(proposal)
```

## SaveResult

根据Evaluate上述计算得到的W,proposal,tiems三个量，计算相关量并保存到json文件夹中

无返回值，只有对结果json文件的操作

#### 函数

```python
def savereuslt(filename,times,proposal,W):
    '''
    读文件
    '''
    new_result.update(id=str(result_json_data["resultlist"][-1]["id"]+1),
                      time=str(int(time.time())),
                      num=str(times),
                      advice=get_advice(proposal,times),
                      energy=str(W))
    '''
    写文件
    '''
    
def get_advice(proposal,times):
```

#### 调用案例

```python
from evaluate import evaluate,getTimes
from getdata import get_alldata

stanard_anglist=get_alldata("./jsonfile/standard/")#获取标准动作
now_anglist,now_xy_list,W=get_alldata("./jsonfile/now/",isRunning=True)#获取当前动作信息
times = getTimes(now_xy_list,0,"test")#计算运动个数 由于采用头部计数 传入0 头部的X即为各个List的第一个值
proposal=evaluate(stanard_anglist,now_anglist,int(times))

savereuslt("result.json",times,proposal,W)       
```



## Draw

获取单个json文件对其进行绘图，测试用代码，不属于主程序，后续可考虑绘图视频

```python
draw_json(path)#根据对应json文件地址绘图
```

观察绘图结果发现腾讯姿态检测api表现较差，对多目标易出错。

