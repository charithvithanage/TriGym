package com.example.charith.trigym.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.charith.trigym.Entities.Address;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;
import com.example.charith.trigym.Utils;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static com.example.charith.trigym.Test.DbContact.TABLE_NAME;
import static com.example.charith.trigym.Utils.getMembershipExpiryDate;

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
    private static final String TABLE_PAYMENT = "table_payment";
    // Common column names
    // Common column names
    private static final String KEY_ID = "id";

    // NOTE_TAGS Table - column names

    private static final String KEY_MEMBERSHIP_NO = "member_membership_no";
    private static final String KEY_MEMBER_RECEIPT_NO = "member_receipt_no";
    private static final String KEY_MEMBER_ID = "member_id";
    private static final String KEY_FIRST_NAME = "member_first_name";
    private static final String KEY_TYPE = "type";
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
    private static final String KEY_MEMBERSHIP_TYPE = "membership_type";
    private static final String KEY_LAST_PAYMENT_DATE = "last_payment_date";
    private static final String KEY_MEMBERSHIP_EXPIRY_DATE = "membership_expiry_date";
    private static final String KEY_MEMBER_VALID_STATUS = "member_valid_status";

    private static final String KEY_ADDRESS_ID = "address_id";
    private static final String KEY_ADDRESS_LINE_1 = "address_line_1";
    private static final String KEY_ADDRESS_LINE_2 = "address_line_2";
    private static final String KEY_ADDRESS_LINE_3 = "address_line_3";
    private static final String KEY_ADDRESS_LINE_CITY = "address_line_city";

    private static final String KEY_PAYMENT_AMOUNT = "payment_amount";

    private static final String KEY_DIABETES = "diabetes";
    private static final String KEY_CHOLESTEROL = "cholesterol";
    private static final String KEY_HIGH_BLOOD_PRESSURE = "high_blood_pressure";
    private static final String KEY_LOW_BLOOD_PRESSURE = "low_blood_pressure";
    private static final String KEY_HEART_PROBLEM = "heart_problem";
    private static final String KEY_CHEST_PAIN = "chest_pain";
    private static final String KEY_HEART_ATTACK = "heart_attack";
    private static final String KEY_ASTHMA = "asthma";

    private static final String KEY_FAINTING = "fainting_spells";
    private static final String KEY_BACK_PAIN = "back_pain";
    private static final String KEY_MEDICATION = "medication";
    private static final String KEY_OTHER_ILLNESS = "other_illness";
    private static final String KEY_SWOLLEN = "swollen";
    private static final String KEY_ARTHRITIS = "arthritis";
    private static final String KEY_HERNIA = "hernia";


    public static final String CREATE_MEMBER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MEMBERS + "("
            + KEY_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MEMBERSHIP_NO + " INTEGER ,"
            + KEY_MEMBER_RECEIPT_NO + " INTEGER ,"
            + KEY_MEMBER_ADDRESS_ID + " INTEGER ,"
            + KEY_FIRST_NAME + " TEXT ,"
            + KEY_SURNAME + " TEXT ,"
            + KEY_LAST_NAME + " TEXT ,"
            + KEY_TYPE + " TEXT ,"
            + KEY_MARRIED_STATUS + " TEXT ,"
            + KEY_MOBILE_1 + " INTEGER,"
            + KEY_MOBILE_2 + " INTEGER,"
            + KEY_NIC + " TEXT,"
            + KEY_DOB + " BLOB,"
            + KEY_AGE + " INTEGER,"
            + KEY_GENDER + " TEXT,"
            + KEY_HEIGHT + " INTEGER,"
            + KEY_WEIGHT + " INTEGER,"
            + KEY_PROFILE_IMAGE_URL + " TEXT,"
            + KEY_COMMENT + " TEXT,"
            + KEY_MEMBERSHIP_EXPIRY_DATE + " TEXT,"
            + KEY_MEMBER_VALID_STATUS + " BOOLEAN,"
            + KEY_MEMBERSHIP_TYPE + " TEXT,"
            + KEY_LAST_PAYMENT_DATE + " TEXT,"
            + KEY_DIABETES + " BOOLEAN,"
            + KEY_CHOLESTEROL + " BOOLEAN,"
            + KEY_HIGH_BLOOD_PRESSURE + " INTEGER DEFAULT 0,"
            + KEY_LOW_BLOOD_PRESSURE + " INTEGER DEFAULT 0,"
            + KEY_HEART_PROBLEM + " INTEGER DEFAULT 0,"
            + KEY_CHEST_PAIN + " INTEGER DEFAULT 0,"
            + KEY_HEART_ATTACK + " INTEGER DEFAULT 0,"
            + KEY_ASTHMA + " INTEGER DEFAULT 0,"
            + KEY_FAINTING + " INTEGER DEFAULT 0,"
            + KEY_BACK_PAIN + " INTEGER DEFAULT 0,"
            + KEY_MEDICATION + " INTEGER DEFAULT 0,"
            + KEY_OTHER_ILLNESS + " INTEGER DEFAULT 0,"
            + KEY_SWOLLEN + " INTEGER DEFAULT 0,"
            + KEY_ARTHRITIS + " INTEGER DEFAULT 0,"
            + KEY_HERNIA + " INTEGER DEFAULT 0" + ")";

    public static final String CREATE_TABLE_ADDRESSES = "CREATE TABLE IF NOT EXISTS " + TABLE_ADDRESSES + "("
            + KEY_ADDRESS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_ADDRESS_LINE_1 + " TEXT ,"
            + KEY_ADDRESS_LINE_2 + " TEXT,"
            + KEY_ADDRESS_LINE_3 + " TEXT,"
            + KEY_ADDRESS_LINE_CITY + " TEXT" + ")";

    public static final String CREATE_TABLE_PAYMENT = "CREATE TABLE IF NOT EXISTS " + TABLE_PAYMENT + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_PAYMENT_AMOUNT + " INTEGER ,"
            + KEY_LAST_PAYMENT_DATE + " TEXT,"
            + KEY_MEMBERSHIP_EXPIRY_DATE + " TEXT,"
            + KEY_MEMBER_ID + " INTEGER,"
            + KEY_MEMBERSHIP_TYPE + " TEXT" + ")";

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
        db.execSQL(CREATE_TABLE_PAYMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADDRESSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT);

        onCreate(db);
    }

    // Adding new Lawyer
    public Long addMember(Member newMember) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MEMBER_ID, newMember.getId());
        values.put(KEY_MEMBERSHIP_NO, newMember.getMembershipNo());
        values.put(KEY_MEMBER_RECEIPT_NO, newMember.getMembershipRecieptNo());
        values.put(KEY_MEMBER_ADDRESS_ID, newMember.getAddressId());
        values.put(KEY_FIRST_NAME, newMember.getFirstName());
        values.put(KEY_SURNAME, newMember.getSurName());
        values.put(KEY_LAST_NAME, newMember.getLastName());
        values.put(KEY_TYPE, newMember.getType());
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
        values.put(KEY_MEMBERSHIP_TYPE, newMember.getMembershipType());
        values.put(KEY_LAST_PAYMENT_DATE, newMember.getLastPaymentDate());
        values.put(KEY_MEMBERSHIP_EXPIRY_DATE, getMembershipExpiryDate(newMember));
        values.put(KEY_MEMBER_VALID_STATUS, newMember.getValidStatus());
        values.put(KEY_DIABETES, newMember.getDiabetes());
        values.put(KEY_CHOLESTEROL, newMember.getCholesterol());
        values.put(KEY_HIGH_BLOOD_PRESSURE, newMember.getHighBloodPressure());
        values.put(KEY_LOW_BLOOD_PRESSURE, newMember.getLowBloodPressure());
        values.put(KEY_HEART_PROBLEM, newMember.getHeartProblem());
        values.put(KEY_CHEST_PAIN, newMember.getPainInChestWhenExercising());
        values.put(KEY_HEART_ATTACK, newMember.getHeartAttackCoronaryBypass());
        values.put(KEY_ASTHMA, newMember.getAnyBreathingDifficultiesAndAsthma());
        values.put(KEY_FAINTING, newMember.getFaintingSpells());
        values.put(KEY_BACK_PAIN, newMember.getBackOrSpinePains());
        values.put(KEY_MEDICATION, newMember.getAreYouOnAnySortOfMedications());
        values.put(KEY_OTHER_ILLNESS, newMember.getOtherSignificantIllness());
        values.put(KEY_SWOLLEN, newMember.getSwollen());
        values.put(KEY_ARTHRITIS, newMember.getArthritis());
        values.put(KEY_HERNIA, newMember.getHernia());
        values.put(KEY_COMMENT, newMember.getComments());

        Long memberId = db.insert(TABLE_MEMBERS, null, values);
        return memberId;
    }

    public void updateMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MEMBER_ID, member.getId());
        values.put(KEY_MEMBERSHIP_NO, member.getMembershipNo());
        values.put(KEY_MEMBER_RECEIPT_NO, member.getMembershipRecieptNo());
        values.put(KEY_MEMBER_ADDRESS_ID, member.getAddressId());
        values.put(KEY_FIRST_NAME, member.getFirstName());
        values.put(KEY_SURNAME, member.getSurName());
        values.put(KEY_LAST_NAME, member.getLastName());
        values.put(KEY_TYPE, member.getType());
        values.put(KEY_MOBILE_1, member.getMobile1());
        values.put(KEY_MOBILE_2, member.getMobile2());
        values.put(KEY_NIC, member.getNIC());
        values.put(KEY_DOB, member.getDOB());
        values.put(KEY_AGE, member.getAge());
        values.put(KEY_MARRIED_STATUS, member.getMarriedStatus());
        values.put(KEY_GENDER, member.getGender());
        values.put(KEY_HEIGHT, member.getHeight());
        values.put(KEY_WEIGHT, member.getWeight());
        values.put(KEY_PROFILE_IMAGE_URL, member.getProfileImage());
        values.put(KEY_MEMBERSHIP_TYPE, member.getMembershipType());
        values.put(KEY_LAST_PAYMENT_DATE, member.getLastPaymentDate());
        values.put(KEY_MEMBERSHIP_EXPIRY_DATE, getMembershipExpiryDate(member));
        values.put(KEY_MEMBER_VALID_STATUS, member.getValidStatus());
        values.put(KEY_DIABETES, member.getDiabetes());
        values.put(KEY_CHOLESTEROL, member.getCholesterol());
        values.put(KEY_HIGH_BLOOD_PRESSURE, member.getHighBloodPressure());
        values.put(KEY_LOW_BLOOD_PRESSURE, member.getLowBloodPressure());
        values.put(KEY_HEART_PROBLEM, member.getHeartProblem());
        values.put(KEY_CHEST_PAIN, member.getPainInChestWhenExercising());
        values.put(KEY_HEART_ATTACK, member.getHeartAttackCoronaryBypass());
        values.put(KEY_ASTHMA, member.getAnyBreathingDifficultiesAndAsthma());
        values.put(KEY_FAINTING, member.getFaintingSpells());
        values.put(KEY_BACK_PAIN, member.getBackOrSpinePains());
        values.put(KEY_MEDICATION, member.getAreYouOnAnySortOfMedications());
        values.put(KEY_OTHER_ILLNESS, member.getOtherSignificantIllness());
        values.put(KEY_SWOLLEN, member.getSwollen());
        values.put(KEY_ARTHRITIS, member.getArthritis());
        values.put(KEY_HERNIA, member.getHernia());
        values.put(KEY_COMMENT, member.getComments());

        db.update(TABLE_MEMBERS, values, KEY_MEMBER_ID + "=?", new String[]{String.valueOf(member.getId())});
    }

    public void deleteMember(String memberId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEMBERS, KEY_MEMBER_ID + "=?", new String[]{memberId});
    }

    public Member getMemberById(String memberId) {
        Member member = new Member();
        // Select All Query

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_MEMBERS, null, KEY_MEMBER_ID + "=?", new String[]{memberId}, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                member.setMembershipNo(cursor.getString(cursor.getColumnIndex(KEY_MEMBERSHIP_NO)));
                member.setMembershipRecieptNo(cursor.getString(cursor.getColumnIndex(KEY_MEMBER_RECEIPT_NO)));
                member.setId(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_ID)));
                member.setAddressId(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_ADDRESS_ID)));
                member.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                member.setSurName(cursor.getString(cursor.getColumnIndex(KEY_SURNAME)));
                member.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                member.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
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

                member.setLastPaymentDate(cursor.getString(cursor.getColumnIndex(KEY_LAST_PAYMENT_DATE)));
                member.setValidStatus(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_VALID_STATUS)) == 1);
                member.setMembershipExpiredDate(cursor.getString(cursor.getColumnIndex(KEY_MEMBERSHIP_EXPIRY_DATE)));

                member.setDiabetes(cursor.getInt(cursor.getColumnIndex(KEY_DIABETES)) == 1);
                member.setCholesterol(cursor.getInt(cursor.getColumnIndex(KEY_CHOLESTEROL)) == 1);
                member.setHighBloodPressure(cursor.getInt(cursor.getColumnIndex(KEY_HIGH_BLOOD_PRESSURE)) == 1);
                member.setLowBloodPressure(cursor.getInt(cursor.getColumnIndex(KEY_LOW_BLOOD_PRESSURE)) == 1);
                member.setHeartProblem(cursor.getInt(cursor.getColumnIndex(KEY_HEART_PROBLEM)) == 1);
                member.setPainInChestWhenExercising(cursor.getInt(cursor.getColumnIndex(KEY_CHEST_PAIN)) == 1);
                member.setHeartAttackCoronaryBypass(cursor.getInt(cursor.getColumnIndex(KEY_HEART_ATTACK)) == 1);
                member.setAnyBreathingDifficultiesAndAsthma(cursor.getInt(cursor.getColumnIndex(KEY_ASTHMA)) == 1);
                member.setFaintingSpells(cursor.getInt(cursor.getColumnIndex(KEY_FAINTING)) == 1);
                member.setBackOrSpinePains(cursor.getInt(cursor.getColumnIndex(KEY_BACK_PAIN)) == 1);
                member.setAreYouOnAnySortOfMedications(cursor.getInt(cursor.getColumnIndex(KEY_MEDICATION)) == 1);
                member.setOtherSignificantIllness(cursor.getInt(cursor.getColumnIndex(KEY_OTHER_ILLNESS)) == 1);
                member.setSwollen(cursor.getInt(cursor.getColumnIndex(KEY_SWOLLEN)) == 1);
                member.setArthritis(cursor.getInt(cursor.getColumnIndex(KEY_ARTHRITIS)) == 1);
                member.setHernia(cursor.getInt(cursor.getColumnIndex(KEY_HERNIA)) == 1);
                member.setMembershipType(cursor.getString(cursor.getColumnIndex(KEY_MEMBERSHIP_TYPE)));
                member.setComments(cursor.getString(cursor.getColumnIndex(KEY_COMMENT)));

            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return member;
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
                member.setMembershipNo(cursor.getString(cursor.getColumnIndex(KEY_MEMBERSHIP_NO)));
                member.setMembershipRecieptNo(cursor.getString(cursor.getColumnIndex(KEY_MEMBER_RECEIPT_NO)));
                member.setId(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_ID)));
                member.setAddressId(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_ADDRESS_ID)));
                member.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                member.setSurName(cursor.getString(cursor.getColumnIndex(KEY_SURNAME)));
                member.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                member.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
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

                member.setLastPaymentDate(cursor.getString(cursor.getColumnIndex(KEY_LAST_PAYMENT_DATE)));
                member.setValidStatus(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_VALID_STATUS)) == 1);
                member.setMembershipExpiredDate(cursor.getString(cursor.getColumnIndex(KEY_MEMBERSHIP_EXPIRY_DATE)));

                member.setDiabetes(cursor.getInt(cursor.getColumnIndex(KEY_DIABETES)) == 1);
                member.setCholesterol(cursor.getInt(cursor.getColumnIndex(KEY_CHOLESTEROL)) == 1);
                member.setHighBloodPressure(cursor.getInt(cursor.getColumnIndex(KEY_HIGH_BLOOD_PRESSURE)) == 1);
                member.setLowBloodPressure(cursor.getInt(cursor.getColumnIndex(KEY_LOW_BLOOD_PRESSURE)) == 1);
                member.setHeartProblem(cursor.getInt(cursor.getColumnIndex(KEY_HEART_PROBLEM)) == 1);
                member.setPainInChestWhenExercising(cursor.getInt(cursor.getColumnIndex(KEY_CHEST_PAIN)) == 1);
                member.setHeartAttackCoronaryBypass(cursor.getInt(cursor.getColumnIndex(KEY_HEART_ATTACK)) == 1);
                member.setAnyBreathingDifficultiesAndAsthma(cursor.getInt(cursor.getColumnIndex(KEY_ASTHMA)) == 1);
                member.setFaintingSpells(cursor.getInt(cursor.getColumnIndex(KEY_FAINTING)) == 1);
                member.setBackOrSpinePains(cursor.getInt(cursor.getColumnIndex(KEY_BACK_PAIN)) == 1);
                member.setAreYouOnAnySortOfMedications(cursor.getInt(cursor.getColumnIndex(KEY_MEDICATION)) == 1);
                member.setOtherSignificantIllness(cursor.getInt(cursor.getColumnIndex(KEY_OTHER_ILLNESS)) == 1);
                member.setSwollen(cursor.getInt(cursor.getColumnIndex(KEY_SWOLLEN)) == 1);
                member.setArthritis(cursor.getInt(cursor.getColumnIndex(KEY_ARTHRITIS)) == 1);
                member.setHernia(cursor.getInt(cursor.getColumnIndex(KEY_HERNIA)) == 1);
                member.setMembershipType(cursor.getString(cursor.getColumnIndex(KEY_MEMBERSHIP_TYPE)));
                member.setComments(cursor.getString(cursor.getColumnIndex(KEY_COMMENT)));

                // Adding contact to list
                members.add(member);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return members;
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

    public List<Payment> getPaymentsByMemberId(String memberId) {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_PAYMENT, null, KEY_MEMBER_ID + "=?", new String[]{memberId}, null, null, null);
        List<Payment> payments = new ArrayList<>();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Payment payment = new Payment();
                payment.setId(c.getInt(c.getColumnIndex(KEY_ID)));
                payment.setLastPaymentDate(c.getString(c.getColumnIndex(KEY_LAST_PAYMENT_DATE)));
                payment.setPaymentExpiryDate(c.getString(c.getColumnIndex(KEY_MEMBERSHIP_EXPIRY_DATE)));
                payment.setAmount(c.getFloat(c.getColumnIndex(KEY_PAYMENT_AMOUNT)));
                payment.setType(c.getString(c.getColumnIndex(KEY_MEMBERSHIP_TYPE)));

                payments.add(payment);
            } while (c.moveToNext());
        }

        c.close();

        return payments;
    }

    public Member getUserById(String userId) {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ADDRESSES, null, KEY_MEMBER_ID + "=?", new String[]{userId}, null, null, null);
        Member member = new Member();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                member.setId(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_ID)));
                member.setAddressId(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_ADDRESS_ID)));
                member.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
                member.setSurName(cursor.getString(cursor.getColumnIndex(KEY_SURNAME)));
                member.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
                member.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
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

                member.setLastPaymentDate(cursor.getString(cursor.getColumnIndex(KEY_LAST_PAYMENT_DATE)));
                member.setValidStatus(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_VALID_STATUS)) == 1);
                member.setMembershipExpiredDate(cursor.getString(cursor.getColumnIndex(KEY_MEMBERSHIP_EXPIRY_DATE)));

                member.setDiabetes(cursor.getInt(cursor.getColumnIndex(KEY_DIABETES)) == 1);
                member.setCholesterol(cursor.getInt(cursor.getColumnIndex(KEY_CHOLESTEROL)) == 1);
                member.setHighBloodPressure(cursor.getInt(cursor.getColumnIndex(KEY_HIGH_BLOOD_PRESSURE)) == 1);
                member.setLowBloodPressure(cursor.getInt(cursor.getColumnIndex(KEY_LOW_BLOOD_PRESSURE)) == 1);
                member.setHeartProblem(cursor.getInt(cursor.getColumnIndex(KEY_HEART_PROBLEM)) == 1);
                member.setPainInChestWhenExercising(cursor.getInt(cursor.getColumnIndex(KEY_CHEST_PAIN)) == 1);
                member.setFaintingSpells(cursor.getInt(cursor.getColumnIndex(KEY_FAINTING)) == 1);
                member.setHeartAttackCoronaryBypass(cursor.getInt(cursor.getColumnIndex(KEY_HEART_ATTACK)) == 1);
                member.setAnyBreathingDifficultiesAndAsthma(cursor.getInt(cursor.getColumnIndex(KEY_ASTHMA)) == 1);
                member.setBackOrSpinePains(cursor.getInt(cursor.getColumnIndex(KEY_BACK_PAIN)) == 1);
                member.setAreYouOnAnySortOfMedications(cursor.getInt(cursor.getColumnIndex(KEY_MEDICATION)) == 1);
                member.setOtherSignificantIllness(cursor.getInt(cursor.getColumnIndex(KEY_OTHER_ILLNESS)) == 1);
                member.setSwollen(cursor.getInt(cursor.getColumnIndex(KEY_SWOLLEN)) == 1);
                member.setArthritis(cursor.getInt(cursor.getColumnIndex(KEY_ARTHRITIS)) == 1);
                member.setHernia(cursor.getInt(cursor.getColumnIndex(KEY_HERNIA)) == 1);
            } while (cursor.moveToNext());
        }

        cursor.close();

        return member;
    }


    public void addPayment(Payment payment) {

        SQLiteDatabase databass = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PAYMENT_AMOUNT, payment.getAmount());
        values.put(KEY_MEMBER_ID, payment.getMember_id());
        values.put(KEY_MEMBERSHIP_TYPE, payment.getType());
        values.put(KEY_LAST_PAYMENT_DATE, payment.getLastPaymentDate());
        values.put(KEY_MEMBERSHIP_EXPIRY_DATE, payment.getPaymentExpiryDate());

        databass.insert(TABLE_PAYMENT, null, values);
    }


    public long getMembersCount(Context context) {
        SQLiteDatabase db = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_MEMBERS);
        db.close();
        return count;
    }


}
