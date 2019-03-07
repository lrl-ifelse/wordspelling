package org.ifelse.points;


import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;
import org.ifelse.vl.VL;
import org.ifelse.vldata.Event;

public class EventSend  extends FlowPoint {

	final String key_event = "event";//数据存放变量
	final String key_out = "params";//数据存放变量
	final String key_delay = "delay";//数据存放变量

	@Override
	public void run(final FlowBox flowBox) throws Exception {


		final Event e = Event.getEvent( Integer.parseInt( params.get( key_event )) );//即将发送的任务事件

		flowBox.log("Event:%s",e);

		final String value = params.get(key_out);//发送出去的任务事件携带的参数

		final Object objparam = flowBox.getValue(value);


		String delay = flowBox.getVarString(params.get(key_delay));
		if( !isNull( delay ) )
		{
			final long time = Long.parseLong(delay);
			flowBox.runInMain(new Runnable() {
				@Override
				public void run() {
					VL.send(e,isNull( value ) ? null:objparam);
				}
			},time);

		}else
		{

				flowBox.runInMain(new Runnable() {
					@Override
					public void run() {
						VL.send(e,isNull( value ) ? null:objparam);
					}
				});

		}



		flowBox.next();

	}




}
