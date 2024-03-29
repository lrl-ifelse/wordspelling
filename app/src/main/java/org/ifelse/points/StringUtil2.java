package org.ifelse.points;

import org.ifelse.models.MWord;
import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;
import org.ifelse.vl.SQLO;

import java.lang.reflect.Method;

public class StringUtil2 extends FlowPoint {

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

    public void trim(final  FlowBox flowBox){

        String instr = getVarString(flowBox,key_in);

        StringBuffer stringBuffer = new StringBuffer();


        for(int i=0;i<instr.length();i++){

            char c = instr.charAt(i);
            if( c != ' ')
                stringBuffer.append(instr.charAt(i));

        }

        setValue(flowBox,key_out,stringBuffer.toString());

        flowBox.next();
    }



}
