package com.cookandroid.password;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // 데이터베이스 정보
    private static final String DATABASE_NAME = "PasswordManager.db";
    private static final int DATABASE_VERSION = 1;

    // 테이블 및 컬럼 정보
    private static final String TABLE_NAME = "passwords";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_SITE_NAME = "site_name";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 생성 SQL문
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_SITE_NAME + " TEXT, "
                + COLUMN_USER_ID + " TEXT, "
                + COLUMN_PASSWORD + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 기존 테이블 제거 후 새로 생성
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // 1. 데이터 추가 (C)
    public boolean addPassword(PasswordModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SITE_NAME, model.getSiteName());
        values.put(COLUMN_USER_ID, model.getUserId());
        values.put(COLUMN_PASSWORD, model.getPassword());

        long result = db.insert(TABLE_NAME, null, values);
        db.close();

        // insert 실패 시 -1 반환
        return result != -1;
    }

    // 2. 전체 데이터 조회 (R)
    public List<PasswordModel> getAllPasswords() {
        List<PasswordModel> passwordList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_ID + " DESC";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String siteName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SITE_NAME));
                String userId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD));

                PasswordModel model = new PasswordModel(id, siteName, userId, password);
                passwordList.add(model);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return passwordList;
    }

    // 3. 데이터 삭제 (D)
    public void deletePassword(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
