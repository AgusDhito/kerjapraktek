package com.example.user.mana_livechatv2.helper;

/**
 * Created by user on 27/07/2016.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.user.mana_livechatv2.model.ChatRoom;

public class Db_ChatRoom extends SQLiteOpenHelper {
    private String TAG =Db_ChatRoom.class.getSimpleName();

    public static final String DATABASE_NAME = "Db_ChatRoom.db";
    public static final String TABLE_NAME = "chat_rooms";
    public static final String CR_ID = "id";
    public static final String CR_NAME = "name";
    public static final String CR_LAST_MESSAGE = "last_message";
    public static final String CR_UNREAD_COUNT = "unread_count";
    public static final String CR_TIMESTAMP = "timestamp";
    private HashMap hp;

    public Db_ChatRoom(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table chat_rooms " +
                        "(id string primary key,name string, last_message string, unread_count string, timestamp string)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS chat_rooms");
        onCreate(db);
    }
    public void drop() {
        SQLiteDatabase db_write = this.getWritableDatabase();
//        db_write.execSQL("DROP TABLE IF EXISTS chat_rooms");
//        Log.d("debug_SqLite", "drop table");
    }

    public boolean insertChatRoom  (ChatRoom cr)
    {
        SQLiteDatabase db_write = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CR_ID, cr.getId());
        contentValues.put(CR_NAME, cr.getName());
        contentValues.put(CR_LAST_MESSAGE, cr.getLastMessage());
        contentValues.put(CR_UNREAD_COUNT, Integer.toString(cr.getUnreadCount()));
        contentValues.put(CR_TIMESTAMP, cr.getTimestamp());
        SQLiteDatabase db_read = this.getReadableDatabase();
        Cursor res = db_read.rawQuery("select * from chat_rooms where id="+cr.getId()+"", null);
        //Jika datanya sudah ada, cukup diupdate aja
        if (res.getCount() != 0) {
//            db_write.update("chat_rooms", contentValues, "id = "+cr.getId()+"", null);
//            Log.d(TAG, "update database");
        }
        else {
            //Jika data belom ada, maka lakukan insert data
            db_write.insert("chat_rooms", null, contentValues);
            Log.d(TAG, "insert into database");
        }
        return true;
    }

    public boolean updateChatRoom (String unread, String message, String id) {
        SQLiteDatabase db_write = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CR_UNREAD_COUNT, unread);
        contentValues.put(CR_LAST_MESSAGE, message);
        db_write.update("chat_rooms", contentValues, "id = "+id+"", null);
        Log.d(TAG, "update database");
        return true;
    }
//    public Cursor getData(int id){
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts where id="+id+"", null );
//        return res;
//    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }
//
//    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("name", name);
//        contentValues.put("phone", phone);
//        contentValues.put("email", email);
//        contentValues.put("street", street);
//        contentValues.put("place", place);
//        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
//        return true;
//    }

//    public Integer deleteContact (Integer id)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete("contacts",
//                "id = ? ",
//                new String[] { Integer.toString(id) });
//    }

    public ArrayList<ChatRoom> getAllChatRooms()
    {
        ArrayList<ChatRoom> array_list = new ArrayList<ChatRoom>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from chat_rooms", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ChatRoom cr = new ChatRoom();
            cr.setId(res.getString(res.getColumnIndex(CR_ID)));
            cr.setName(res.getString(res.getColumnIndex(CR_NAME)));
            cr.setLastMessage(res.getString(res.getColumnIndex(CR_LAST_MESSAGE)));
            cr.setUnreadCount(Integer.parseInt(res.getString(res.getColumnIndex(CR_UNREAD_COUNT))));
            cr.setTimestamp(res.getString(res.getColumnIndex(CR_TIMESTAMP)));
            array_list.add(cr);
            res.moveToNext();
        }
        return array_list;
    }
}