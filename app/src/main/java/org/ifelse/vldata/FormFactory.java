package org.ifelse.vldata;

import org.ifelse.vldata.forms.*;
import org.ifelse.vldata.*;

public class FormFactory{

    public static Class getFormClass(Event event){

        switch(event){

           case F_TEST_CN_TO_EN : return FormCnToEn.class;//根据中文拼单词
           case F_ORDER : return FormOrder.class;//顺序朗读
           case F_CODE_EXAMPLE : return FormCode.class;//示例代码
           case F_MAIN : return FormMain.class;//首页
           case F_FLASH : return FormFlash.class;//引导页

        }
        return null;
    }




}

