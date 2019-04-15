package com.example.david.ee5application;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.david.ee5application.BluetoothConnectionService.TAG2;

public class In_App_Database  extends SQLiteOpenHelper {
    //TO DO: PACKET IDS ARE NOT ENOUGH TO SELECT APPROPRIATELY, NEED PACKET ID AND SESSION ID.
    public static int storedCount;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Session_Data";

    public static final String TABLE_NAME = "Session_Data";

    public static final String HISTORY_TABLE = "Data_History";

    public static final String Key_Packet_id = "Packet_id";
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
    public static final String Key_Pitch_2 = "Pitch_2";
    public static final String Key_Yaw_2 = "Yaw_2";
    public static final String Key_Roll_2 = "Roll_2";
    public static final String Key_Pitch_3 = "Pitch_3";
    public static final String Key_Yaw_3 = "Yaw_3";
    public static final String Key_Roll_3 = "Roll_3";
    public static final String STRING_EMPTY ="";

    //Table to store all historic sessions Recorded.
    public static final String Key_Table_id = "Table_id";
    private static final String SQL_CREATE_DATA_HISTORY =
            "CREATE TABLE Data_History("+Key_Table_id+" INTEGER PRIMARY KEY);";

    private static final String SQL_CREATE_SESSION_DATA =
            "CREATE TABLE "+TABLE_NAME+"(" +Key_Packet_id + " INTEGER," +
                    Key_Session_id + " INTEGER, " + Key_Mower_id + " INTEGER, " +
                    Key_Date + " TEXT, "+Key_Time+" TEXT, "+Key_Gps_x+" REAL, "+Key_Gps_y+" REAL, "+
                    Key_Joystick+" REAL, "+Key_Oil_Temp+" REAL, "+
                    Key_Pitch_1+" REAL, "+Key_Roll_1+" REAL, "+ Key_Yaw_1+" REAL, "+
                    Key_Pitch_2+" REAL, "+Key_Roll_2+" REAL, "+ Key_Yaw_2+" REAL, "+
                    Key_Pitch_3+" REAL, "+Key_Roll_3+" REAL, "+ Key_Yaw_3+" REAL);";
    private static final String SQL_DELETE_SESSION_DATA = "DROP TABLE IF EXISTS "+TABLE_NAME;

    private static final String TAG = "DATABASE: ";

    public In_App_Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DATA_HISTORY);
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

    public void addNewPacket(
            int packet_id, String Mower_id, String Date, String Time, String GPS_x, String GPS_y, String Joystick,
            String Oil_temp, String Pitch_1, String Roll_1, String Yaw_1, String Pitch_2,
            String Roll_2, String Yaw_2, String Pitch_3, String Roll_3, String Yaw_3
    )
    {
        SessionDataDetails sessionDetails = new SessionDataDetails();
        Log.d(TAG, "Packet_ID is: "+packet_id);
        if (!STRING_EMPTY.equals(Mower_id) &&
                !STRING_EMPTY.equals(Time) &&
                !STRING_EMPTY.equals(GPS_x) &&
                !STRING_EMPTY.equals(GPS_y)) {
            sessionDetails.setPacket_id(packet_id);
            sessionDetails.setKey_Mower_id(Integer.parseInt(Mower_id));
            sessionDetails.setSession_id(storedCount);
            sessionDetails.setKey_Date(Date);
            sessionDetails.setKey_Time(Time);
            sessionDetails.setKey_Gps_x(Double.parseDouble(GPS_x));
            sessionDetails.setKey_Gps_y(Double.parseDouble(GPS_y));
            sessionDetails.setKey_Joystick(Double.parseDouble(Joystick));
            sessionDetails.setKey_Oil_Temp(Double.parseDouble(Oil_temp));
            sessionDetails.setKey_Pitch_1(Double.parseDouble(Pitch_1));
            sessionDetails.setKey_Pitch_2(Double.parseDouble(Pitch_2));
            sessionDetails.setKey_Pitch_3(Double.parseDouble(Pitch_3));
            sessionDetails.setKey_Roll_1(Double.parseDouble(Roll_1));
            sessionDetails.setKey_Roll_2(Double.parseDouble(Roll_2));
            sessionDetails.setKey_Roll_3(Double.parseDouble(Roll_3));
            sessionDetails.setKey_Yaw_1(Double.parseDouble(Yaw_1));
            sessionDetails.setKey_Yaw_2(Double.parseDouble(Yaw_2));
            sessionDetails.setKey_Yaw_3(Double.parseDouble(Yaw_3));

            addSessionData(sessionDetails);

        } else {
            Log.d(TAG, "SOMETHING HAS GONE WRONG AS THERE IS NO GPS/TIME DATA!");
        }
    }

    public long addSessionData(SessionDataDetails data){

        //Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();

        //Create map with movie details to be inserted
        ContentValues session_details = new ContentValues();
        session_details.put(Key_Packet_id, data.getPacket_id());
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
        session_details.put(Key_Roll_1, data.getKey_Roll_1());
        session_details.put(Key_Roll_2, data.getKey_Roll_2());
        session_details.put(Key_Roll_3, data.getKey_Roll_3());
        session_details.put(Key_Yaw_1, data.getKey_Yaw_1());
        session_details.put(Key_Yaw_2, data.getKey_Yaw_2());
        session_details.put(Key_Yaw_3, data.getKey_Yaw_3());

        long newRowId = db.insert(TABLE_NAME, null, session_details);
        db.close();
        return newRowId;
    }

    public List getAllSessionData() {
        List sessionDetailsList = new ArrayList();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        //If necessary, order can be added by:
        //+ " ORDER BY " + KEY_MOVIE_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //if TABLE has rows
        if (cursor.moveToFirst()) {
            //Loop through the table rows
            do {
                SessionDataDetails sessionDetails = new SessionDataDetails();
                sessionDetails.setPacket_id(cursor.getInt(0));
                sessionDetails.setKey_Mower_id(cursor.getInt(1));
                sessionDetails.setSession_id(cursor.getInt(2));
                sessionDetails.setKey_Date(cursor.getString(3));
                sessionDetails.setKey_Time(cursor.getString(4));
                sessionDetails.setKey_Gps_x(cursor.getDouble(5));
                sessionDetails.setKey_Gps_y(cursor.getDouble(6));
                sessionDetails.setKey_Joystick(cursor.getDouble(7));
                sessionDetails.setKey_Oil_Temp(cursor.getDouble(8));
                sessionDetails.setKey_Pitch_1(cursor.getDouble(9));
                sessionDetails.setKey_Pitch_2(cursor.getDouble(10));
                sessionDetails.setKey_Pitch_3(cursor.getDouble(11));
                sessionDetails.setKey_Roll_1(cursor.getDouble(12));
                sessionDetails.setKey_Roll_2(cursor.getDouble(13));
                sessionDetails.setKey_Roll_3(cursor.getDouble(14));
                sessionDetails.setKey_Yaw_1(cursor.getDouble(15));
                sessionDetails.setKey_Yaw_2(cursor.getDouble(16));
                sessionDetails.setKey_Yaw_3(cursor.getDouble(17));

                //Add movie details to list
                sessionDetailsList.add(sessionDetails);
            } while (cursor.moveToNext());
        }
        db.close();
        return sessionDetailsList;
    }

    public SessionDataDetails getSessionData(int PacketId) {

        SessionDataDetails sessionDetails = new SessionDataDetails();
        SQLiteDatabase db = this.getReadableDatabase();
        //specify the columns to be fetched
        String[] columns = {Key_Packet_id, Key_Mower_id, Key_Session_id, Key_Date, Key_Time, Key_Gps_x, Key_Gps_y, Key_Joystick, Key_Oil_Temp, Key_Pitch_1, Key_Roll_1, Key_Yaw_1, Key_Pitch_2, Key_Roll_2, Key_Yaw_2, Key_Pitch_3, Key_Roll_3, Key_Yaw_3};
        //Select condition
        String selection = PacketId + " = ?";
        //Arguments for selection
        String[] selectionArgs = {String.valueOf(PacketId)};


        Cursor cursor = db.query(TABLE_NAME, columns, selection,
                selectionArgs, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();
            sessionDetails.setPacket_id(cursor.getInt(0));
            sessionDetails.setKey_Mower_id(cursor.getInt(1));
            sessionDetails.setSession_id(cursor.getInt(2));
            sessionDetails.setKey_Date(cursor.getString(3));
            sessionDetails.setKey_Time(cursor.getString(4));
            sessionDetails.setKey_Gps_x(cursor.getDouble(5));
            sessionDetails.setKey_Gps_y(cursor.getDouble(6));
            sessionDetails.setKey_Joystick(cursor.getDouble(7));
            sessionDetails.setKey_Oil_Temp(cursor.getDouble(8));
            sessionDetails.setKey_Pitch_1(cursor.getDouble(9));
            sessionDetails.setKey_Pitch_2(cursor.getDouble(10));
            sessionDetails.setKey_Pitch_3(cursor.getDouble(11));
            sessionDetails.setKey_Roll_1(cursor.getDouble(12));
            sessionDetails.setKey_Roll_2(cursor.getDouble(13));
            sessionDetails.setKey_Roll_3(cursor.getDouble(14));
            sessionDetails.setKey_Yaw_1(cursor.getDouble(15));
            sessionDetails.setKey_Yaw_2(cursor.getDouble(16));
            sessionDetails.setKey_Yaw_3(cursor.getDouble(17));

        }
        db.close();
        return sessionDetails;

    }

    public void updateMovie(SessionDataDetails data) {
        SQLiteDatabase db = this.getWritableDatabase();
        String packetIds[] = {String.valueOf(data.getPacket_id())};

        ContentValues session_details = new ContentValues();
        session_details.put(Key_Packet_id, data.getPacket_id());
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
        session_details.put(Key_Roll_1, data.getKey_Roll_1());
        session_details.put(Key_Roll_2, data.getKey_Roll_2());
        session_details.put(Key_Roll_3, data.getKey_Roll_3());
        session_details.put(Key_Yaw_1, data.getKey_Yaw_1());
        session_details.put(Key_Yaw_2, data.getKey_Yaw_2());
        session_details.put(Key_Yaw_3, data.getKey_Yaw_3());
        db.update(TABLE_NAME, session_details, Key_Packet_id + " = ?", packetIds);
        db.close();
    }

    public void deletePacket(int packetId, int sessionId) {
        String packetIds[] = {String.valueOf(packetId), String.valueOf(sessionId)};
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, Key_Packet_id + " = ? AND " + Key_Session_id + " = ?", packetIds);
        db.close();
    }
    public void createHistoricLog()
    {
        //This query checks the existing table to see how many tables already exist in history.
        int NthEntry = 0;
        //FOR THE ASCENDING/DESCENDING ORDER HERE IS IMPORTANT!!!
        String selectQuery = "SELECT MAX("+Key_Table_id+") FROM Data_History";
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor cursor = db_read.rawQuery(selectQuery, null);
        //if table has rows
        if(cursor.moveToFirst()){
            do{
                NthEntry = cursor.getInt(0);
                Log.d(TAG2, "The Nth entry has located the highest value entry of: "+NthEntry);

            } while(cursor.moveToNext());
        }
        db_read.close();

        //This part then creates an entry in the database for the current session.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues session_history = new ContentValues();
        if(NthEntry >= 0)
        {
            session_history.put(Key_Table_id,  NthEntry+1);
        } else {
            session_history.put(Key_Table_id,  0);
        }
        long newRowId = db.insert(HISTORY_TABLE, null, session_history);
        db.close();
        storedCount = NthEntry;
    }

}
