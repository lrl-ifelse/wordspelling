package org.ifelse.vldata;

import org.ifelse.speakword.R;
import org.ifelse.vl.MFlow;

public class FlowBoxs{

    public static int getBox(Event event){
        switch(event){
         case B_WORD_ORDER : return R.raw.b_word_order;//单词顺序读
         case B_WORD_TEST_CN_TO_EN : return R.raw.b_word_test;//考单词汉译英
         case B_INIT : return R.raw.b_init;//系统初始化
         case B_WORD_TEST_EN_TO_CN : return R.raw.b_word_test_e2c;//英译汉

      }
        return 0;
    }


    public static MFlow getBoxInfo(Event event) {
        switch(event)
        {
           case B_WORD_ORDER : return new MFlow(48,"单词顺序读","b_word_order");
           case B_WORD_TEST_CN_TO_EN : return new MFlow(49,"考单词汉译英","b_word_test");
           case B_INIT : return new MFlow(6,"系统初始化","b_init");
           case B_WORD_TEST_EN_TO_CN : return new MFlow(97,"英译汉","b_word_test_e2c");


        }
        return null;
    }

}

