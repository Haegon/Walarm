package com.gohn.walarm.Manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.gohn.walarm.Model.Alarm;
import com.gohn.walarm.Model.Weather;

import java.util.ArrayList;

/**
 * Created by HaegonKoh on 2015. 6. 22..
 */
public class AlarmDBMgr {

    public static final String DB_NAME = "Alarms.db";
    public static final String TABLE_ALARM = "Alarms";
    public static final String TABLE_SEQ = "AlarmSeq";
    public static final String TABLE_RING = "AlarmRing";

    public static final String NAME = "name";
    public static final String HOUR = "hour";
    public static final String MIN = "minute";
    public static final String DAYS = "days";
    public static final String OPTIONS = "options";
    public static final String ISON = "ison";

    public static final String CTIME = "ctime";

    public static final String WEATHER = "weather";
    public static final String URI = "uri";

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
                "CREATE TABLE IF NOT EXISTS " + TABLE_ALARM +
                        "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        NAME + " TEXT," +
                        HOUR + " INTEGER," +
                        MIN + " INTEGER," +
                        DAYS + " INTEGER," +
                        OPTIONS + " INTEGER," +
                        ISON + " INTEGER);");
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_SEQ +
                        "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT, ctime INTEGER);");
        mDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS " + TABLE_RING +
                        "(" + "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        WEATHER + " INTEGER," +
                        URI + " TEXT);");

        if (isRingEmpty()) {
            for (int i = 0; i < Weather.WEATHERS.length; i++)
                insertRing(i, "content://settings/system/notification_sound");
        }
    }

    public long insert(ContentValues addRowValue) {

        return mDatabase.insert(TABLE_ALARM, null, addRowValue);
    }

    public Uri getRing(int weather) {

        String query = String.format("select uri from AlarmRing where weather=?");
        Cursor c = rawQuery(query, new String[]{String.format("%d", weather)});

        if (c != null) {
            c.moveToFirst();
            return Uri.parse(c.getString(0));
        }
        return Uri.parse("");
    }

    public long insertRing(int weather, String uri) {

        ContentValues cv = new ContentValues();
        cv.put(WEATHER, weather);
        cv.put(URI, uri);

        return mDatabase.insert(TABLE_RING, null, cv);
    }

    public int updateRing(int weather, String uri) {

        ContentValues cv = new ContentValues();
        cv.put(URI, uri);

        return  mDatabase.update(TABLE_RING, cv, "weather=?", new String[]{String.format("%d", weather)});
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return mDatabase.query(TABLE_ALARM, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public int update(ContentValues updateRowValue, String whereClause, String[] whereAgs) {

        return mDatabase.update(TABLE_ALARM, updateRowValue, whereClause, whereAgs);
    }

    public int delete(String whereClause, String[] whereAgs) {
        return mDatabase.delete(TABLE_ALARM, whereClause, whereAgs);
    }

    public Cursor rawQuery(String clause, String[] type2) {
        return mDatabase.rawQuery(clause, type2);
    }

    public long increaseSeq(ContentValues addRowValue) {

        return mDatabase.insert(TABLE_SEQ, null, addRowValue);
    }

    public void addAlarm(Alarm alarm) {

        ContentValues cv = new ContentValues();
        cv.put(AlarmDBMgr.NAME, alarm.Name);
        cv.put(AlarmDBMgr.HOUR, alarm.Hour);
        cv.put(AlarmDBMgr.MIN, alarm.Minute);
        cv.put(AlarmDBMgr.DAYS, alarm.Days);
        cv.put(AlarmDBMgr.ISON, alarm.IsOn);
        cv.put(AlarmDBMgr.OPTIONS, alarm.Options);
        insert(cv);

        ContentValues seq = new ContentValues();
        seq.put(AlarmDBMgr.CTIME, (int) System.currentTimeMillis() / 1000);
        increaseSeq(seq);
    }

    public Alarm getAlarm(int no) {

        String query = String.format("select _id, name, hour, minute, days, options, ison from Alarms where _id=?");
        Cursor c = rawQuery(query, new String[]{String.format("%d", no)});

        if (c != null) {
            c.moveToFirst();
        }

        Alarm a = new Alarm();
        a.No = c.getInt(0);
        a.Name = c.getString(1);
        a.Hour = c.getInt(2);
        a.Minute = c.getInt(3);
        a.Days = c.getInt(4);
        a.Options = c.getInt(5);
        a.IsOn = c.getInt(6);

        return a;
    }

    public void delAlarm(int no) {
        delete("_id=?", new String[]{String.format("%d", no)});
    }


    public ArrayList<Alarm> getAlarms() {

        String query = String.format("select _id, name, hour, minute, days, options, ison from Alarms");
        Cursor c = rawQuery(query, null);

        ArrayList<Alarm> alarmList = new ArrayList<Alarm>();

        if (c != null) {
            while (c.moveToNext()) {
                Alarm a = new Alarm();
                a.No = c.getInt(0);
                a.Name = c.getString(1);
                a.Hour = c.getInt(2);
                a.Minute = c.getInt(3);
                a.Days = c.getInt(4);
                a.Options = c.getInt(5);
                a.IsOn = c.getInt(6);

                alarmList.add(a);
            }
        }
        return alarmList;
    }

    public void onoffAlarm(Alarm alarm) {

        ContentValues cv = new ContentValues();
        cv.put(AlarmDBMgr.ISON, alarm.IsOn);
        update(cv, "_id=?", new String[]{String.format("%d", alarm.No)});
    }

    public int getLastNo() {

        String query = String.format("select _id from AlarmSeq order by _id desc limit 1 ");
        Cursor c = rawQuery(query, new String[]{});

        if (c.getCount() == 0) {
            return 0;
        }

        c.moveToFirst();

        return c.getInt(0);
    }

    // 벨소리가 들어있는지 체크
    // 벨소리가 없으면 기본 벨소리를 insert해준다.
    public boolean isRingEmpty() {

        String query = String.format("select _id from AlarmRing");
        Cursor c = rawQuery(query, new String[]{});

        return c.getCount() == 0;
    }
}
