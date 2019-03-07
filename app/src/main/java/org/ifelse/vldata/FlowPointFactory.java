package org.ifelse.vldata;

import org.ifelse.vl.FlowPoint;

public class FlowPointFactory{

    public static FlowPoint createFlowPoint(int pid){

        switch(pid){
           case 100101 : return new org.ifelse.points.Start();//Start
           case 100102 : return new org.ifelse.points.PointNone();//注释
           case 100103 : return new org.ifelse.points.EventSend();//发送事件
           case 400101 : return new org.ifelse.points.StringUtil();//字符运算
           case 400102 : return new org.ifelse.points.StringUtil2();//字符操作
           case 500101 : return new org.ifelse.points.ObjectRead();//对象取值
           case 500102 : return new org.ifelse.points.ObjectSet();//对象赋值
           case 600101 : return new org.ifelse.points.UserData();//本地数据
           case 600102 : return new org.ifelse.points.UserSaveData();//保存数据
           case 700101 : return new org.ifelse.points.PermUtil();//权限判断
           case 700102 : return new org.ifelse.points.PermAlert();//权限请求
           case 800100 : return new org.ifelse.points.TTSInit();//百度语音初始化
           case 800101 : return new org.ifelse.points.TTS();//朗读
           case 800104 : return new org.ifelse.points.Asr();//语音识别
           case 800102 : return new org.ifelse.points.BWordImport();//单词导入
           case 800103 : return new org.ifelse.points.BPoint();//业务节点

      }
        return null;
    }




}

