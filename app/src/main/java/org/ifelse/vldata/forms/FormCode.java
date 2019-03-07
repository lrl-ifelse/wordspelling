package org.ifelse.vldata.forms;

import android.view.View;
import android.widget.TextView;
import com.github.chrisbanes.photoview.PhotoView;
import org.ifelse.models.MCode;
import org.ifelse.wordspelling.R;
import org.ifelse.vl.Bind;
import org.ifelse.vl.Form;
import org.ifelse.vldata.Event;

/*
示例代码
*/
public class FormCode extends Form{

    @Bind(R.id.photo_view)
    PhotoView photo_view;

    @Bind(R.id.txt_code)
    TextView txt_code;




    @Override
    public void onClick(View view){

        switch (view.getId()){


        }
    }


    @Override
    public boolean onMessage(Event event, Object value) {

        switch (event){


        }
        return false;

    }

    MCode code;

    @Override
    public void onStateChanged(FormState fs, Object value) {

        //log("%-12s taskid:%d taskroot:%b", fs, getTaskId(), isTaskRoot());

        switch (fs) {

            case FS_VALUE:

                code = (MCode) value;

                break;

            case FS_CREATE:

                setContentView(R.layout.formcode);

                break;
            case FS_UI_INIT:

                photo_view.setImageResource(code.drawable);

                txt_code.setText(code.code);

                break;

        }
    }


}