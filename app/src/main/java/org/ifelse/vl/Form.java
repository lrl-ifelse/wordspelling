package org.ifelse.vl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import org.ifelse.vldata.Event;

import java.lang.reflect.Field;

/**
 * Created by dizhanbin on 18/7/11.
 */

public class Form extends AppCompatActivity {

    private boolean is_should_destory_on_pause;

    private OnPermissionListener _permission_listener;

    public void setDestoryOnPause(boolean canstop){

        is_should_destory_on_pause = canstop;

    }
    public boolean isDestoryOnPause(){
        return is_should_destory_on_pause;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        onStateChanged(FormState.FS_CREATE, savedInstanceState);
        bind();
        onStateChanged(FormState.FS_UI_INIT, null);

    }


    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        onStateChanged(FormState.FS_RESUME, null);

    }


    @Override
    protected void onStart() {
        super.onStart();
        onStateChanged(FormState.FS_START, null);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onStateChanged(FormState.FS_RESTART, null);
    }

    @Override
    protected void onStop() {
        super.onStop();

        onStateChanged(FormState.FS_STOP, null);

    }

    @Override
    protected void onPause() {
        super.onPause();
        onStateChanged(FormState.FS_PAUSE, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        onStateChanged(FormState.FS_DESTORY, null);
    }

    public boolean onMessage(Event event, Object value) {

        return false;
    }

    public void onValue(Object value) {
        onStateChanged(FormState.FS_VALUE, value);
    }

    public void sendMessage(final Event event, final Object value, long delay) {


        if (delay <= 0)
            sendMessage(event, value);
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    sendMessage(event, value);
                }
            }, delay);
        }

    }

    public void sendMessage(Event event) {

        sendMessage(event, null);

    }

    public void sendMessage(Event event, Object obj) {

        VL.send(event, obj);

    }

    public void cancel(Event event) {
        VL.cancel(event);
    }

    public void log(String format, Object... args) {

        NLog.log(getClass().getSimpleName(), format, args);

    }

    public void bind() {

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            //log("field :%s",field.getName());
            field.setAccessible(true);
            Bind bind = field.getAnnotation(Bind.class);
            if (bind != null) {

                View view = findViewById(bind.value());
                if (view != null)
                    try {
                        field.set(this, view);
                    } catch (IllegalAccessException e) {
                        //e.printStackTrace();
                        throw new RuntimeException("bind view error:" + field.getName(), e);
                    }

            }
        }

    }

    public void onClick(View view){


    }

    public void onStateChanged(FormState fs, Object value) {

        log("%-12s taskid:%d taskroot:%b", fs, getTaskId(), isTaskRoot());

    }





    public int getAnimationEnter(boolean fromback) {

        return 0;//fromback ? R.anim.slide_in_left : R.anim.slide_in_right;


    }

    public int getAnimationExit(boolean enterback) {

        return 0;//enterback ? R.anim.slide_out_left : R.anim.slide_out_right;

    }

    /*
     *  隐藏软键盘
     * */
    public void hideSoftInput() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public void showKeyboard() {


        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    public void runOnUiThread(final Runnable runnable, final long delay) {


        new Thread() {

            @Override
            public void run() {


                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(runnable);


            }
        }.start();

    }

    public enum FormState {

        FS_VALUE,  //页面传递过来的参数
        FS_CREATE, //设置布局
        FS_UI_INIT,//初始化布局
        FS_START,
        FS_RESTART,
        FS_RESUME,
        FS_PAUSE,
        FS_STOP,
        FS_DESTORY,

    }


    public interface OnPermissionListener {

        void onPermission(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }


    public void mainThreadExecuteDelay(Runnable runnable, long time){

        new Handler(Looper.getMainLooper()).postDelayed(runnable,time);

    }
    public void postDelayInUI(Runnable runable,long delay){

        runOnUiThread(runable,delay);

        //new Handler( Looper.getMainLooper() ).postDelayed(runable,delay);

    }

    public Object getRootParentTag(int parentid, View view){


        if( view.getId() == parentid )
            return view.getTag();
        View parent = (View)view.getParent();
        while(parent != null){

            if( parent.getId() == parentid )
                return parent.getTag();
            parent = (View) parent.getParent();

        }
        return null;

    }


    public void setText(View view , @IdRes int rid, String text){

        if( view != null ) {
            TextView tv = view.findViewById(rid);
            if( tv != null )
                tv.setText(text);
        }

    }
    public void setText(@IdRes int rid, String text){
        TextView tv = findViewById(rid);
        if( tv != null )
            tv.setText(text);
    }

    public void resetEnable(final View view,long delay){
        view.setEnabled(false);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if( view != null )
                    view.setEnabled(true);
            }
        },delay);

    }


    public void setOnPermissionListener(OnPermissionListener l) {

        _permission_listener = l;

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (_permission_listener != null) {
            _permission_listener.onPermission(requestCode, permissions, grantResults);
        }
    }

}
