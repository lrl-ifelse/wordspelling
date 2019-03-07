package script

import com.intellij.openapi.project.Project
import org.ifelse.editors.EditorFactory;
import org.ifelse.model.MProperty;
import org.ifelse.IEAppLoader
import org.ifelse.vl.*;
import org.ifelse.utils.*;
import org.ifelse.model.*;
import com.alibaba.fastjson.JSON;

class R {


    Project project;
    VLPoint vlPoint;
    MFlowPoint mFlowPoint;

    String getSequence(Project project){

        return IEAppLoader.getMProject(project).getSequenceStr();

    }
    /*
    open a activity
     */
    void open_form(){
        

        List<MProperty> properies = vlPoint.mproperties;

        def formname = getProperty(properies,"classname");
        def path = project.getBasePath()+"/app/src/main/java/org/ifelse/vldata/forms/"+formname+".java";

        GUI.println(project,"/script/R.groovy.open_form");
        GUI.println(project,"open:"+path);

        File file = new File(path);

        if( !file.exists() )
        {
            GUI.println(project,"设置模版后点击运行生成页面代码");
        }else
        {
            EditorFactory.open(project,path);
        }

    }
    /*

    open a flowpoint
     */
    void open_point(){


        String classname = mFlowPoint.classz;

        def path = project.getBasePath()+"/app/src/main/java/org/ifelse/points/"+classname.substring( classname.lastIndexOf('.') + 1)+".java";

        GUI.println(project,"/script/R.groovy.open_point");
        GUI.println(project,"open:"+path);

        EditorFactory.open(project,path);


    }

    /*
    open a flow
     */
    void open_flow(){

        List<MProperty> properies = vlPoint.mproperties;


        def path = project.getBasePath()+"/iedata/flows/"+getProperty(properies,"name")+".ie";

        GUI.println(project,"/script/R.groovy.open_flow");
        GUI.println(project,"open:"+path);

        File file = new File(path);

        if( !file.exists() )
        {

            MDoc mDoc = new MDoc();

            mDoc.title = getProperty(properies,"descript");
            mDoc.flowid = vlPoint.id;



            VLPoint point = new VLPoint();
            point.id = 1;
            point.flow_point_id = "100101";
            point.mproperties = new ArrayList();

            MProperty mp_args = vlPoint.getMProperty("event");
            point.mproperties.add(mp_args.clone());
            point.mproperties.add(new MProperty("value"));
            point.mproperties.add(new MProperty("descript","descript","起始点"));

            point.x = 50;
            point.y = 50;

            mDoc.addItem(point);
            String json =  JSON.toJSONString(mDoc, SerializerFeature.PrettyFormat);
            FileUtil.save(ljson,path);


        }


        EditorFactory.open(project,path);


    }



    String getProperty(List<MProperty> properies,String key){



        for(int i=0;i<properies.size();i++)
        {
            if( properies.get(i).key.equals(key) )
            {

                return properies.get(i).value;

            }


        }
        return "";


    }


    List<MVar> getVars(){

        String path = project.getBasePath()+"/iedata/Flows.ie";
        List<VLItem> list = TemplateUtil.parseFlow(path);

        List<MVar> static_vars = new ArrayList();


        for(VLItem item : list) {

            if (item instanceof VLPoint) {

                VLPoint vp = (VLPoint) item;

                if (vp.flow_point_id.equals("900102")) {

                    String from = project.getBasePath()+"/iedata/flows/"+vp.getMProperty("name").value+'.ie';

                    List<VLItem> items = TemplateUtil.parseFlow(from);
                    for(VLItem p_item : items ){

                        List<MVar> vars = p_item.getVars();

                        if( vars != null )
                            for(MVar mvar : vars){

                                int vindex = static_vars.indexOf(mvar);
                                if( vindex > -1 )
                                {
                                    static_vars.get(vindex).count++;
                                }
                                else{
                                    static_vars.add(mvar);
                                    mvar.count = 1;
                                }

                            }




                    }

                    //log('flow->'+from);
                }
            }
        }

        return static_vars;


    }



}
