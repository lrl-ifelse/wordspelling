package org.ifelse.wordspelling;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import org.ifelse.models.MDialogConfirmData;
import org.ifelse.ui.DDialog;
import org.ifelse.vl.VL;
import org.ifelse.vldata.Event;

public class EventFilter {
    public static boolean filter(Event event, Object value) {

        switch (event){

            case S_DIALOG_CONFIRM:

                do_show_confirm(value);

                break;


        }

        return false;
    }

    private static void do_show_confirm(Object value) {

        final MDialogConfirmData data = (MDialogConfirmData) value;


        new DDialog.Builder(VL.getInstance().getForm())
                .setStyle(R.style.ConfirmDialog)
                .setContentView(R.layout.dialog_confirm)
                .setInitListener(new DDialog.ViewInitListener() {
                    @SuppressLint("ResourceAsColor")
                    @Override
                    public void ViewInit(View view) {



                        TextView msg = view.findViewById(R.id.text_msg);
                        if (!TextUtils.isEmpty(data.msg)) {
                            msg.setVisibility(View.VISIBLE);
                            msg.setText(data.msg);
                        } else {
                            msg.setVisibility(View.INVISIBLE);
                        }


                        TextView title = view.findViewById(R.id.text_title);
                        if (!TextUtils.isEmpty(data.title)) {
                            title.setVisibility(View.VISIBLE);

                            title.setText(data.title);
                        } else {
                            title.setVisibility(View.INVISIBLE);
                        }


                        boolean btn_ok_v = (data.btn_ok != null && data.btn_ok.length() > 0);
                        TextView btn_ok = view.findViewById(R.id.btn_ok);
                        btn_ok.setVisibility(btn_ok_v ? View.VISIBLE : View.GONE);
                        if (btn_ok_v) {
                            btn_ok.setText(data.btn_ok);
                        }

                        boolean btn_cancel_v = (data.btn_cancel != null && data.btn_cancel.length() > 0);
                        TextView btn_cancel = view.findViewById(R.id.btn_cancel);
                        btn_cancel.setVisibility(btn_cancel_v ? View.VISIBLE : View.GONE);
                        if (btn_cancel_v) {
                            btn_cancel.setText(data.btn_cancel);
                        }



                        if (!btn_ok_v && !btn_cancel_v) {
                            ((View) btn_ok.getParent()).setVisibility(View.GONE);
                        }




                    }
                })
                .setCancelable(data.cancelable)

                .addButtonListener(R.id.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (data.runnable_ok != null)
                            data.runnable_ok.run();
                        else if (data.event_ok != null && data.event_ok != Event.S_NONE)
                            VL.send(data.event_ok, data.value);

                    }
                })

                .addButtonListener(R.id.btn_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (data.runnable_cancel != null)
                            data.runnable_cancel.run();
                        else if (data.event_cancel != null && data.event_cancel != Event.S_NONE)
                            VL.send(data.event_cancel, data.value);

                    }
                })
                .create()
                .show();


    }
}
