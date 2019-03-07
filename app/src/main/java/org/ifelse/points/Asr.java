package org.ifelse.points;

import com.alibaba.fastjson.JSON;
import com.baidu.speech.EventListener;
import com.baidu.speech.asr.SpeechConstant;
import org.ifelse.models.MAsr;
import org.ifelse.wordspelling.JKApp;
import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;
import org.ifelse.vl.NLog;

public class Asr extends FlowPoint {
    @Override
    public void run(final FlowBox flowBox) throws Exception {


        //flowBox.setValue(params.get("result"),result);



        new Thread(){


            @Override
            public void run() {


                JKApp.instance.asr.registerListener(new EventListener() {


                    String result;
                    @Override
                    public void onEvent(String name, String params, byte[] bytes, int offset, int length) {

                        flowBox.log("asr name:%s params:%s [%s]",name,params,bytes==null?"null":new String(bytes,offset,length));
                        switch (name){

                            case SpeechConstant.CALLBACK_EVENT_ASR_END:

                                JKApp.instance.asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0);
                                break;
                            case SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL:
                            {

                                if( bytes != null )
                                {
                                    result = new String(bytes,offset,length);
                                    flowBox.log(result);
                                }

                            }
                                break;
                            case  SpeechConstant.CALLBACK_EVENT_ASR_EXIT: {
                                JKApp.instance.asr.unregisterListener(this);

                                if( result != null ) {

                                    try {

                                        flowBox.log(result);

                                        MAsr mAsr = JSON.parseObject(result, MAsr.class);
                                        if (mAsr.getError() == 0) {
                                            String str = mAsr.getBest_result();
                                            setValue(flowBox, "result", str.replace(" ", ""));
                                        }

                                    } catch (Exception e) {


                                        NLog.e(e);
                                    }
                                }



                                flowBox.next();
                            }
                                break;


                        }


                    }
                });

                String json ="{\"accept-audio-data\":false,\"disable-punctuation\":false,\"accept-audio-volume\":true,\"pid\":1737}";


                JKApp.instance.asr.send(SpeechConstant.ASR_START, json, null, 0, 0);




            }
        }.start();


        Thread.sleep(10000);







    }
}
