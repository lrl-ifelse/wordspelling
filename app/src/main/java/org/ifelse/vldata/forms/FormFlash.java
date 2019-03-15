package org.ifelse.vldata.forms;

import org.ifelse.wordspelling.R;
import org.ifelse.vl.Form;
import org.ifelse.vldata.Event;

/*
引导页
*/
public class FormFlash extends Form{


    private boolean inited;

    @Override
    public boolean onMessage(Event event, Object value) {

        switch (event){

            case B_INIT_SUCCESS:
                setDestoryOnPause(true);
                sendMessage(Event.F_MAIN,null);
                return true;

        }


        return false;
    }


    public void onStateChanged(FormState fs, Object value) {

        log("%-12s taskid:%d taskroot:%b", fs, getTaskId(), isTaskRoot());

        switch (fs) {

            case FS_CREATE:

                setContentView(R.layout.formflash);

                break;

            case FS_UI_INIT:


                break;
            case FS_RESUME:
                if( !inited ) {
                    sendMessage(Event.B_INIT);
                    inited = false;
                }
                break;
            case FS_PAUSE:

                if( isDestoryOnPause() )
                    finish();
                break;


        }

    }


}