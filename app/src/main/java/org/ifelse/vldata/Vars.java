package org.ifelse.vldata;

import org.ifelse.vl.FlowBox;

public class Vars{






	public static Object getGlobalVar(String key){

		return FlowBox.getGlobalValue(key);

	}



}

