package com.example.administrator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    /**
     * 数据库名
     */
    private static final String DATABASE_NAME = "txtread.db";

    /**
     * 数据库版本
     */
    private static final int DATABASE_VERSION = 1;

    public static final String SHUJIA_TABLE = "myshujia";

    public static final String DATA_TABLE = "mydata";

    private final String CONFIG_TABLE = "myconfig";

    private final String SHUJIA_CREATE = "CREATE TABLE "
            + SHUJIA_TABLE + " (" + MobileColumn.SHUJIA_ID
            + " integer primary key autoincrement,"
            + MobileColumn.SHUJIA_URL + " text,"
            + MobileColumn.SHUJIA_NAME + " text,"
            + MobileColumn.SHUJIA_LASTURL + " text,"
            + MobileColumn.SHUJIA_ZHANGJIE + " integer,"
            + MobileColumn.SHUJIA_ZONGZHANGJIE + " integer" + ");";

    private final String DATA_CREATE = "CREATE TABLE "
            + DATA_TABLE + " (" + MobileColumn.DATA_ID
            + " integer primary key autoincrement,"
            + MobileColumn.DATA_URL + " text,"
            + MobileColumn.DATA_VALUE + " text,"
            + MobileColumn.DATA_HOMEURL + " text" + ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SHUJIA_CREATE);
        sqLiteDatabase.execSQL(DATA_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
