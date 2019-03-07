# speakword
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
    调用了包括权限判断、单词导入、语音提示等功能，整个逻辑清晰可见

![avatar](./images/flow_init.jpg)

## 流程绘制
    如何绘制一个流程
    在流程管理界面 
