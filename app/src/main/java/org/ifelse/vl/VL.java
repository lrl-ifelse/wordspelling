package org.ifelse.vl;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import org.ifelse.speakword.JKApp;
import org.ifelse.vldata.Event;
import org.ifelse.vldata.FlowBoxs;


/**
 * Created by dizhanbin on 18/7/12.
 */

public class VL {


    private static VL _instance = new VL();

    private FormManager _form_manager;
    private Application _applistion;

    public static VL getInstance(){

        return _instance;
    }

    public static void send(final Event event, final Object value){

        log("event :%s value:%s",event,value);
        if( _instance._adapter == null ) {
            log("event :%s","业务拦截适配器未实现");
            return;
        }
        if( !_instance._adapter.onEvent(event,value) ){//业务拦截



            final Class formClass = _instance._adapter.getForm(event);






            if( formClass != null && formClass.getGenericSuperclass().equals(Form.class)){//该事件为 页面事件

                log("页面事件 :%s",formClass);

                if( !is_run_in_main() ){//主线程中调用

                    log("去主线程中执行.");
                    run_in_main(new Runnable() {
                        @Override
                        public void run() {
                            log("push 页面");
                            _instance._form_manager.push(formClass,value);
                        }
                    });
                }
                else{
                    log("当前在主线程中执行.");
                    _instance._form_manager.push(formClass,value);

                }
                return;

            }



            int flow = _instance._adapter.getFlow(event);
            if( flow != 0 ){
                log("流程事件");

                FlowBox box = FlowMaker.parse(JKApp.instance,flow,event);

                if( box != null )
                    box.run(value);
                else{

                    throw new RuntimeException(String.format("未实现流程:%s", FlowBoxs.getBoxInfo(event).name));
                }

                return;
            }

            {//事件在 页面中调用
                log("事件发送页面处理 ");
                _instance._form_manager.onMessage(event,value);
            }


        }






    }

    public static void send(final Event event){

        send(event,null);
    }


    VLAdapter _adapter;

    public static void setAdapter(Application application,VLAdapter adapter){

        _instance._form_manager = new FormManager(application);
        _instance._adapter = adapter;
        NLog.release = !adapter.isDebug();
        NLog.on("初始化 运行环境");


    }

    public Form getForm() {
        return _form_manager.top();
    }

    public static void cancel(Event e) {

        FlowBox.addCancelEvent(e);
    }

    public void clearFormTo(MFormNav formNav) {


        Class form_to = _adapter.getForm(formNav.form_clear_to);

        for(int i=_form_manager._forms.size()-1;i>-1;i--){


            Form form = _form_manager._forms.getByIndex(i).get();
            if( form != null ){

                log("clearFormTo %s -->",form);
                if(  !form.getClass().equals(form_to)  ){

                    log("clearFormTo  --Xfinish");
                    form.finish();
                    _form_manager._forms.remove(i);

                }
                else{
                    log("clearFormTo  --to push to%s",( formNav.form_push==null?formNav.form_clear_to:formNav.form_push));
                    send(( formNav.form_push==null?formNav.form_clear_to:formNav.form_push),formNav.value);
                    break;
                }



            }


        }
    }



    public interface VLAdapter{

        Class getForm(Event event);
        int getFlow(Event event);
        boolean onEvent(Event event, Object value);
        boolean isDebug();

        String getVarString(String k);
    }


    static Handler _main_handler;
    public static void run_in_main(Runnable runnable){

        if( _main_handler == null )
            _main_handler = new Handler(Looper.getMainLooper());
        _main_handler.post(runnable);

    }
    public static boolean is_run_in_main(){

        return Thread.currentThread() == Looper.getMainLooper().getThread();

    }

    private static void log(String format,Object ... args){

        NLog.log("VL",format,args);
    }



}
