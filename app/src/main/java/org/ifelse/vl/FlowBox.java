package org.ifelse.vl;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import org.ifelse.vldata.Event;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FlowBox {

    static {

        global_values = new HashMap<String, Object>();
        //parseProjectinfos();
    }



    static Map<String, Object> global_values;
    Map<String, Object> static_values = new HashMap<>();
    Map<String, FlowPoint> points = new HashMap<String, FlowPoint>();


    private Event eventValue;
    private FlowPoint point_root;
    private FlowPoint point_current;
    private FlowPoint point_error;
    private Context context;

    private boolean debug = true;

    Object value;
    private String title;
    private String TAG;


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
        TAG = String.format("%s-%d",title,hashCode());
    }



    public FlowBox(Context context,Event event) {

        this.context = context;
        this.eventValue = event;

    }

    public Context getContext() {

        return this.context;

    }

    public void setError(FlowPoint e){

        this.point_error = e;
    }

    public void setRoot(FlowPoint r) {

        this.point_root = r;

    }
    public FlowPoint getRoot(){
        return point_root;
    }

    public void setDebug(boolean f) {

        debug = f;
    }

    public Object getFlowParam() {


        return value;

    }


    long timestart;


    public void run(final Object value) {
        this.value = value;
        point_current = point_root;
        flows.add(new WeakReference<FlowBox>(this));

        timestart = System.currentTimeMillis();
        new Thread(null, null, "flowbox", 1024 * 1024 * 2) {
            public void run() {

                try {

                    if( !NLog.release ) log("point(%s)[%d] %s :%s",point_current.getId(), flow_index++,point_current.getDescript(), point_current.getClass().getSimpleName());

                    point_current.run(FlowBox.this);

                } catch (Exception e) {
                    NLog.e(e);
                    error(e);

                }

            }
        }.start();


    }

    int flow_index;

    public void next()  {




        if (canceled || point_current == null || point_current.isEnd()) {

            onFinish();

        } else {

            point_current = point_current.getChild(FlowBox.this);
            if (point_current == null)
                onFinish();
            else {

                try {
                    if( !NLog.release )
                        log("point(%s)[%d] %s :%s",point_current.getId(), flow_index++,point_current.getDescript(), point_current.getClass().getSimpleName());
                    point_current.run(this);
                }catch (Exception e){
                   // NLog.e(e);
                    error(e);
                }
            }

        }

    }

    public void log(String str, Object... args) {

        if( str != null )
        NLog.log(TAG , str, args);

    }


    boolean isFinished = false;

    public void onFinish() {

        //log("flow is over. duration:%d",(System.currentTimeMillis()-timestart));

        isFinished = true;

        long duration = (System.currentTimeMillis()-timestart);

        if( canceled )
            log("flow is canceled. duration:%d",duration);
        else
            log("flow is over. duration:%d",duration);


        flow_index = 0;
        synchronized (static_values) {
            static_values.clear();
        }

    }


    public void addPoint(FlowPoint flowpoint) {

        points.put(flowpoint.getId(), flowpoint);

    }

    public FlowPoint getPoint(String pointid) {
        return points.get(pointid);
    }

    public void setEvent(Event event) {

        this.eventValue = event;
    }


    public void setValue(String s, Object s1) {

        if (s == null || s.length() == 0)
            return;

        if (s.charAt(0) == '$') {
            synchronized (static_values){
                static_values.put(s, s1);
            }
        }
        else if (s.charAt(0) == '#')
            synchronized (global_values) {
                global_values.put(s, s1);
            }

    }

    public Object getValue(String k) {

        if( k == null || k.length() == 0 )
            return k;
        if (k.charAt(0) == '$')
            return static_values.get(k);
        else if (k.charAt(0) == '#')
            return global_values.get(k);
        else if(k.length() > 1 && k.charAt(0) == 'R' && k.charAt(1) == ':' )
            return VL.getInstance()._adapter.getVarString(k);
        return k;

    }


    public void remove(String key) {


        if (key == null || key.length() == 0) {
            return;
        }
        if (key.charAt(0) == '$') {
            synchronized (static_values) {
                static_values.remove(key);
            }
        } else if (key.charAt(0) == '#') {
            synchronized (global_values) {
                global_values.remove(key);
            }
        }


    }

    public String getVarString(String korv) {

        if( korv == null || korv.length() == 0 )
            return korv;
        if (isVar(korv)) {
            Object r = getValue(korv);
            if (r == null)
                return null;
            else
                return r.toString();
        } else {
            if( korv.length() > 1 && korv.charAt(0) == 'R' && korv.charAt(1) == ':'){
                return VL.getInstance()._adapter.getVarString(korv);
            }
            return korv;
        }

    }

    public static  boolean isContainGlobal(String k){
        return global_values.containsKey(k);
    }

    public static Object getGlobalValue(String k) {

        return global_values.get(k);

    }

    public static boolean isOK(String k, Object v){

        return ( global_values.containsKey(k) && v != null && v.equals(global_values.get(k)));

    }

    public static void clearGlobalValues(){

        try {
            NLog.i("global clearGlobalValues");
            for (String k : global_values.keySet()) {
                NLog.i("global value-> key:%s v:%s", k, global_values.get(k));
            }
        }catch (Exception e){}

        global_values.clear();
    }


    boolean err_happend;
    public void error(Exception e) {

        //MessageCenter.getInstance().sendMessage(T.REQ_WAITTING_HIDE);
        //log(">>flow exec error.check flow log. %s err_had:%b error_p:%s",err_happend,point_error);

        if( !err_happend && point_error != null ){

            try {

                point_current =   point_error;
                setValue("$exception",e);

                log("point[%d] %s :%s", flow_index++,point_current.getDescript(), point_current.getClass().getSimpleName());

                point_error.run(this);


            } catch (Exception err) {
                err.printStackTrace();
            }
            err_happend = true;
        }
        else{
            NLog.e(e);

            onFinish();
        }



        //

    }


    public boolean isVar(String str) {
        return str != null && str.length() > 0 && (str.charAt(0) == '$' || str.charAt(0) == '#');
    }


    public static void setGlobalValue(String key, Object value) {

        if( key != null )
        if (key.charAt(0) == '#') {
            synchronized (global_values) {
                global_values.put(key, value);
            }
        }

    }
    public static void removeGlobal(String key){
        if( key != null )
            synchronized (global_values){

                global_values.remove(key);
            }
    }

    public final static boolean isNumeric(String s) {
        if (s != null && !"".equals(s.trim()))
            return s.matches("^[0-9]*$");
        else
            return false;
    }

    public void runInMain(Runnable runnable){

        new Handler(Looper.getMainLooper()).post(runnable);

    }
    public void runInMain(Runnable runnable,long delay){

        new Handler(Looper.getMainLooper()).postDelayed(runnable,delay);

    }

    static List<WeakReference<FlowBox>> flows = new ArrayList<>();
    private boolean canceled;


    public static void addCancelEvent(Event event) {

        for (int i = flows.size() - 1; i > -1; i--) {

            WeakReference<FlowBox> wf = flows.get(i);
            FlowBox flowBox = wf.get();
            if (flowBox == null || flowBox.isFinished) {
                flows.remove(i);
            }
            else if( flowBox.getEvent().ordinal() == event.ordinal() ){

                NLog.i("取消掉 流程：%s  %s",event,flowBox.getTitle());
                flowBox.cancel();

            }

        }

    }

    public Event getEvent(){

        return eventValue;
    }
    public void cancel(){

        canceled = true;
    }
}
