# wordspelling
    基于ifelse辅助开发插件的演示项目
    实现业务可视化。
    运行项目前请先安装插件

[插件地址](https://github.com/fclassroom/ifelse)


## 目录结构

    /iedata         插件数据目录
        /flows      流程目录
        /icons      图标目录
        /script     插件调用脚本目录
        /template   模版目录
        Event.ie    事件配置数据文件
        Flows.ie    流程管理文件
        Forms.ie    页面管理文件
        points.json 节点配置文件
        project.json 插件配置文件
    src/..../vldata  模版生成文件
    src/..../vl      框架文件
    src/..../points  通用节点实现目录


## 功能描述

    项目实现了初始化、读单词、考单词 三个功能
    页面中只有简单交互逻辑、大部分逻辑包含在流程中。

    例如：
    FormFlash.sendMessage(Event.B_INIT);
    调用了包括权限判断、单词导入、语音提示等功能，整个逻辑清晰可见，没有业务逻辑代码。

![avatar](./images/flow_init.jpg)

## 流程绘制
    如何绘制一个流程
    在流程管理界面 

## log 流程执行输出了统一格式的内容

    W/系统初始化-225921130: point(5)[0] 起始点 :Start
    W/系统初始化-225921130: Start params:null
    W/系统初始化-225921130: point(11)[1] 权限判断 :PermUtil
    W/系统初始化-225921130: point(27)[2] 百度语音初始化 :TTSInit
    W/系统初始化-225921130: [$init_baidu_result] is OK :true
    W/系统初始化-225921130: point(8)[3] 朗读-初始化提示 :TTS
    W/系统初始化-225921130: tts onSynthesizeStart:0
    W/系统初始化-225921130: tts onSpeechFinish:0
    W/系统初始化-225921130: point(38)[4] 本地数据 :UserData
    W/系统初始化-225921130: [$inited] is OK :false
    W/系统初始化-225921130: point(32)[5] 单词导入 :BWordImport
    W/系统初始化-225921130: [$init_word] is OK :true
    W/系统初始化-225921130: point(39)[6] 保存数据 :UserSaveData
    W/系统初始化-225921130: point(34)[7] 朗读-初始化成功 :TTS
    W/系统初始化-225921130: tts onSynthesizeStart:0
    W/系统初始化-225921130: tts onSpeechFinish:0
    W/系统初始化-225921130: point(45)[8] 初始化成功 :EventSend
    W/系统初始化-225921130: Event:B_INIT_SUCCESS
    W/系统初始化-225921130: flow is over. duration:7784


    拷贝 log 后 选择对应流程 ctrl + i 导入log
    显示执行顺序加快调试时间
![avatar](./images/flow_log.jpg)
