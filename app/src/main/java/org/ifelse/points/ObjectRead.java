package org.ifelse.points;


import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;

import java.lang.reflect.Field;

public class ObjectRead extends FlowPoint {

    final static String key_data = "data";
    final static String key_field = "field";
    final static String key_value = "value";

    @Override
    public void run(FlowBox flowBox) throws Exception {

        Object obj = getVarValue(flowBox,key_data);
        String field = getVarName(key_field);
        String value = getVarName(key_value);

        String[] fields = field.split(",");
        String[] values = value.split(",");

        if( obj != null ) {
            Class classz = obj.getClass();
            for(int i=0;i<fields.length;i++){

                Field ff = classz.getDeclaredField(fields[i]);
                ff.setAccessible(true);
                Object vobj = ff.get(obj);

                flowBox.setValue(values[i],vobj);

            }
        }

        flowBox.next();
    }
}
