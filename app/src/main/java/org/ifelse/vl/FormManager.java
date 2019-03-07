package org.ifelse.vl;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.view.View;
import org.ifelse.wordspelling.JKApp;
import org.ifelse.wordspelling.R;
import org.ifelse.vldata.Event;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by dizhanbin on 18/7/13.
 */

public class FormManager {


    ListMap<Class,WeakReference<Form>> _forms = new ListMap<Class,WeakReference<Form>>();
    HashMap<Class,Object> _values = new HashMap<>();

    Application _application;

    public FormManager(Application application){

        _application = application;


        _application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                Form form = (Form) activity;
                Object value = _values.remove(form.getClass());
                form.onValue(value);

            }

            @Override
            public void onActivityStarted(Activity activity) {




            }

            @Override
            public void onActivityResumed(Activity activity) {

                log("加入队列：%s",activity);

                _forms.put(activity.getClass(),new WeakReference(activity));
                log("--------------当前页面-------------------------onActivityResumed %s",activity);
                for(int i=_forms.size()-1;i>-1;i--){

                    Form form = _forms.getByIndex(i).get();
                    if( form != null )
                        log("当前页面 ->%s  should_destory:%b",form,form.isDestoryOnPause());

                }


            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {




            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

                _forms.remove(activity.getClass());

                log("--------------当前页面-------------------------onActivityDestroyed %s",activity);
                for(int i=_forms.size()-1;i>-1;i--){

                    Form form = _forms.getByIndex(i).get();
                    if( form != null )
                        log("当前页面 ->%s  should_destory_onpause:%b",form,form.isDestoryOnPause());

                }


            }
        });



    }

    public Form top(){

        if( _forms.size() == 0 )
            return null;
        return _forms.getByIndex(_forms.size()-1).get();

    }

    public void push(Class<Form> formClass, Object value) {



        log("push 页面:%s ",formClass);
        //当前页面 无需启动
        Class current_class = _forms.top();
        if( formClass.equals(current_class) )
        {
            log("已在当前页面不进行跳转 onValue");
            WeakReference<Form> form_wr = _forms.get(current_class);
            if( value instanceof MFormParams )
            {
                MFormParams params = (MFormParams) value;
                form_wr.get().onValue(params.value);
            }
            else
                form_wr.get().onValue(value);

            return;
        }


        final Form current_form = top();


        final Context context = current_form==null? JKApp.instance:current_form;

        WeakReference<Form> wr = _forms.get(formClass);



        final Intent intent = new Intent(context,formClass);
        //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        //未发现指定页面
        if( wr == null  ){
            log("队列中未发现指定页面");



            if( value instanceof MFormParams){

                log("创建并启动页面:%s 有共享动画",formClass);

                MFormParams fp = (MFormParams) value;



                final View share_view = fp.share_view != null ? fp.share_view : ( fp.share_view_id > 0 ? current_form.findViewById(fp.share_view_id) : null );




                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP&&share_view!=null  && current_form != null ) {

                    if( share_view.getTransitionName() == null || share_view.getTransitionName().length() == 0 )
                        share_view.setTransitionName("share");
                    log("有共享动画启动  view:%s  transitionName:%s",share_view,share_view.getTransitionName());
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(current_form, Pair.create(share_view,share_view.getTransitionName()));
                    ActivityCompat.startActivity(current_form, intent, compat.toBundle());


                }
                else {


                    //放大空间 跳转动画
                    //ActivityOptionsCompat compat = ActivityOptionsCompat.makeThumbnailScaleUpAnimation(view, BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), 0, 0);
                    //ActivityCompat.startActivity(this, new Intent(this, SecondActivity.class), compat.toBundle());
                    log("无共享动画启动 参数中无共享元素");
                    ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(context,fp.anim_exter, fp.anim_exit);
                    ActivityCompat.startActivity(context, intent, compat.toBundle());


                }
                _values.put(formClass, fp.value);
            }
            else {

                log("创建并启动页面 无FormParams:%s",formClass);





                ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.fade_in_0,R.anim.fade_in_0);
                if( current_form == null )
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityCompat.startActivity(context, intent, compat.toBundle());

                _values.put(formClass, value);
            }

            return;

        }
        else//队列中有此页面
        {


            log("队列中已存在此页面");
            Form form = wr.get();


            int enter = form.getAnimationEnter(true);
            if( enter == 0 )
                enter = R.anim.fade_in_0;
            int exit = current_form.getAnimationExit(false);
            if( exit == 0 )
                exit = R.anim.fade_in_0;


            ActivityOptionsCompat compat = ActivityOptionsCompat.makeCustomAnimation(current_form,enter,exit);
            ActivityCompat.startActivity(current_form, intent, compat.toBundle());
            form.onValue(value);

            return;
        }


    }

    public void onMessage(Event event, Object value) {

        for(int i=_forms.size()-1;i>-1;i--){

            Form form = _forms.getByIndex(i).get();
            if( form.onMessage(event,value) ){
                log("事件:%s 在页面:%s 中执行",event,form);
                return;
            }

        }


    }

    public void log(String format, Object... value){

        NLog.log("FormManager",format,value);

    }

}
