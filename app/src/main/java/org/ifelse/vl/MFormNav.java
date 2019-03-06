package org.ifelse.vl;


import org.ifelse.vldata.Event;

public class MFormNav {

    public Event form_clear_to;//清空页面直到指定页面
    public Event form_push;//弹出页面
    public Object value;

    public MFormNav(Event clear_to , Event push_to, Object o) {
        form_clear_to = clear_to;
        form_push = push_to;
        value = o;
    }

}
