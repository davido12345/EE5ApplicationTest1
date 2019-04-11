package com.example.david.ee5application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaCas;

import java.util.ArrayList;
import java.util.List;

public class SaveDataInApp extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Session Data";

    public static final String TABLE_NAME = "Session Data";

    public static final String Key_Session_id = "Session_id";
    public static final String Key_Mower_id = "Mower_id";
    public static final String Key_Date = "Date";
    public static final String Key_Time = "Time";
    public static final String Key_Gps_x = "Gps_x";
    public static final String Key_Gps_y = "Gps_y";
    public static final String Key_Joystick = "Joystick";
    public static final String Key_Oil_Temp = "Oil_temp";
    public static final String Key_Pitch_1 = "Pitch_1";
    public static final String Key_Yaw_1 = "Yaw_1";
    public static final String Key_Roll_1 = "Roll_1";
    public static final String Key_Pitch_2 = "Pitch_1";
    public static final String Key_Yaw_2 = "Yaw_1";
    public static final String Key_Roll_2 = "Roll_1";
    public static final String Key_Pitch_3 = "Pitch_1";
    public static final String Key_Yaw_3 = "Yaw_1";
    public static final String Key_Roll_3 = "Roll_1";

    private static final String SQL_CREATE_SESSION_DATA =
            "CREATE TABLE movies (" + Key_Session_id + " INTEGER PRIMARY KEY, " + Key_Mower_id + " INTEGER, " +
                    Key_Date + " TEXT, "+Key_Time+" TEXT, "+Key_Gps_x+" REAL, "+Key_Gps_y+" REAL, "+
                    Key_Joystick+" REAL, "+Key_Oil_Temp+" REAL, "+Key_Pitch_1+" REAL, "+Key_Roll_1+" REAL, "+
                    Key_Yaw_1+" REAL, "+Key_Pitch_2+" REAL, "+Key_Roll_2+" REAL, "+
                    Key_Yaw_2+" REAL, "+Key_Pitch_3+" REAL, "+Key_Roll_3+" REAL, "+
                    Key_Yaw_3+" REAL);";

    private static final String SQL_DELETE_SESSION_DATA = "DROP TABLE IF EXISTS "+TABLE_NAME;

    public SaveDataInApp(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_SESSION_DATA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        //Drop the table while upgrading the database
        // such as adding new column or changing existing constraint
        db.execSQL(SQL_DELETE_SESSION_DATA);
        this.onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.onUpgrade(db, oldVersion, newVersion);
    }
    //adds new packet
    public long addSessionData(SessionDataDetails data){

            //Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create map with movie details to be inserted
        ContentValues session_details = new ContentValues();
        session_details.put(Key_Mower_id, data.getKey_Mower_id());
        session_details.put(Key_Session_id, data.getSession_id());
        session_details.put(Key_Gps_x, data.getKey_Gps_x());
        session_details.put(Key_Gps_y, data.getKey_Gps_y());
        session_details.put(Key_Date, data.getKey_Date());
        session_details.put(Key_Time, data.getKey_Time());
        session_details.put(Key_Oil_Temp, data.getKey_Oil_Temp());
        session_details.put(Key_Joystick, data.getKey_Joystick());
        session_details.put(Key_Pitch_1, data.getKey_Pitch_1());
        session_details.put(Key_Pitch_1, data.getKey_Pitch_2());
        session_details.put(Key_Pitch_3, data.getKey_Pitch_3());
        session_details.put(Key_Roll_1, data.getKey_Pitch_1());
        session_details.put(Key_Roll_2, data.getKey_Pitch_2());
        session_details.put(Key_Roll_3, data.getKey_Pitch_3());
        session_details.put(Key_Yaw_1, data.getKey_Yaw_1());
        session_details.put(Key_Yaw_2, data.getKey_Yaw_2());
        session_details.put(Key_Yaw_3, data.getKey_Yaw_3());

        long newRowId = db.insert(TABLE_NAME, null, session_details);
        db.close();
        return newRowId;
    }
    public List getAllSessionData(){
        List sessionDataList = new ArrayList();
        //FOR THE ASCENDING/DESCENDING ORDER HERE IS IMPORTANT!!!
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY "+Key_Session_id+" DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //if table has rows
        if(cursor.moveToFirst()){
            do{
                SessionDataDetails sessionDetails = new SessionDataDetails();
                sessionDetails.setSession_id(cursor.getInt(0));
                sessionDetails.setKey_Mower_id(cursor.getInt(1));
                sessionDetails.setKey_Time(cursor.getString(2));
                sessionDetails.setKey_Gps_x(cursor.getDouble(3));
                sessionDetails.setKey_Gps_y(cursor.getDouble(4));
                sessionDetails.setKey_Joystick(cursor.getDouble(5));
                sessionDetails.setKey_Oil_Temp(cursor.getDouble(6));
                sessionDetails.setKey_Pitch_1(cursor.getDouble(7));
                sessionDetails.setKey_Roll_1(cursor.getDouble(8));
                sessionDetails.setKey_Yaw_1(cursor.getDouble(9));
                sessionDetails.setKey_Pitch_2(cursor.getDouble(10));
                sessionDetails.setKey_Roll_2(cursor.getDouble(11));
                sessionDetails.setKey_Yaw_2(cursor.getDouble(12));
                sessionDetails.setKey_Pitch_3(cursor.getDouble(13));
                sessionDetails.setKey_Roll_3(cursor.getDouble(14));
                sessionDetails.setKey_Yaw_3(cursor.getDouble(15));

                sessionDataList.add(sessionDetails);
            } while(cursor.moveToNext());
        }
        db.close();
        return sessionDataList;
    }
    //Not really needed so I'll skip it for now.
    /*public SessionDataDetails getPacket(int SessionDataId){

    }
    public void updateSessionData(SessionDataDetails data){

    }
    public void deleteSessionData(int packetID){

    }
    */
}
