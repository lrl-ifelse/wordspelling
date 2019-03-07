package org.ifelse.points;

import android.content.SharedPreferences;
import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;


/**
 * Created by dzb on 16/9/8.
 *
 *
 * 可 ， 相隔
 <property name="key" title="key" type="0"  args="" value="" />
 <property name="value" title="数据" type="0"  args="" value="" />
 <property name="descript" title="描述" type="0" args="" value="保存本地数据" />
 *
 */
public class UserSaveData extends FlowPoint {


    final static String key_key = "key";
    final static String key_value = "value";

    @Override
    public void run(FlowBox flowBox) throws Exception {

        String[] keys = this.params.get(key_key).split(",");
        String[] values = this.params.get(key_value).split(",");

        SharedPreferences sp = UserData.getPreference(flowBox.getContext());

        SharedPreferences.Editor editor = sp.edit();
        for(int i=0;i<keys.length;i++)
        {
           editor.putString( keys[i], flowBox.getVarString(  values[i] ) );
        }
        editor.commit();
        flowBox.next();
    }
}
