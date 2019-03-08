package org.ifelse.points;

import org.ifelse.models.MDialogConfirmData;
import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;
import org.ifelse.vl.VL;
import org.ifelse.vldata.Event;

/*
{
						"key":"title",
						"name":"title"
					},
					{
						"key":"message",
						"name":"message"

					},
					{
						"key":"button_ok",
						"name":"ok text"

					},
					{
						"key":"button_cancel",
						"name":"cancel text"

					},
					{
						"key":"selectindex",
						"name":"select index"

					},
 */
public class DialogConfirm extends FlowPoint {

    final String k_title = "title";
    final String k_message = "message";
    final String k_button_ok = "button_ok";
    final String k_button_cancel = "button_cancel";
    final String k_selectindex = "selectindex";

    @Override
    public void run(final FlowBox flowBox) throws Exception {


        final MDialogConfirmData data = new MDialogConfirmData();

        data.title = getVarString(flowBox,k_title);
        data.msg = getVarString(flowBox,k_message);
        data.btn_ok = getVarString(flowBox,k_button_ok);
        data.btn_cancel = getVarString(flowBox,k_button_cancel);

        data.setOnCancelListener(new Runnable() {
            @Override
            public void run() {
                flowBox.setValue(getVarName(k_selectindex),1);
                flowBox.next();
            }
        });

        data.setOnOkListener(new Runnable() {
            @Override
            public void run() {
                flowBox.setValue(getVarName(k_selectindex),0);
                flowBox.next();
            }
        });

        data.cancelable = false;

        flowBox.runInMain(new Runnable() {
            @Override
            public void run() {
                VL.send(Event.S_DIALOG_CONFIRM,data);
            }
        });





    }
}
