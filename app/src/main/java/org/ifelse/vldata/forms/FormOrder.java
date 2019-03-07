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
import org.ifelse.vl.VL;
import org.ifelse.vldata.Event;

/*
顺序朗读
*/
public class FormOrder extends Form{


    @Bind(R.id.btn_start)
    TextView btn_start;

    @Bind(R.id.text_word)
    TextView text_word;

    @Bind(R.id.text_cn)
    TextView text_cn;



    boolean canpaused = false;

    @Override
    public void onClick(View view){

        switch (view.getId()){


            case R.id.btn_start: {




                if( !canpaused ) {
                    view.setEnabled(false);
                    sendMessage(Event.B_WORD_ORDER);

                }
                else {

                    VL.cancel(Event.B_WORD_ORDER);
                    canpaused = false;
                    btn_start.setText("开始");


                }


            }
                break;

        }
    }


    @Override
    public boolean onMessage(Event event, Object value) {

        switch (event){

            case B_WORD_SPEAKING:

            {
                MWord word = (MWord) value;

                text_word.setText(word.word);

                text_cn.setText(word.cn);

                canpaused = true;
                btn_start.setEnabled(true);
                btn_start.setText("暂停");


            }
                break;

        }
        return false;

    }

    @Override
    public void onStateChanged(FormState fs, Object value) {

        //log("%-12s taskid:%d taskroot:%b", fs, getTaskId(), isTaskRoot());

        switch (fs) {

            case FS_CREATE:

                setContentView(R.layout.formorder);
                setTitle("顺序背单词");

                break;
            case FS_DESTORY:


                cancel(Event.B_WORD_ORDER);

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
                code.code = "FormOrder\nsendMessage(Event.B_WORD_ORDER);\n /iedata/flows/b_word_order.ie";
                code.drawable = R.mipmap.flow_word_order;

                sendMessage(Event.F_CODE_EXAMPLE,code);

                return true;

        }
        return false;

    }

}