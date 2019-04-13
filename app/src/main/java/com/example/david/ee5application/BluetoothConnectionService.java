package com.example.david.ee5application;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;



public class BluetoothConnectionService extends SQLiteOpenHelper {
    public static int storedCount;

    public static final String TAG2 = "QUAIL: ";

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





    private static final String TAG = "BluetoothConnectionServ";
    private static String STRING_EMPTY = "";
    private static final String appName = "MYAPP";

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString("4ca7ec2b-ef6e-405e-b595-87a0b55226e7");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;

    public BluetoothConnectionService(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }


    /**
     * This thread runs while listening for incoming connections. It behaves
     * like a server-side client. It runs until a connection is accepted
     * (or until cancelled).
     */
    private class AcceptThread extends Thread {
        
        // The local server socket
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try{
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(appName, MY_UUID_INSECURE);

                Log.d(TAG, "AcceptThread: Setting up Server using: " + MY_UUID_INSECURE);
            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            mmServerSocket = tmp;
        }

        public void run(){
            Log.d(TAG, "run: AcceptThread Running.");

            BluetoothSocket socket = null;

            try{
                // This is a blocking call and will only return on a
                // successful connection or an exception
                Log.d(TAG, "run: RFCOM server socket start.....");

                socket = mmServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket accepted connection.");

            }catch (IOException e){
                Log.e(TAG, "AcceptThread: IOException: " + e.getMessage() );
            }

            //
            if(socket != null){
                connected(socket,mmDevice);
            }

            Log.i(TAG, "END mAcceptThread ");
        }

        public void cancel() {
            Log.d(TAG, "cancel: Canceling AcceptThread.");
            try {
                mmServerSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: Close of AcceptThread ServerSocket failed. " + e.getMessage() );
            }
        }

    }

    /**
     * This thread runs while attempting to make an outgoing connection
     * with a device. It runs straight through; the connection either
     * succeeds or fails.
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid) {
            Log.d(TAG, "ConnectThread: started.");
            mmDevice = device;
            deviceUUID = uuid;
        }

        public void run(){
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN mConnectThread ");

            // Get a BluetoothSocket for a connection with the
            // given BluetoothDevice
            try {
                Log.d(TAG, "ConnectThread: Trying to create InsecureRfcommSocket using UUID: "
                        +MY_UUID_INSECURE );
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Could not create InsecureRfcommSocket " + e.getMessage());
            }

            mmSocket = tmp;

            // Always cancel discovery because it will slow down a connection
            mBluetoothAdapter.cancelDiscovery();

            // Make a connection to the BluetoothSocket

            try {
                // This is a blocking call and will only return on a
                // successful connection or an exception
                mmSocket.connect();

                Log.d(TAG, "run: ConnectThread connected.");
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException e1) {
                    Log.e(TAG, "mConnectThread: run: Unable to close connection in socket " + e1.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: Could not connect to UUID: " + MY_UUID_INSECURE );
            }


            connected(mmSocket,mmDevice);
        }
        public void cancel() {
            try {
                Log.d(TAG, "cancel: Closing Client Socket.");
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "cancel: close() of mmSocket in Connectthread failed. " + e.getMessage());
            }
        }
    }



    /**
     * Start the chat service. Specifically start AcceptThread to begin a
     * session in listening (server) mode. Called by the Activity onResume()
     */
    public synchronized void start() {
        Log.d(TAG, "start");

        // Cancel any thread attempting to make a connection
        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null) {
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();
        }
    }

    /**

     AcceptThread starts and sits waiting for a connection.
     Then ConnectThread starts and attempts to make a connection with the other devices AcceptThread.
     **/

    public void startClient(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startClient: Started.");

        //initprogress dialog
        mProgressDialog = ProgressDialog.show(mContext,"Connecting Bluetooth"
                ,"Please Wait...",true);

        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    /**
     Finally the ConnectedThread which is responsible for maintaining the BTConnection, Sending the data, and
     receiving incoming data through input/output streams respectively.
     **/
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "ConnectedThread: Starting.");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            //dismiss the progressdialog when connection is established
            try{
                mProgressDialog.dismiss();
            }catch (NullPointerException e){
                e.printStackTrace();
            }


            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run(){
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int packet_id = 0;
            int bytes; // bytes returned from read()
            addSessionToHistory();
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                // Read from the InputStream
                try {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    bytes = mmInStream.read(buffer);
                    //INPUT STREAM TO DATAPACKET
                    String incomingMessage = new String(buffer, 0, bytes);


                    String Mower_id="";
                    String Date="";
                    String Time="";
                    String GPS_x="";
                    String GPS_y="";
                    String Joystick="";
                    String Oil_temp="";
                    String Pitch_1="";
                    String Roll_1="";
                    String Yaw_1="";
                    String Pitch_2="";
                    String Roll_2="";
                    String Yaw_2="";
                    String Pitch_3="";
                    String Roll_3="";
                    String Yaw_3="";

                    Log.d(TAG, "Input Stream: "+incomingMessage);

                    String[] packet = incomingMessage.split("#");
                    String first =  packet[0];
                    Log.d(TAG, "The first packet selected: "+first);
                    String second = packet[1];

                    Log.d(TAG, "The Second packet selected: "+second);
                        if(first.contains("%") && first.contains(">")){
                            String[] separated = first.split("\\*");
                            Log.d(TAG, "WE DID THIS 1");
                            Mower_id =  separated[1];
                            Date = separated[2];
                            Time =  separated[3];
                            GPS_x = separated[4];
                            GPS_y = separated[5];
                            Joystick = separated[6];
                            Oil_temp = separated[7];
                            Pitch_1 = separated[8];
                            Roll_1 = separated[9];
                            Yaw_1 = separated[10];
                            Pitch_2 = separated[11];
                            Roll_2 = separated[12];
                            Yaw_2 = separated[13];
                            Pitch_3 = separated[14];
                            Roll_3 = separated[15];
                            Yaw_3 = separated[16];
                         } else if (second.contains("%")){
                            String[] separated = second.split("\\*");
                            Log.d(TAG, "WE DID THIS 2");
                            Mower_id =  separated[1];
                            Date = separated[2];
                            Time =  separated[3];
                            GPS_x = separated[4];
                            GPS_y = separated[5];
                            Joystick = separated[6];
                            Oil_temp = separated[7];
                            Pitch_1 = separated[8];
                            Roll_1 = separated[9];
                            Yaw_1 = separated[10];
                            Pitch_2 = separated[11];
                            Roll_2 = separated[12];
                            Yaw_2 = separated[13];
                            Pitch_3 = separated[14];
                            Roll_3 = separated[15];
                            Yaw_3 = separated[16];
                        } else {
                            Log.d(TAG, "WE DID THIS NONE!");
                        }
                    Log.d(TAG, "MOWER ID: " + Mower_id);
                    Log.d(TAG, "Time: " + Time);
                    Log.d(TAG, "Yaw_3: " + Yaw_3);
                    addNewPacket(packet_id, Mower_id, Date, Time, GPS_x, GPS_y, Joystick, Oil_temp, Pitch_1, Roll_1, Yaw_1, Pitch_2, Roll_2, Yaw_2, Pitch_3, Roll_3, Yaw_3);
                    Log.d(TAG, "Packet addition was successful");
                    packet_id++;
                    //getPacket(1);


                } catch (IOException e) {
                    Log.e(TAG, "write: Error reading Input Stream. " + e.getMessage() );
                    break;
                }
            }
        }

        //Call this from the main activity to send data to the remote device
        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to outputstream: " + text);
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    /**
     * Write to the ConnectedThread in an unsynchronized manner
     *
     * @param out The bytes to write
     * @see ConnectedThread#write(byte[])
     */
    public void write(byte[] out) {
        // Create temporary object
        ConnectedThread r;

        // Synchronize a copy of the ConnectedThread
        Log.d(TAG, "write: Write Called.");
        //perform the write
        mConnectedThread.write(out);
    }
    //Not used right now
    /*public static String readUntilChar(InputStream stream, char target) {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
            char a;
            while ((a = (char) buffer.read()) != '#') {
                char c = (char) a;
                if (c == target)
                    break;
                sb.append(c);
            }
            System.out.println(sb.toString());
        } catch (IOException e) {
        }
        return sb.toString();
    }

*/





    private void addNewPacket(
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


    @Override
    public void onCreate(SQLiteDatabase db){
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
    //adds new packet
    public void addSessionToHistory()
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

    public SessionDataDetails getPacket(int session_id) {

        SessionDataDetails dataPacket = new SessionDataDetails();
        SQLiteDatabase db = this.getReadableDatabase();
        //specify the columns to be fetched
        String[] columns = {Key_Session_id, Key_Mower_id, Key_Time, Key_Gps_x, Key_Gps_y, Key_Joystick, Key_Oil_Temp, Key_Pitch_1, Key_Roll_1, Key_Yaw_1, Key_Pitch_2, Key_Roll_2, Key_Yaw_2, Key_Pitch_3, Key_Roll_3, Key_Yaw_3};
        //Select condition
        String selection = Key_Session_id + " = ?";
        //Arguments for selection
        String[] selectionArgs = {String.valueOf(Key_Session_id)};


        Cursor cursor = db.query(TABLE_NAME, columns, selection,
                selectionArgs, null, null, null);
        if (null != cursor) {
            cursor.moveToFirst();
            dataPacket.setSession_id(cursor.getInt(0));
            Log.d(TAG, "PACKET RETURNS for the Session ID: "+cursor.getInt(0));
            dataPacket.setKey_Mower_id(cursor.getInt(1));
            dataPacket.setKey_Time(cursor.getString(2));
            dataPacket.setKey_Gps_x(cursor.getDouble(3));
            Log.d(TAG, "PACKET RETURNS for the GPSX: "+cursor.getDouble(3));
            dataPacket.setKey_Gps_y(cursor.getDouble(4));

        }
        db.close();
        return dataPacket;

    }

}
