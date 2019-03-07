package org.ifelse.points;

import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizerListener;
import org.ifelse.wordspelling.JKApp;
import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;

public class TTS extends FlowPoint {
    @Override
    public void run(final FlowBox flowBox) throws Exception {



       String txt = getVarString(flowBox,"txt" );


       flowBox.log("TTS:%s",txt);

        JKApp.instance.mSpeechSynthesizer.setSpeechSynthesizerListener(new SpeechSynthesizerListener() {
            @Override
            public void onSynthesizeStart(String s) {

                flowBox.log("tts onSynthesizeStart:%s",s );
            }

            @Override
            public void onSynthesizeDataArrived(String s, byte[] bytes, int i) {

            }

            @Override
            public void onSynthesizeFinish(String s) {

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
