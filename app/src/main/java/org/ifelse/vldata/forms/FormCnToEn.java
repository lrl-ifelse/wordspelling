package org.ifelse.vldata.forms;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import org.ifelse.models.MCode;
import org.ifelse.models.MWord;
import org.ifelse.wordspelling.R;
import org.ifelse.vl.Bind;
import org.ifelse.vl.Form;
import org.ifelse.vldata.Event;

/*
根据中文拼单词
*/
public class FormCnToEn extends Form{


    @Bind(R.id.btn_start)
    TextView btn_start;

    @Bind(R.id.text_cn)
    TextView text_cn;

    @Bind(R.id.text_en)
    TextView text_en;



    @Override
    public void onClick(View view){

        switch (view.getId()){


            case R.id.btn_start:
                
                sendMessage(Event.B_WORD_TEST_CN_TO_EN);

                btn_start.setEnabled(false);

                btn_start.setText("单词拼写");

                break;


        }
    }


    @Override
    public boolean onMessage(Event event, Object value) {

        switch (event){

            case B_WORD_SPEAKING:

                MWord word = (MWord) value;
                text_cn.setText(word.cn);
                return true;

        }
        return false;

    }

    @Override
    public void onStateChanged(FormState fs, Object value) {

        //log("%-12s taskid:%d taskroot:%b", fs, getTaskId(), isTaskRoot());

        switch (fs) {

            case FS_CREATE:

                setContentView(R.layout.formcntoen);
                setTitle("单词拼写");
                break;
            case FS_PAUSE:
                cancel(Event.B_WORD_TEST_CN_TO_EN);
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
                code.code = "FormCnToEn\nsendMessage(Event.B_WORD_TEST_CN_TO_EN);\n /iedata/flows/b_word_test.ie";
                code.drawable = R.mipmap.flow_test;

                sendMessage(Event.F_CODE_EXAMPLE,code);

                return true;

        }
        return false;

    }
}