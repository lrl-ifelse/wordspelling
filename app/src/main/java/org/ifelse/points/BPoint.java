package org.ifelse.points;

import org.ifelse.models.MWord;
import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;
import org.ifelse.vl.SQLO;

import java.lang.reflect.Method;

public class BPoint extends FlowPoint {

    final static String key_method = "method";
    final static String key_in = "in";
    final static String key_out = "out";


    @Override
    public void run(FlowBox flowBox) throws Exception {

        String method_str = getVarName(key_method);


        Method method = getClass().getMethod(method_str,FlowBox.class);
        if( method != null ) {

            flowBox.log( "%s.%s",params.get("descript"),method_str);
            method.invoke(this, flowBox);
        }
        else
            flowBox.log("未实现业务方法 BPoint.%s",method_str);

    }

    public void splite_word(final  FlowBox flowBox){

        String instr = getVarString(flowBox,key_in);

        StringBuffer stringBuffer = new StringBuffer();


        for(int i=0;i<instr.length();i++){

            char c = instr.charAt(i);
            if( c != ' ')
                stringBuffer.append(instr.charAt(i)).append(',');

        }

        flowBox.log("splite Char :%s",stringBuffer.toString());

        setValue(flowBox,key_out,stringBuffer.toString());

        flowBox.next();
    }



    public void get_word_by_seq(final  FlowBox flowBox){


        Object order = getVarValue(flowBox,key_in);

        try {

            MWord mWord = SQLO.selectOne(MWord.class,"seq="+order);
            flowBox.setValue(getVarName(key_out),mWord);

        } catch (Exception e) {
            e.printStackTrace();
        }
        flowBox.next();


    }


}
