package com.example.administrator.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs
            , String groupBy, String having, String orderBy){
        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public long insert(String table, ContentValues values){
        return db.insert(table, null, values);
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs){
        return db.update(table, values, whereClause, whereArgs);
    }

    public int delete(String table, String whereClause, String[] whereArgs){
        return db.delete(table, whereClause, whereArgs);
    }
}
