#保存结果与建议至json文件
import time
import json

from evaluate import evaluate,getTimes
from getdata import get_alldata


def get_advice(proposal,times):
    standard_proposal=[x/times for x in proposal]
    if max(standard_proposal)<1.5:
        #print("动作标准，请继续保持")
        return "动作标准，请继续保持"
    elif  max(standard_proposal[0:4])>2.5:
        print("手弯曲程度较大，未完全上单杠")
    elif max(standard_proposal[0:4])>1.5:
        print("基本标准,建议手不要弯曲")
    elif max(standard_proposal[4:8])>1.5:
        print("基本标准,腿伸直一点更好")

def savereuslt(filename,times,proposal,W,new_filename):
    with open(filename,'r',encoding='utf8')as fp:
        result_json_data = json.load(fp)
        #print(result_json_data)
    new_result={}
    new_result.update(id=str(123),
                      time=str(time.asctime(time.localtime(time.time()) )),
                      num=str(times),
                      advice=get_advice(proposal,times),
                      energy=str(W),
                      img=str(new_filename))

    result_json_data["resultlist"].append(new_result)

    with open(filename,"w",encoding='utf-8') as f:
        json.dump(result_json_data,f,ensure_ascii=False)
        #print("加载入文件完成...")

if __name__ == '__main__':
    stanard_anglist=get_alldata("./jsonfile/standard/")#获取标准动作
    now_anglist,now_xy_list,W=get_alldata("./jsonfile/now/",isRunning=True)#获取当前动作信息
    times = getTimes(now_xy_list,0,"test")#计算运动个数 由于采用头部计数 传入0 头部的X即为各个List的第一个值
    proposal=evaluate(stanard_anglist,now_anglist,int(times))
    savereuslt("./result/result.json",times,proposal,W)