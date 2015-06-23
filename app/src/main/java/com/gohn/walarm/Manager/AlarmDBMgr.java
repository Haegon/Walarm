package com.gohn.walarm.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.gohn.walarm.Model.Alarm;

import java.util.ArrayList;

/**
 * Created by HaegonKoh on 2015. 6. 22..
 */
public class AlarmDBMgr {

    public static final String DB_NAME = "Alarms.db";
    public static final String TABLE_NAME = "Alarms";

    public static final String NOON = "afternoon";
    public static final String HOUR = "hour";
    public static final String MIN = "minute";
    public static final String ISON = "ison";
 //   public static final String WORD = "word";
 //  public static final String MEANING = "meaning";

    public ArrayList<Alarm> alarmList = new ArrayList<Alarm>();
    static final int DB_VERSION = 1;

    Context mContext = null;

    private static AlarmDBMgr mDBManager = null;
    private SQLiteDatabase mDatabase = null;

    public static AlarmDBMgr getInstance(Context context) {
        if (mDBManager == null) {
            mDBManager = new AlarmDBMgr(context);
        }
        return mDBManager;
    }

    public static AlarmDBMgr getInstance() {
        return mDBManager;
    }

    private AlarmDBMgr(Context context) {
        mContext = context;
        mDatabase = context.openOrCreateDatabase(DB_NAME, Context.MODE_PRIVATE, null);

        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                        "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        NOON + " INTEGER," +
                        HOUR + " INTEGER," +
                        MIN + " INTEGER," +
                        ISON + " INTEGER);");
    }

    public long insert(ContentValues addRowValue) {

        return mDatabase.insert(TABLE_NAME, null, addRowValue);
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return mDatabase.query(TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public int update(ContentValues updateRowValue, String whereClause, String[] whereAgs) {

        return mDatabase.update(TABLE_NAME, updateRowValue, whereClause, whereAgs);
    }

    public int delete(String whereClause, String[] whereAgs) {
        return mDatabase.delete(TABLE_NAME, whereClause, whereAgs);
    }

    public Cursor rawQuery(String clause, String[] type2) {
        return mDatabase.rawQuery(clause, type2);
    }

    public void addAlarm(Alarm alarm) {

        ContentValues cv = new ContentValues();
        cv.put(AlarmDBMgr.NOON, alarm.Afternoon);
        cv.put(AlarmDBMgr.HOUR, alarm.Hour);
        cv.put(AlarmDBMgr.MIN, alarm.Minute);
        cv.put(AlarmDBMgr.ISON, alarm.IsOn);
        insert(cv);
    }

    public Alarm getAlarm(int no) {

        String query = String.format("select _id, afternoon, hour, minute, ison from Alarms where _id=?");
        Cursor c = rawQuery(query, new String[]{String.format("%d", no)});

        if (c != null) {
            c.moveToFirst();
        }

        Alarm a = new Alarm();
        a.No = c.getInt(0);
        a.Afternoon = c.getInt(1);
        a.Hour = c.getInt(2);
        a.Minute = c.getInt(3);
        a.IsOn = c.getInt(4);

        return a;
    }

    public ArrayList<Alarm> getAlarms() {

        String query = String.format("select _id, afternoon, hour, minute, ison from Alarms");
        Cursor c = rawQuery(query, null);

        ArrayList<Alarm> alarmList = new ArrayList<Alarm>();

        if (c != null) {
            while (c.moveToNext()) {
                Alarm a = new Alarm();
                a.No = c.getInt(0);
                a.Afternoon = c.getInt(1);
                a.Hour = c.getInt(2);
                a.Minute = c.getInt(3);
                a.IsOn = c.getInt(4);

                alarmList.add(a);
            }
        }
        return alarmList;
    }

    public void onoffAlarm(Alarm alarm) {

        ContentValues cv = new ContentValues();
        cv.put(AlarmDBMgr.ISON, alarm.IsOn);
        update(cv, "_id=?", new String[]{String.format("%d",alarm.No)});
    }

    public int getLastNo() {

        String query = String.format("select _id from Alarms order by _id desc limit 1 ");
        Cursor c = rawQuery(query, new String[]{});

        if ( c.getCount() == 0 ) {
            return 0;
        }

        c.moveToFirst();

        return  c.getInt(0);
    }
}
