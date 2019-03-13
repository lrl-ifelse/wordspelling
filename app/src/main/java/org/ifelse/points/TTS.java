package org.ifelse.points;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;

import org.ifelse.utils.FileUtil;
import org.ifelse.wordspelling.JKApp;
import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class TTS extends FlowPoint {
    @Override
    public void run(final FlowBox flowBox) throws Exception {



       String txt = getVarString(flowBox,"txt" );


       flowBox.log("TTS:%s",txt);

        JKApp.instance.mSpeechSynthesizer.setSpeechSynthesizerListener(new SpeechSynthesizerListener() {

            FileOutputStream fileOutputStream = null;

            @Override
            public void onSynthesizeStart(String s) {

//                try {
//                    fileOutputStream = new FileOutputStream("/sdcard/a.mp3");
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }

                flowBox.log("tts onSynthesizeStart:%s",s );
            }

            @Override
            public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

//                flowBox.log("tts onSynthesizeDataArrived:%s",s );
//
//
//                try {
//                    fileOutputStream.write(bytes);
//                    fileOutputStream.flush();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }



            }

            @Override
            public void onSynthesizeFinish(String s) {


//                flowBox.log("tts onSynthesizeFinish:%s",s );
//                try {
//                    fileOutputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }

            @Override
            public void onSpeechStart(String s) {

                //flowBox.log("tts onSpeechStart:%s",s );

            }

            @Override
            public void onSpeechProgressChanged(String s, int i) {
                //flowBox.log("tts onSpeechProgressChanged:%s  %d",s,i );
            }

            @Override
            public void onSpeechFinish(String s) {

                flowBox.log("tts onSpeechFinish:%s",s );
                flowBox.next();

            }

            @Override
            public void onError(String s, SpeechError speechError) {
                flowBox.log("tts onError:%s   speechError:%s",s,speechError);
                flowBox.next();
            }
        });
        JKApp.instance.mSpeechSynthesizer.speak(txt);




    }
}
