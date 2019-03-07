package com.example.charith.trigym.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.charith.trigym.Entities.Address;
import com.example.charith.trigym.Entities.Member;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    Context context;
    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "TRYGYM";

    private static final String TABLE_MEMBERS = "table_members";
    private static final String TABLE_ADDRESSES = "table_addresses";
    // Common column names
    // Common column names
    private static final String KEY_ID = "id";

    // NOTE_TAGS Table - column names
    private static final String KEY_MEMBER_ID = "member_id";
    private static final String KEY_NAME = "member_name";
    private static final String KEY_MOBILE_1 = "member_mobile_1";
    private static final String KEY_MOBILE_2 = "member_mobile_2";
    private static final String KEY_NIC = "member_nic";
    private static final String KEY_DOB = "member_dob";
    private static final String KEY_AGE = "member_age";
    private static final String KEY_GENDER = "member_gender";
    private static final String KEY_HEIGHT = "member_height";
    private static final String KEY_WEIGHT = "member_weight";
    private static final String KEY_PROFILE_IMAGE_URL = "member_progile_image_url";
    private static final String KEY_HEALTH_CONDITION = "member_health_condition";
    private static final String KEY_MEMBER_ADDRESS_ID = "member_address_id";

    private static final String KEY_ADDRESS_ID = "address_id";
    private static final String KEY_ADDRESS_LINE_1 = "address_line_1";
    private static final String KEY_ADDRESS_LINE_2 = "address_line_2";
    private static final String KEY_ADDRESS_LINE_3 = "address_line_3";
    private static final String KEY_ADDRESS_LINE_CITY = "address_line_city";

    public static final String CREATE_MEMBER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MEMBERS + "("
            + KEY_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MEMBER_ADDRESS_ID + " INTEGER ," + KEY_NAME + " TEXT ," + KEY_MOBILE_1 + " INTEGER,"
            + KEY_MOBILE_2 + " INTEGER," + KEY_NIC + " TEXT," + KEY_DOB + " BLOB," + KEY_AGE + " INTEGER," + KEY_GENDER + " TEXT," + KEY_HEIGHT
            + " INTEGER," + KEY_WEIGHT + " INTEGER," + KEY_PROFILE_IMAGE_URL + " TEXT," + KEY_HEALTH_CONDITION + " TEXT" + ")";

    public static final String CREATE_TABLE_ADDRESSES = "CREATE TABLE IF NOT EXISTS " + TABLE_ADDRESSES + "("
            + KEY_ADDRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_ADDRESS_LINE_1 + " TEXT ," + KEY_ADDRESS_LINE_2 + " TEXT,"
            + KEY_ADDRESS_LINE_3 + " TEXT," + KEY_ADDRESS_LINE_CITY + " TEXT" + ")";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public SQLiteDatabase initDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_MEMBER_TABLE);
        db.execSQL(CREATE_TABLE_ADDRESSES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESSES);

        onCreate(db);
    }

    // Adding new Lawyer
    public void addMember(Member newMember, Integer addressId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MEMBER_ADDRESS_ID, addressId);
        values.put(KEY_NAME, newMember.getName());
        values.put(KEY_MOBILE_1, newMember.getMobile1());
        values.put(KEY_MOBILE_2, newMember.getMobile2());
        values.put(KEY_NIC, newMember.getNIC());
        values.put(KEY_DOB, newMember.getDOB());
        values.put(KEY_AGE, newMember.getAge());
        values.put(KEY_GENDER, newMember.getGender());
        values.put(KEY_HEIGHT, newMember.getHeight());
        values.put(KEY_WEIGHT, newMember.getWeight());
        values.put(KEY_PROFILE_IMAGE_URL, newMember.getProfileImage());
        values.put(KEY_HEALTH_CONDITION, newMember.getHealthCondition());

        db.insert(TABLE_MEMBERS, null, values);

    }

    // Adding new Lawyer
    public Long addAddress(Address address) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ADDRESS_LINE_1, address.getLine1());
        values.put(KEY_ADDRESS_LINE_2, address.getLine2());
        values.put(KEY_ADDRESS_LINE_3, address.getLine3());
        values.put(KEY_ADDRESS_LINE_CITY, address.getCity());

        Long id = db.insert(TABLE_ADDRESSES, null, values);

        return id;
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MEMBERS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Member member = new Member();
                member.setId(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_ID)));
                member.setAddressId(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_ADDRESS_ID)));
                member.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
                member.setMobile1(cursor.getInt(cursor.getColumnIndex(KEY_MOBILE_1)));
                member.setMobile2(cursor.getInt(cursor.getColumnIndex(KEY_MOBILE_2)));
                member.setNIC(cursor.getString(cursor.getColumnIndex(KEY_NIC)));
                member.setDOB(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
                member.setAge(cursor.getInt(cursor.getColumnIndex(KEY_AGE)));
                member.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
                member.setProfileImage(cursor.getString(cursor.getColumnIndex(KEY_PROFILE_IMAGE_URL)));
                member.setHeight(cursor.getFloat(cursor.getColumnIndex(KEY_HEIGHT)));
                member.setWeight(cursor.getFloat(cursor.getColumnIndex(KEY_WEIGHT)));

                // Adding contact to list
                members.add(member);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return members;
    }

    public Address getAddressById(String addressId) {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_ADDRESSES, null, KEY_ADDRESS_ID + "=?", new String[]{addressId}, null, null, null);
        Address address = new Address();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                address.setId(c.getInt(c.getColumnIndex(KEY_ADDRESS_ID)));
                address.setLine1(c.getString(c.getColumnIndex(KEY_ADDRESS_LINE_1)));
                address.setLine2(c.getString(c.getColumnIndex(KEY_ADDRESS_LINE_2)));
                address.setLine3(c.getString(c.getColumnIndex(KEY_ADDRESS_LINE_3)));
                address.setCity(c.getString(c.getColumnIndex(KEY_ADDRESS_LINE_CITY)));

            } while (c.moveToNext());
        }

        c.close();

        return address;
    }
}
