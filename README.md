# wordspelling
    基于ifelse辅助开发插件的演示项目
    实现业务可视化。
    运行项目前请先安装插件

[插件地址](https://github.com/fclassroom/ifelse)

[单词拼读apk](./wordspelling.apk)

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

[使用介绍](https://github.com/fclassroom/ifelse/blob/master/README_CN.md)

```
Copyright 1999-2019 fclassroom Group.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at following link.

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```