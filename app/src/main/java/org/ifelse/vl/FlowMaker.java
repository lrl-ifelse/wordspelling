package org.ifelse.vl;

import android.content.Context;
import android.support.annotation.RawRes;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.ifelse.vldata.Event;
import org.ifelse.vldata.FlowBoxs;
import org.ifelse.vldata.FlowPointFactory;

import java.io.InputStream;

/**
 * Created by dizhanbin on 18/7/16.
 */

public class FlowMaker {





    public static FlowBox parse(Context context, @RawRes int rid, Event event){







        try {


            long current = System.currentTimeMillis();


            FlowBox box = new FlowBox(context, event);

            MFlow flow = FlowBoxs.getBoxInfo(event);
            box.setTitle(flow.name);


            InputStream is = context.getResources().openRawResource(rid);

            byte[] bytes = new byte[is.available()];

            is.read(bytes);

            is.close();

            String json = new String(bytes, "utf-8");


            Object obj_ = JSON.parse(json);

            JSONArray jsonArray = null;

            if( obj_ instanceof JSONObject )
            {
                JSONObject jobj = (JSONObject) obj_;

                if( jobj.containsKey("items") )
                {
                    jsonArray = jobj.getJSONArray("items");
                }

            }else if( obj_ instanceof  JSONArray ) {

                jsonArray = (JSONArray) obj_;
            }



            for (Object obj : jsonArray) {

                JSONObject jobj = (JSONObject) obj;

                if (!jobj.getBoolean("line")) {

                    int eid = jobj.getInteger("flow_point_id");
                    String pid = jobj.getString("id");
                    FlowPoint point = FlowPointFactory.createFlowPoint(eid);

                    point.setId(pid);
                    JSONArray mps = jobj.getJSONArray("mproperties");
                    for (Object mpobj : mps) {

                        JSONObject mpjobj = (JSONObject) mpobj;
                        point.addParams(mpjobj.getString("key"), mpjobj.getString("value"));

                    }
                    box.addPoint(point);
                    if (point.isStart())
                        box.setRoot(point);


                } else {

                    Line line = new Line();


                    String id_form = jobj.getString("id_from");
                    String id_to = jobj.getString("id_to");

                    line.fromid = id_form;
                    line.toid = id_to;

                    JSONArray mps = jobj.getJSONArray("mproperties");

                    for (Object mpobj : mps) {

                        JSONObject mpjobj = (JSONObject) mpobj;

                        switch (mpjobj.getString("key")) {
                            case "descript":
                                line.conditions = mpjobj.containsKey("value") ? mpjobj.getString("value"):null;
                                break;
                            case "isint":
                                line.isNumber = mpjobj.containsKey("value") && mpjobj.getBoolean("value");
                                break;

                        }

                    }

                    line.child = box.getPoint(line.toid);
                    line.setParent( box.getPoint(line.fromid) );



                }

            }

            NLog.i("parse flow time:%d",(System.currentTimeMillis()-current));

            return box;

        }catch (Exception e){

            NLog.e(e);
        }
        return null;



    }
}
