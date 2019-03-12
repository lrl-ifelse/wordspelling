import com.intellij.openapi.project.Project
import org.ifelse.editors.EditorFactory;
import org.ifelse.model.MProperty;
import org.ifelse.vl.VLItem;

import org.ifelse.IEAppLoader;
import org.ifelse.utils.*;

class FormBase{

    Project project;
    
    void run(Map binding){

        String path = project.getBasePath()+"/iedata/template/form.tpl";
        String path_layout = project.getBasePath()+"/iedata/template/layout.tpl";

        def tpl = new File(path);
        def engine = new groovy.text.GStringTemplateEngine();
        def template = engine.createTemplate(tpl).make(binding);

        def classname = binding.get("classname");

        String layout_file = project.getBasePath()+"/app/src/main/res/layout/"+classname.toLowerCase()+".xml";



        String dir = project.getBasePath()+"/app/src/main/java/org/ifelse/vldata/forms/";
        String javapath = dir +classname+".java";

        def filedir = new File(dir);
        if( !filedir.exists() )
            filedir.mkdirs();

        def file = new File(javapath);

        if (!file.exists()) {

            FileUtil.copy(path_layout,layout_file);
            FileUtil.save(template.toString(),javapath);

            log("   " + javapath);
            log("   " + layout_file);

            String manifest = project.getBasePath()+"/app/src/main/AndroidManifest.xml";

            StringBuffer strs = new StringBuffer();
            strs.append( "\t\t<activity\n");
            strs.append( "\t\t android:name=\"org.ifelse.vldata.forms."+classname+"\"\n");
            strs.append( "\t\t android:configChanges=\"keyboard|keyboardHidden|orientation|locale|screenSize\"\n");
            strs.append( "\t\t android:launchMode=\"singleTask\"\n");
            strs.append( "\t\t android:screenOrientation=\"portrait\" \n");
            strs.append( "\t\t >\n");
            strs.append( "\t\t</activity>\n");

            FileUtil.addActivity(manifest,strs);

            
        }
        else {
            //log("exist " + javapath);
        }



    }

    void log(String msg){

        GUI.println(project,msg);

    }

}