package org.ifelse.models;


import android.text.TextUtils;


import org.ifelse.vldata.Event;

/**
 * Created by dizhanbin on 18/2/2.
 * 确认对话框数据
 */

public class MDialogConfirmData {
    public String title = "";
    public String msg;
    public boolean cancelable = true;
    public Event event_ok = Event.S_NONE;
    public Event event_cancel = Event.S_NONE;

    public String btn_ok;
    public String btn_cancel;

    public boolean btn_ok_default = true;
    public boolean btn_cancel_default = true;

    public int tag;

    public Object value;


    public MDialogConfirmData(String msg) {

        this.msg = msg;

    }

    public MDialogConfirmData() {

    }


    public Runnable runnable_ok;

    public void setOnOkListener(Runnable runnable) {
        runnable_ok = runnable;

    }

    public Runnable runnable_cancel;

    public void setOnCancelListener(Runnable runnable) {

        runnable_cancel = runnable;

    }


}
