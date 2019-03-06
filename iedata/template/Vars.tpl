package org.ifelse.vldata;

import org.ifelse.vl.FlowBox;

public class Vars{



<%

 for(MVar mvar : vars)
 {


   out.println('    /* '+mvar.flow+'->'+mvar.descript+'.'+mvar.name+" */")
   out.println('    public static final String K_'+string.sub(mvar.key,2)+' = "'+mvar.key+'";')


 }

%>


	public static Object getGlobalVar(String key){

		return FlowBox.getGlobalValue(key);

	}



}

