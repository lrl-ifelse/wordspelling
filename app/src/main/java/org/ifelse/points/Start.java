package org.ifelse.points;

import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;

public class Start extends FlowPoint {


    static final String key_params = "params";

    @Override
    public boolean isStart() {
        return true;
    }



    @Override
    public void run(FlowBox flowBox) throws Exception {

        flowBox.log("Start params:%s", flowBox.getFlowParam());
        String paramstr = params.get(key_params);
        if (!isNull(paramstr)) {
            flowBox.setValue(paramstr, flowBox.getFlowParam());
        }

        flowBox.notifyFlowContinue();

    }


}
