package org.ifelse.points;


import org.ifelse.models.MWord;
import org.ifelse.vl.FlowBox;
import org.ifelse.vl.FlowPoint;
import org.ifelse.vl.SQLO;

import java.io.*;

/**
 * Created by dizhanbin on 17/11/15.
 */

public class BWordImport extends FlowPoint {
    @Override
    public void run(FlowBox flowBox) throws Exception {



        SQLO.beginTransaction();

        try {


            InputStream inputStream = flowBox.getContext().getResources().getAssets().open("words.txt");

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


            String word = null;
            int lines = 0;
            while ((word = bufferedReader.readLine()) != null) {

                lines++;
                {

                    String cn = bufferedReader.readLine();

                    MWord mWord = new MWord();
                    mWord.word = word.replace("'","''");
                    mWord.cn = cn.replace("'","''");
                    mWord.seq = lines;

                    SQLO.insert(mWord);

                }


            }
            SQLO.setTransactionSuccessfully();
            flowBox.setValue(params.get("result"),true);

        }
        catch (Exception e){
            flowBox.log("words import error:%s",e.toString());
            flowBox.setValue(params.get("result"),false);
        }
        finally {
            SQLO.commitTransaction();
        }
        flowBox.next();


    }
}
