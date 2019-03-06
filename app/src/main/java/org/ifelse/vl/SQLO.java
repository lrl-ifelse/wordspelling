package org.ifelse.vl;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dzb on 16/3/9.
 */
public class SQLO {


    private static final String TAG = "SQLO";
    public static SQLiteDatabase database=null;
    static  boolean inited;
    public static boolean debug = true;

    public static interface CallBack{
        public void onInited();
    }
    public static void init(final Context context,final String path,final List<Class> ts,final CallBack callBack){




        new Thread(){
            public void run(){

                synchronized (databse_lock) {

                    if (database == null)
                        try {

                            int MODE_MULTI_PROCESS = 0x0004;
                            database = context.openOrCreateDatabase(path, MODE_MULTI_PROCESS, null);
                            initTables(ts);
                            inited = true;
                            callBack.onInited();
                            NLog.i("sql sqlo inited.");
                        } catch (Exception e) {
                            NLog.e(e);
                        }
                }

            }
        }.start();



    }

    static Object databse_lock = new Object();
    private static synchronized SQLiteDatabase getDatabase(){


        synchronized (databse_lock){

            return database;
        }


    }



    public static interface Trigger<T>{

        public void onUpdate(T oldv, T newv);

    }

    static Map<String,WeakReference<Trigger>> triggers = new HashMap<>();

    public static void addTrigger(Class tablec,Trigger trigger){

        triggers.put( tablec.getSimpleName(),new WeakReference<Trigger>(trigger) );


    }


    static long lastseq;
    public static String getSequence() {
        long id = System.currentTimeMillis();
        if( lastseq >= id )
            lastseq++;
        else
            lastseq = id;
        return String.valueOf(lastseq);
    }

    static Map<String,String> tables = new HashMap<String,String>();
    private static void initTables(List<Class> ts){


        Cursor cursor=null;
        try{
            cursor = getDatabase().query("sqlite_master",new String[]{"name"},null,null,null,null,null);
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                String tn = cursor.getString(0);
                tables.put(tn,tn);
                cursor.moveToNext();
            }

        }catch(Exception e){
            e.printStackTrace();
        }finally{
            if( cursor != null )
                cursor.close();
        }


        if( ts != null )
        for(Class classz:ts)
            existOrCreateTable(classz);




    }
    public static void close(){
        try{
            if( database != null ){
                if( getDatabase().isOpen() )
                    getDatabase().close();
            }
            database = null;
        }catch(Exception e){

        }
    }

    public static void existOrCreateTable(Class classz){
        //Table tt = (Table) classz.getAnnotation(Table.class);
        String tname = classz.getSimpleName();
        NLog.i("sqlo table name:%s", tname);
        if( tables.get(tname) == null )
            createTable(classz);
    }
    public static void createTable(Class classz){

        String tname = classz.getSimpleName();
        Field[] fields = classz.getDeclaredFields();

        StringBuffer   sql = new StringBuffer();
         sql.append("CREATE TABLE IF NOT EXISTS ").append(tname);
         sql.append(" (keyid varchar PRIMARY KEY");



        for(Field f:fields)
        {

            String fname = f.getName();
            if( fname.equals("keyid") )
            {
                continue;
            }
            else if( fname.endsWith("_") || fname.equals("serialVersionUID") )
            {
                continue;
            }


            if( f.getType() == Integer.class || f.getType() == int.class || f.getType() == Long.class || f.getType() == long.class )
            {
                sql.append(",").append(fname).append(" int ");
            }
            else if( f.getType() == String.class )
            {
                sql.append(",").append(fname).append(" varchar ");
            }
            else if( f.getType() == boolean.class || f.getType() == Boolean.class ){

                sql.append(",").append(fname).append(" bit ");

            }else if( f.getType() == float.class || f.getType() == Float.class ){

                sql.append(",").append(fname).append(" float ");

            }else if( f.getType() == double.class || f.getType() == Double.class ){

                sql.append(",").append(fname).append(" numeric(10,5) ");

            }




        }
        sql.append(")");

        execSql(sql.toString());

        tables.put(tname, tname);




    }


    public static void insertList(List list) throws IllegalAccessException {
        for(Object obj : list)
            insert(obj);

    }


    public static String insert(Object obj) throws IllegalAccessException {
        Class classz = obj.getClass();
        String tname = classz.getSimpleName();
        NLog.i("sqlo table name:%s", tname);
        if( tables.get(tname) == null )
        {
            createTable(classz);
        }

        String keyid = getSequence();

        StringBuffer  fields = new StringBuffer("keyid");

        StringBuffer  values = new StringBuffer("'");
        values.append(keyid).append("'");




        for(Field f:classz.getDeclaredFields())
        {

            String fname = f.getName();
            if( fname.equals("keyid") ) {
                f.set(obj,keyid);
                continue;
            }
            if( fname.endsWith("_") || fname.equals("serialVersionUID") )
            {

                continue;
            }
            if( f.getType() == int.class || f.getType() == Integer.class )
            {

                fields.append(",").append(fname);
                values.append(",").append( f.getInt(obj) );

            }
            else if( f.getType() == long.class || f.getType() == Long.class )
            {

                fields.append(",").append(fname);
                values.append(",").append( f.getLong(obj) );

            }
            else if( f.getType() == boolean.class || f.getType() == Boolean.class )
            {

                fields.append(",").append(fname);
                values.append(",").append( f.getBoolean(obj)?1:0 );

            }
            else if( f.getType() == float.class || f.getType() == Float.class )
            {

                fields.append(",").append(fname);
                values.append(",").append( f.getFloat(obj) );

            }
            else if( f.getType() == double.class || f.getType() == Double.class )
            {

                fields.append(",").append(fname);
                values.append(",").append( f.getDouble(obj) );

            }

            else if(f.getType() == String.class )
            {
                Object  v = f.get(obj);

                String value = null;
                if( v == null )
                    value = "";
                else
                    value = v.toString();
                fields.append(",").append(fname);
                values.append(",'").append( value ).append("'");

            }

        }

        execSql(String.format("insert into %s(%s) values(%s)", tname, fields.toString(), values.toString()));


        return keyid;


    }

    public static void execSql(String sql){

        NLog.i("sql:%s", sql);
        getDatabase().execSQL(sql);
    }

    public static void update(Object obj) throws Exception {

        Class classz = obj.getClass();
        String tname = classz.getSimpleName();
        NLog.i("sqlo table name:%s", tname);
        if( tables.get(tname) == null )
        {
            createTable(classz);
        }


        StringBuffer sql = new StringBuffer("update ");
        sql.append(tname).append(" set ");

        String keyid = null;


        WeakReference<Trigger> wf = triggers.get(tname);

        Object oldv = null;
        boolean needtrigger = ( wf != null && wf.get() != null );


        for(Field f:classz.getDeclaredFields())
        {


            String fname = f.getName();
            if( fname.equals("keyid") ) {

                keyid = f.get(obj).toString();

                if( needtrigger ){

                    oldv = selectOne(classz,"where keyid='"+keyid+"'");

                }

                continue;
            }
            if( fname.endsWith("_")  || fname.equals("serialVersionUID") )
            {

                continue;
            }
            if(  f.getType() == int.class || f.getType() == Integer.class  )
            {

                if( sql.indexOf("=")>0 )
                    sql.append(",");
                sql.append(fname).append("=").append( f.getInt(obj) );

            }
            else  if(  f.getType() == long.class || f.getType() == Long.class  )
            {

                if( sql.indexOf("=")>0 )
                    sql.append(",");
                sql.append(fname).append("=").append( f.getLong(obj) );

            }
            else if(  f.getType() == boolean.class || f.getType() == Boolean.class  )
            {

                if( sql.indexOf("=")>0 )
                    sql.append(",");
                sql.append(fname).append("=").append( f.getBoolean(obj)?1:0 );

            }else if(  f.getType() == float.class || f.getType() == Float.class  )
            {

                if( sql.indexOf("=")>0 )
                    sql.append(",");
                sql.append(fname).append("=").append( f.getFloat(obj) );

            }else if(  f.getType() == double.class || f.getType() == Double.class  )
            {

                if( sql.indexOf("=")>0 )
                    sql.append(",");
                sql.append(fname).append("=").append( f.getDouble(obj) );

            }

            else if(f.getType() == String.class )
            {
                if( sql.indexOf("=")>0 )
                    sql.append(",");

                Object  v = f.get(obj);
                String value = null;
                if( v == null )
                    value = "";
                else
                    value = v.toString();

                sql.append(" ").append(fname).append("='").append(value).append("' ");


            }

        }
        sql.append(" where keyid='").append(keyid).append("'");

        execSql(sql.toString());




        if( wf != null && wf.get() != null ){

            wf.get().onUpdate(oldv,obj);
        }


    }


    public static Object updateOrInsert(Object obj){

        Class classz = obj.getClass();
        String tname = classz.getSimpleName();
        if( tables.get(tname) == null )
        {
            createTable(classz);
        }
        try {
            Field field = classz.getDeclaredField("keyid");

            if( field.get(obj) != null )
            {
                update(obj);
                return obj;
            }
            else
               return insert(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    public static void deleteAll(Class classz)
    {

        String tname = classz.getSimpleName();
        NLog.i("sqlo table name:%s", tname);
        if( tables.get(tname) == null )
        {
            createTable(classz);
            return;
        }
        else
        {
            execSql("delete from "+tname);
        }

    }

    public static void delete(Object obj) throws NoSuchFieldException, IllegalAccessException {

        Class classz = obj.getClass();
        String tname = classz.getSimpleName();
        NLog.i("sqlo table name:%s", tname);
        if( tables.get(tname) == null )
        {
           return;
        }
        StringBuffer sql = new StringBuffer("delete from ");

        Field f = classz.getDeclaredField("keyid");

        sql.append(tname).append(" where keyid='")
                .append(f.get(obj).toString()).append("'");
        execSql(sql.toString());


    }



    public static  <T> T  selectOne(Class<T> classz, String s) throws Exception {

        List<T> list = select(classz, s);
        if( list.size() > 0 )
            return list.get(0);
        else
            return null;

    }



    public static List<List> select(String sql){


        ArrayList<List> result = new ArrayList<>();
        Cursor cursor=null;
        try{




            cursor = getDatabase().rawQuery(sql, null);
            cursor.moveToFirst();

            while( !cursor.isAfterLast() ){


                for(int i=0;i<cursor.getColumnCount();i++) {

                    List data = new ArrayList();
                    int type = cursor.getType(i);

                    switch (type){

                        case Cursor.FIELD_TYPE_INTEGER:
                            data.add( cursor.getInt(i) );
                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            data.add(cursor.getString(i));
                            break;
                        case Cursor.FIELD_TYPE_FLOAT:
                            data.add(cursor.getFloat(i));
                            break;
                        default:
                            data.add(cursor.getString(i));

                    }
                    result.add(data);



                }
                cursor.moveToNext();
            }
        }catch (Exception e)
        {

            NLog.e(e);
        }

        finally{
            if( cursor != null )
                cursor.close();
        }

        return result;



    }

    public static int sum(String sql){

        Cursor cursor=null;
        try {

            cursor = getDatabase().rawQuery(sql, null);
            cursor.moveToFirst();
            int result = cursor.getInt(0);
            return result;


        }catch (Exception e){

            NLog.e(e);

        }finally{
            if( cursor != null )
                cursor.close();
        }


        return 0;
    }


    private static void read(Cursor cursor,Object val,Field[] fields) throws IllegalAccessException {

        String fname;
        int findex;
        Class ftype;

        for(Field field : fields ){

            fname = field.getName();
            findex = cursor.getColumnIndex(fname);


            if( findex > -1 )
            {

                ftype = field.getType();
                if( ftype == String.class  )
                    field.set(val,cursor.getString(findex));
                else if( ftype == Integer.class  || ftype == int.class)
                    field.set(val,cursor.getInt(findex));
                else if( ftype == Long.class  || ftype == long.class)
                    field.set(val,cursor.getLong(findex));
                else if( ftype == Boolean.class  || ftype == boolean.class)
                    field.set(val,cursor.getInt(findex)==1?true:false);
                else if( ftype == Float.class  || ftype == float.class)
                    field.set(val,cursor.getFloat(findex));
                else if( ftype == Double.class  || ftype == double.class)
                    field.set(val,cursor.getDouble(findex));


            }

        }

    }

    public static <T> List select(Class<T> classz, String where,String order,int page,int pagesize)  {


        String tname = classz.getSimpleName();
        if( tables.get(tname) == null )
        {
            createTable(classz);
        }

        ArrayList<T> result = new ArrayList<T>();
        Cursor cursor=null;
        try{


            cursor = getDatabase().query(tname,null,where,null,null,null,order,(page-1)*pagesize+","+pagesize);

           // cursor = getDatabase().rawQuery(sql, null);
            cursor.moveToFirst();
            T val;
            Field[] fields = classz.getDeclaredFields();

            while( !cursor.isAfterLast() ){

                val = classz.newInstance();
                read(cursor,val,fields);
                result.add(val);
                cursor.moveToNext();

            }
        }catch (Exception e)
        {

            NLog.e(e);
        }

        finally{
            if( cursor != null )
                cursor.close();
        }

        return result;
    }


    public static <T> List select(Class<T> classz, String where,String order)  {


        String tname = classz.getSimpleName();
        if( tables.get(tname) == null )
        {
            createTable(classz);
        }

        ArrayList<T> result = new ArrayList<T>();
        Cursor cursor=null;
        try{


            cursor = getDatabase().query(tname,null,where,null,null,null,order);

           // cursor = getDatabase().rawQuery(sql, null);
            cursor.moveToFirst();
            T val;
            Field[] fields = classz.getDeclaredFields();

            while( !cursor.isAfterLast() ){

                val = classz.newInstance();
                read(cursor,val,fields);
                result.add(val);
                cursor.moveToNext();

            }
        }catch (Exception e)
        {

            NLog.e(e);
        }

        finally{
            if( cursor != null )
                cursor.close();
        }

        return result;
    }

    public static <T> List select(Class<T> classz, String sql)  {


        String tname = classz.getSimpleName();
        if( tables.get(tname) == null )
        {
            createTable(classz);
        }

        ArrayList<T> result = new ArrayList<T>();
        Cursor cursor=null;
        try{

            if( sql == null )
                sql = "select * from "+tname;
            else
            {
                if( !sql.trim().startsWith("select") ) {

                    sql = "select * from " + tname + ( sql.indexOf("where") < 0 ?" where " : " ") + sql;
                }
            }

            if( debug )
                Log.i(TAG,sql);

            cursor = getDatabase().rawQuery(sql, null);
            cursor.moveToFirst();
            T val;
            Field[] fields = classz.getDeclaredFields();

            while( !cursor.isAfterLast() ){

                val = classz.newInstance();
                read(cursor,val,fields);
                result.add(val);
                cursor.moveToNext();

            }
        }catch (Exception e)
        {

            NLog.e(e);
        }

        finally{
            if( cursor != null )
                cursor.close();
        }

        return result;
    }


    public static void beginTransaction(){


        getDatabase().beginTransaction();


    }
    public static void setTransactionSuccessfully(){

        getDatabase().setTransactionSuccessful();

    }

    public static void commitTransaction(){


        getDatabase().endTransaction();

    }
}
