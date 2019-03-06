package org.ifelse.points;



import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by dzb on 16/7/29.
 */
public class ObjectSet extends FlowPoint {

    final static String key_data = "data";
    final static String key_field = "field";
    final static String key_value = "value";

    @Override
    public void run(FlowBox flowBox) throws Exception {

        Object obj = flowBox.getValue(params.get(key_data));




        Field f=null;
        if( obj instanceof List)
        {

            List list = (List) obj;
            for(Object o : list) {

                if (f == null) {

                    f = o.getClass().getDeclaredField(params.get(key_field));
                    f.setAccessible(true);

                }
                setValue(o,f, flowBox.getValue(params.get(key_value))  );

            }

        }
        else{

            String[] fields = params.get(key_field).split(",");
            String[] values = params.get(key_value).split(",");

            for(int i=0;i<fields.length;i++) {

                f = obj.getClass().getDeclaredField(fields[i]);
                f.setAccessible(true);
                setValue(obj, f, flowBox.getValue( values[i] ) );

            }
        }


        flowBox.notifyFlowContinue();
    }

    void setValue(Object obj, Field f, Object value) throws IllegalAccessException {

        if( f.getType().equals(boolean.class) ){

            if( !(value instanceof  Boolean)){

                if( value instanceof  Integer ){
                    int v = (int)value;
                    f.setBoolean(obj,v>0);
                    return;
                }
                else if( value instanceof String){

                    boolean v = "1".equals(value) || "true".equals(value);
                    f.setBoolean(obj,v);
                    return;

                }

            }

        }


        else if( f.getType().equals(int.class) ){
            f.setInt(obj,Integer.parseInt(value.toString()));
        }
        else
            f.set(obj,value);
    }
}
