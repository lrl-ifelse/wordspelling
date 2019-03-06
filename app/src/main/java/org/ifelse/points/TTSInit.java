package org.ifelse.points;

import org.ifelse.speakword.JKApp;
import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;

public class TTSInit extends FlowPoint {
    @Override
    public void run(FlowBox flowBox) throws Exception {


        boolean result = JKApp.instance.initTTs();


        flowBox.setValue(params.get("result"),result);

        flowBox.notifyFlowContinue();

    }
}
