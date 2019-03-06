package org.ifelse.vldata;

/* cretae by template/Event.tpl */

public enum Event{


    B_INIT,//初始化
    B_INIT_SUCCESS,//数据初始化成功
    B_WORD_ORDER,//单词顺序读
    B_WORD_SPEAKING,//正在朗读
    B_WORD_TEST_CN_TO_EN,//汉子拼单词
    B_WORD_TEST_EN_TO_CN,//英译汗
    F_CODE_EXAMPLE,//代码示例
    F_FLASH,//启动页
    F_MAIN,//主页面
    F_ORDER,//循序朗读
    F_TEST_CN_TO_EN,//汉译英页面

    S_NONE;

    public static Event getEvent(int eid){

        switch(eid){

            case 6 : return Event.B_INIT;
            case 47 : return Event.B_INIT_SUCCESS;
            case 48 : return Event.B_WORD_ORDER;
            case 43 : return Event.B_WORD_SPEAKING;
            case 49 : return Event.B_WORD_TEST_CN_TO_EN;
            case 97 : return Event.B_WORD_TEST_EN_TO_CN;
            case 99 : return Event.F_CODE_EXAMPLE;
            case 5 : return Event.F_FLASH;
            case 1 : return Event.F_MAIN;
            case 2 : return Event.F_ORDER;
            case 74 : return Event.F_TEST_CN_TO_EN;

        }
        return Event.S_NONE;


    }

}