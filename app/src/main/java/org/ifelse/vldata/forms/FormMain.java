package org.ifelse.vldata.forms;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import org.ifelse.models.MCode;
import org.ifelse.speakword.R;
import org.ifelse.vl.Form;
import org.ifelse.vl.MFormParams;
import org.ifelse.vldata.Event;

/*
首页
*/
public class FormMain extends Form{


    @Override
    public void onClick(View view){


        switch (view.getId()){

            case R.id.btn_order: {
                MFormParams mFormParams = new MFormParams();

                mFormParams.share_view = view;

                sendMessage(Event.F_ORDER, mFormParams);
            }
                break;
            case R.id.btn_test: {

                MFormParams mFormParams = new MFormParams();

                mFormParams.share_view = view;

                sendMessage(Event.F_TEST_CN_TO_EN, mFormParams);


            }
                break;

        }


    }

    @Override
    public void onStateChanged(FormState fs, Object value) {

        switch (fs) {

            case FS_CREATE:
                setContentView(R.layout.formmain);
                break;
            case FS_UI_INIT:


                break;



        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()){

            case R.id.menu_item_code:


                MCode code = new MCode();
                code.code = "FormFlash\nsendMessage(Event.B_INIT);\n /iedata/flows/b_init.ie";
                code.drawable = R.mipmap.flow_init;

                sendMessage(Event.F_CODE_EXAMPLE,code);

                return true;

        }
        return false;

    }
}