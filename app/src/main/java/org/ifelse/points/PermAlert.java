package org.ifelse.points;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;
import org.ifelse.vl.Form;
import org.ifelse.vl.VL;

import java.util.List;

/**
 * Created by dizhanbin on 16/10/29.
 <property name="permissioin" title="权限" type="0" args="" value=""/>
 */

public class PermAlert extends FlowPoint {


    final String key_permissioin = "permissioin";

    @Override
    public void run(final FlowBox flowBox) throws Exception {





        Object obj;

        if( flowBox.isVar(params.get(key_permissioin)) )
        {
            obj =  flowBox.getValue(params.get(key_permissioin));

        }
        else
            obj = params.get(key_permissioin);



        final Form activity = VL.getInstance().getForm();

        activity.setOnPermissionListener(new Form.OnPermissionListener() {
            @Override
            public void onPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


                flowBox.notifyFlowContinue();

            }
        });


        if( obj instanceof  String ) {

            String permission = flowBox.getVarString(params.get(key_permissioin));
            final String[] parray = permission.split(",");


            flowBox.runInMain(new Thread(){

                @Override
                public void run() {


                        ActivityCompat.requestPermissions(activity,
                                parray,
                                100
                        );




                }
            });


        }
        else if( obj instanceof List)
        {


            List ps = (List) obj;

            String[] parray = new String[ps.size()];

            for(int i=0;i<ps.size();i++)
            {
                parray[i] = ps.get(i).toString();
            }

            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    parray[0])) {
                ActivityCompat.requestPermissions(activity,
                        parray,
                        100
                );
            }

        }





        //flowBox.notifyFlowContinue();
    }
}
