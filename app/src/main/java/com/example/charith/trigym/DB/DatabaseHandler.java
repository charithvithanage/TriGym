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
    private static final String TABLE_HEALTH_CONDITION = "table_health_condition";
    private static final String TABLE_MEMBER_HEALTH = "table_member_health";
    // Common column names
    // Common column names
    private static final String KEY_ID = "id";

    // NOTE_TAGS Table - column names
    private static final String KEY_MEMBER_ID = "member_id";
    private static final String KEY_FIRST_NAME = "member_first_name";
    private static final String KEY_LAST_NAME = "member_last_name";
    private static final String KEY_SURNAME = "member_surname";
    private static final String KEY_MOBILE_1 = "member_mobile_1";
    private static final String KEY_MOBILE_2 = "member_mobile_2";
    private static final String KEY_NIC = "member_nic";
    private static final String KEY_DOB = "member_dob";
    private static final String KEY_AGE = "member_age";
    private static final String KEY_GENDER = "member_gender";
    private static final String KEY_MARRIED_STATUS = "member_married_status";
    private static final String KEY_HEIGHT = "member_height";
    private static final String KEY_WEIGHT = "member_weight";
    private static final String KEY_PROFILE_IMAGE_URL = "member_progile_image_url";
    private static final String KEY_COMMENT = "member_health_condition";
    private static final String KEY_MEMBER_ADDRESS_ID = "member_address_id";

    private static final String KEY_ADDRESS_ID = "address_id";
    private static final String KEY_ADDRESS_LINE_1 = "address_line_1";
    private static final String KEY_ADDRESS_LINE_2 = "address_line_2";
    private static final String KEY_ADDRESS_LINE_3 = "address_line_3";
    private static final String KEY_ADDRESS_LINE_CITY = "address_line_city";

    private static final String KEY_DIABETES = "diabetes";
    private static final String KEY_CHOLESTEROL = "cholesterol";
    private static final String KEY_HIGH_BLOOD_PRESSURE = "high_blood_pressure";
    private static final String KEY_LOW_BLOOD_PRESSURE = "low_blood_pressure";
    private static final String KEY_HEART_PROBLEM = "heart_problem";
    private static final String KEY_CHEST_PAIN = "chest_pain";
    private static final String KEY_FAINTING = "fainting_spells";
    private static final String KEY_BACK_PAIN = "back_pain";
    private static final String KEY_MEDICATION = "medication";
    private static final String KEY_OTHER_ILLNESS = "other_illness";
    private static final String KEY_SWOLLEN = "swollen";
    private static final String KEY_ARTHRITIS = "arthritis";
    private static final String KEY_HERNIA = "hernia";


    public static final String CREATE_MEMBER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MEMBERS + "("
            + KEY_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_MEMBER_ADDRESS_ID + " INTEGER ," + KEY_FIRST_NAME + " TEXT ," + KEY_SURNAME + " TEXT ," + KEY_LAST_NAME + " TEXT ," + KEY_MARRIED_STATUS + " TEXT ," + KEY_MOBILE_1 + " INTEGER,"
            + KEY_MOBILE_2 + " INTEGER," + KEY_NIC + " TEXT," + KEY_DOB + " BLOB," + KEY_AGE + " INTEGER," + KEY_GENDER + " TEXT," + KEY_HEIGHT
            + " INTEGER," + KEY_WEIGHT + " INTEGER," + KEY_PROFILE_IMAGE_URL + " TEXT," + KEY_COMMENT + " TEXT," + KEY_DIABETES + " BOOLEAN," +
            KEY_CHOLESTEROL + " BOOLEAN," + KEY_HIGH_BLOOD_PRESSURE + " INTEGER DEFAULT 0," + KEY_LOW_BLOOD_PRESSURE + " INTEGER DEFAULT 0," + KEY_HEART_PROBLEM + " INTEGER DEFAULT 0," + KEY_CHEST_PAIN + " INTEGER DEFAULT 0," + KEY_FAINTING + " INTEGER DEFAULT 0,"
            + KEY_BACK_PAIN + " INTEGER DEFAULT 0," + KEY_MEDICATION + " INTEGER DEFAULT 0," + KEY_OTHER_ILLNESS + " INTEGER DEFAULT 0," + KEY_SWOLLEN + " INTEGER DEFAULT 0," + KEY_ARTHRITIS + " INTEGER DEFAULT 0," + KEY_HERNIA + " INTEGER DEFAULT 0" + ")";

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
    public void addMember(Member newMember) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MEMBER_ADDRESS_ID, newMember.getAddressId());
        values.put(KEY_FIRST_NAME, newMember.getFirstName());
        values.put(KEY_SURNAME, newMember.getSurName());
        values.put(KEY_LAST_NAME, newMember.getLastName());
        values.put(KEY_MOBILE_1, newMember.getMobile1());
        values.put(KEY_MOBILE_2, newMember.getMobile2());
        values.put(KEY_NIC, newMember.getNIC());
        values.put(KEY_DOB, newMember.getDOB());
        values.put(KEY_AGE, newMember.getAge());
        values.put(KEY_MARRIED_STATUS, newMember.getMarriedStatus());
        values.put(KEY_GENDER, newMember.getGender());
        values.put(KEY_HEIGHT, newMember.getHeight());
        values.put(KEY_WEIGHT, newMember.getWeight());
        values.put(KEY_PROFILE_IMAGE_URL, newMember.getProfileImage());
        values.put(KEY_DIABETES, newMember.getDiabetes());
        values.put(KEY_CHOLESTEROL, newMember.getCholesterol());
        values.put(KEY_HIGH_BLOOD_PRESSURE, newMember.getHighBloodPressure());
        values.put(KEY_LOW_BLOOD_PRESSURE, newMember.getLowBloodPressure());
        values.put(KEY_HEART_PROBLEM, newMember.getHeartProblem());
        values.put(KEY_CHEST_PAIN, newMember.getPainInChestWhenExercising());
        values.put(KEY_FAINTING, newMember.getFaintingSpells());
        values.put(KEY_BACK_PAIN, newMember.getBackOrSpinePains());
        values.put(KEY_MEDICATION, newMember.getAreYouOnAnySortOfMedications());
        values.put(KEY_OTHER_ILLNESS, newMember.getOtherSignificantIllness());
        values.put(KEY_SWOLLEN, newMember.getSwollen());
        values.put(KEY_ARTHRITIS, newMember.getArthritis());
        values.put(KEY_HERNIA, newMember.getHernia());

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
                member.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                member.setSurName(cursor.getString(cursor.getColumnIndex(KEY_SURNAME)));
                member.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                member.setMobile1(cursor.getInt(cursor.getColumnIndex(KEY_MOBILE_1)));
                member.setMobile2(cursor.getInt(cursor.getColumnIndex(KEY_MOBILE_2)));
                member.setNIC(cursor.getString(cursor.getColumnIndex(KEY_NIC)));
                member.setDOB(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
                member.setAge(cursor.getInt(cursor.getColumnIndex(KEY_AGE)));
                member.setGender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
                member.setMarriedStatus(cursor.getString(cursor.getColumnIndex(KEY_MARRIED_STATUS)));
                member.setProfileImage(cursor.getString(cursor.getColumnIndex(KEY_PROFILE_IMAGE_URL)));
                member.setHeight(cursor.getFloat(cursor.getColumnIndex(KEY_HEIGHT)));
                member.setWeight(cursor.getFloat(cursor.getColumnIndex(KEY_WEIGHT)));

                member.setDiabetes(cursor.getInt(cursor.getColumnIndex(KEY_DIABETES)) == 1);
                member.setCholesterol(cursor.getInt(cursor.getColumnIndex(KEY_CHOLESTEROL)) == 1);
                member.setHighBloodPressure(cursor.getInt(cursor.getColumnIndex(KEY_HIGH_BLOOD_PRESSURE)) == 1);
                member.setLowBloodPressure(cursor.getInt(cursor.getColumnIndex(KEY_LOW_BLOOD_PRESSURE)) == 1);
                member.setHeartProblem(cursor.getInt(cursor.getColumnIndex(KEY_HEART_PROBLEM)) == 1);
                member.setPainInChestWhenExercising(cursor.getInt(cursor.getColumnIndex(KEY_CHEST_PAIN)) == 1);
                member.setFaintingSpells(cursor.getInt(cursor.getColumnIndex(KEY_FAINTING)) == 1);
                member.setBackOrSpinePains(cursor.getInt(cursor.getColumnIndex(KEY_BACK_PAIN)) == 1);
                member.setAreYouOnAnySortOfMedications(cursor.getInt(cursor.getColumnIndex(KEY_MEDICATION)) == 1);
                member.setOtherSignificantIllness(cursor.getInt(cursor.getColumnIndex(KEY_OTHER_ILLNESS)) == 1);
                member.setSwollen(cursor.getInt(cursor.getColumnIndex(KEY_SWOLLEN)) == 1);
                member.setArthritis(cursor.getInt(cursor.getColumnIndex(KEY_ARTHRITIS)) == 1);
                member.setHernia(cursor.getInt(cursor.getColumnIndex(KEY_HERNIA)) == 1);

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
