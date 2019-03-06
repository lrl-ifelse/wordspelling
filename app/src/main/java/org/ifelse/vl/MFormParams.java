package org.ifelse.vl;

import android.os.Build;
import android.support.annotation.IdRes;
import android.view.View;

/**
 * Created by dizhanbin on 18/7/14.
 */

public class MFormParams {


    //"share" default transitionName


    public  @IdRes
    int share_view_id;
    public Object value;
    public int anim_exit;
    public int anim_exter;
    public View share_view;


    public MFormParams(View view, Object value){

        share_view = view;
        this.value = value;

    }


    public MFormParams( int view, Object value){

        share_view_id = view;
        this.value = value;

    }
    public MFormParams(){


    }

    public void setAnim(int enter,int exit){

        anim_exter = enter;
        anim_exit = exit;

    }

    public void setShareView(View view){

        this.share_view = view;
        if( view != null ){

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if( view.getTransitionName() == null ){

                    view.setTransitionName("share");
                }
            }

        }


    }


}
