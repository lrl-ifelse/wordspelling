package org.ifelse.points;


import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;

/**
 * Created by dizhanbin on 17/11/15.
 */

public class PointNone extends FlowPoint {
    @Override
    public void run(FlowBox flowBox) throws Exception {

        flowBox.log("%s",getVarString(flowBox,"descript"));
        flowBox.notifyFlowContinue();

    }
}
