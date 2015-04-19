package com.gayathri.enterpriselinchpin;

import android.database.sqlite.SQLiteDatabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;


public class Db extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "messages";
    private static final String MESSAGES_TABLE = "messages";

    //contact_messages column names
    private static final String MESSAGE = "message";
    private static final String RECEIVED = "received";
    private static final String CTIME = "ctime";
    private static final String EMAIL = "email";

    //One Time Instantiation
    static Db instance = null;
    static SQLiteDatabase db = null;

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_MSG_TABLE = "CREATE TABLE IF NOT EXISTS " + MESSAGES_TABLE + " ( "
                + EMAIL + " TEXT, "
                + RECEIVED + " INTEGER, "
                + CTIME + " DATE DEFAULT (datetime('now','localtime')), "
                + MESSAGE + " TEXT "
                + ")";
        db.execSQL(CREATE_CONTACTS_MSG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE);
        onCreate(db);
    }

    public static void init(Context context) {
        if (null == instance) {
            instance = new Db(context);
        }
    }

    public static void deactivate() {
        if (null != db && db.isOpen()) {
            db.close();
        }
        db = null;
        instance = null;
    }

    public static SQLiteDatabase getDb() {
        if (null == db) {
            db = instance.getWritableDatabase();
        }
        return db;
    }

    public Db(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static Cursor getMessages(String email) {
        String query = "SELECT " + MESSAGE + "," + RECEIVED + " FROM " + MESSAGES_TABLE + " WHERE "
                + EMAIL + "='" + email + "' ORDER BY " + CTIME;

        return getDb().rawQuery(query, null);
    }

    public static void putMessage(Context context, String email, String message, boolean received){
        init(context);
        int cid ;
        SQLiteDatabase db = getDb();
        ContentValues values = new ContentValues();
        values.put(EMAIL, email);
        values.put(MESSAGE, message);
        values.put(RECEIVED, received ? 1 : 0);
        db.insert(MESSAGES_TABLE, null, values);
        db.close();
        deactivate();
    }
}
