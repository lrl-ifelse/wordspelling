package org.ifelse.vldata;

import org.ifelse.vl.FlowBox;

public class Urls{



    public static String getUrl(int id){

        switch (id) {
<%

 for(Map map : urls)
 {

   out.println('            case '+map.get("id")+': return "'+map.get("url")+'";//'+map.get("descript") );

 }

%>
        }
        return null;

    }

}