package com.example.charith.trigym.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Entities.Payment;

import java.util.ArrayList;
import java.util.List;

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
    private static final String TABLE_PAYMENT = "table_payment";

    private static final String KEY_PAYMENT_AMOUNT = "payment_amount";
    private static final String KEY_PAYMENT_ID = "payment_id";

    // NOTE_TAGS Table - column names

    private static final String KEY_MEMBERSHIP_NO = "member_membership_no";
    private static final String KEY_MEMBER_RECEIPT_NO = "member_receipt_no";
    private static final String KEY_MEMBER_ID = "member_id";
    private static final String KEY_FIRST_NAME = "member_first_name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_CAT = "category";
    private static final String KEY_LAST_NAME = "member_last_name";
    private static final String KEY_SURNAME = "member_surname";
    private static final String KEY_ADDRESS_LINE_1 = "address_line_1";
    private static final String KEY_ADDRESS_LINE_2 = "address_line_2";
    private static final String KEY_ADDRESS_LINE_3 = "address_line_3";
    private static final String KEY_ADDRESS_CITY = "address_line_city";
    private static final String KEY_GUARDIAN_NAME = "guardian_name";
    private static final String KEY_GUARDIAN_TEL = "guardian_tel";
    private static final String KEY_GUARDIAN_RELATIONSHIP = "guardian_relationship";


    private static final String KEY_MOBILE_1 = "member_mobile_1";
    private static final String KEY_MOBILE_2 = "member_mobile_2";
    private static final String KEY_NIC = "member_nic";
    private static final String KEY_DOB = "member_dob";
    private static final String KEY_AGE = "member_age";
    private static final String KEY_GENDER = "member_gender";
    private static final String KEY_MARRIED_STATUS = "member_married_status";
    private static final String KEY_HEIGHT = "member_height";
    private static final String KEY_WEIGHT = "member_weight";
    private static final String KEY_PROFILE_IMAGE_URL = "member_profile_image_url";
    private static final String KEY_COMMENT = "member_health_condition";
    private static final String KEY_MEMBERSHIP_TYPE = "membership_type";
    private static final String KEY_LAST_PAYMENT_DATE = "last_payment_date";
    private static final String KEY_MEMBERSHIP_EXPIRY_DATE = "membership_expiry_date";
    private static final String KEY_MEMBER_VALID_STATUS = "member_valid_status";
    private static final String KEY_MEMBER_IS_ACTIVE = "member_active_status";

    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_MODIFIED_AT = "modified_at";

    public static final String CREATE_MEMBER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_MEMBERS + "("
            + KEY_MEMBER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MEMBERSHIP_NO + " INTEGER ,"
            + KEY_MEMBER_RECEIPT_NO + " INTEGER ,"
            + KEY_FIRST_NAME + " TEXT ,"
            + KEY_SURNAME + " TEXT ,"
            + KEY_LAST_NAME + " TEXT ,"
            + KEY_GUARDIAN_NAME + " TEXT ,"
            + KEY_GUARDIAN_TEL + " TEXT ,"
            + KEY_GUARDIAN_RELATIONSHIP + " TEXT ,"
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
            + KEY_TYPE + " TEXT ,"
            + KEY_CAT + " TEXT ,"
            + KEY_ADDRESS_LINE_1 + " TEXT ,"
            + KEY_ADDRESS_LINE_2 + " TEXT ,"
            + KEY_ADDRESS_LINE_3 + " TEXT ,"
            + KEY_ADDRESS_CITY + " TEXT ,"
            + KEY_MEMBERSHIP_EXPIRY_DATE + " TEXT,"
            + KEY_MEMBER_VALID_STATUS + " BOOLEAN,"
            + KEY_MEMBERSHIP_TYPE + " TEXT,"
            + KEY_LAST_PAYMENT_DATE + " TEXT,"
            + KEY_MEMBER_IS_ACTIVE + " BOOLEAN,"
            + KEY_CREATED_AT + " TEXT,"
            + KEY_MODIFIED_AT + " TEXT"
            + ")";

    public static final String CREATE_TABLE_PAYMENT = "CREATE TABLE IF NOT EXISTS " + TABLE_PAYMENT + "("
            + KEY_PAYMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_MEMBER_ID + " INTEGER ,"
            + KEY_PAYMENT_AMOUNT + " INTEGER ,"
            + KEY_LAST_PAYMENT_DATE + " TEXT,"
            + KEY_MEMBERSHIP_EXPIRY_DATE + " TEXT,"
            + KEY_CREATED_AT + " TEXT,"
            + KEY_MODIFIED_AT + " TEXT,"
            + KEY_MEMBERSHIP_TYPE + " TEXT, FOREIGN KEY(" + KEY_MEMBER_ID + ") REFERENCES " + TABLE_MEMBERS + "(" + KEY_MEMBER_ID + ") ON DELETE CASCADE)";

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
        db.execSQL(CREATE_TABLE_PAYMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEMBERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENT);

        onCreate(db);
    }

    // Adding new Lawyer
    public Long addMember(Member newMember) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        setContentValuesForMember(newMember, values);

        Long memberId = db.insert(TABLE_MEMBERS, null, values);
        return memberId;
    }


    public void updateMember(Member member) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();

        setContentValuesForMember(member, values);


        db.update(TABLE_MEMBERS, values, KEY_MEMBER_ID + "=?", new String[]{String.valueOf(member.getMember_id())});
    }

    public Member getMemberById(String memberId) {
        Member member = new Member();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_MEMBERS, null, KEY_MEMBER_ID + "=?", new String[]{memberId}, null, null, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                member = getMemberWithValues(cursor);

            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return member;
    }


    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MEMBERS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Member member = getMemberWithValues(cursor);

                // Adding contact to list
                members.add(member);
            } while (cursor.moveToNext());
        }

        cursor.close();

        // return contact list
        return members;
    }

    public void deleteMember(String memberId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEMBERS, KEY_MEMBER_ID + "=?", new String[]{memberId});
    }


    public long getMembersCount(Context context) {
        SQLiteDatabase db = getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, TABLE_MEMBERS);
        db.close();
        return count;
    }

    public List<Payment> getAllPayments() {

        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + TABLE_PAYMENT;
        Cursor c = db.rawQuery(selectQuery, null);
        List<Payment> payments = new ArrayList<>();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Payment payment = setValuesToPayment(c);
                payments.add(payment);
            } while (c.moveToNext());
        }

        c.close();

        return payments;
    }


    public Payment getPaymentById(String id, String memberId) {


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_PAYMENT, null, KEY_PAYMENT_ID + "=?" + " AND " + KEY_MEMBER_ID + "=?", new String[]{id, memberId}, null, null, null);
        Payment payment = new Payment();
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                payment = setValuesToPayment(c);

            } while (c.moveToNext());
        }

        c.close();

        return payment;
    }

    public List<Payment> getPaymentsByMemberId(String memberId) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_PAYMENT, null, KEY_MEMBER_ID + "=?", new String[]{memberId}, null, null, null);
        List<Payment> payments = new ArrayList<>();

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Payment payment = setValuesToPayment(c);
                payments.add(payment);
            } while (c.moveToNext());
        }

        c.close();

        return payments;
    }

    public void updatePayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();


        ContentValues values = new ContentValues();

        values = setContentValuesToPayment(payment, values);


        db.update(TABLE_PAYMENT, values, KEY_PAYMENT_ID + "=?" + " AND " + KEY_MEMBER_ID + "=?", new String[]{String.valueOf(payment.getPayment_id()), String.valueOf(payment.getMember_id())});
    }

    public Long addPayment(Payment payment) {

        SQLiteDatabase databass = getWritableDatabase();
        ContentValues values = new ContentValues();

        setContentValuesToPayment(payment, values);

        Long id = databass.insert(TABLE_PAYMENT, null, values);
        return id;
    }

    private Payment setValuesToPayment(Cursor c) {
        Payment payment = new Payment();
        payment.setPayment_id(c.getLong(c.getColumnIndex(KEY_PAYMENT_ID)));
        payment.setMember_id(c.getLong(c.getColumnIndex(KEY_MEMBER_ID)));
        payment.setLast_payment_date(c.getString(c.getColumnIndex(KEY_LAST_PAYMENT_DATE)));
        payment.setMembership_expiry_date(c.getString(c.getColumnIndex(KEY_MEMBERSHIP_EXPIRY_DATE)));
        payment.setPayment_amount(c.getFloat(c.getColumnIndex(KEY_PAYMENT_AMOUNT)));
        payment.setMembership_type(c.getString(c.getColumnIndex(KEY_MEMBERSHIP_TYPE)));
        payment.setCreated_at(c.getString(c.getColumnIndex(KEY_CREATED_AT)));
        payment.setModified_at(c.getString(c.getColumnIndex(KEY_MODIFIED_AT)));

        return payment;
    }

    //Return member by assigning values to member object from cursor
    private Member getMemberWithValues(Cursor cursor) {
        Member member = new Member();
        member.setMember_membership_no(cursor.getString(cursor.getColumnIndex(KEY_MEMBERSHIP_NO)));
        member.setMember_receipt_no(cursor.getString(cursor.getColumnIndex(KEY_MEMBER_RECEIPT_NO)));
        member.setMember_id(cursor.getLong(cursor.getColumnIndex(KEY_MEMBER_ID)));
        member.setMember_first_name(cursor.getString(cursor.getColumnIndex(KEY_FIRST_NAME)));
        member.setMember_surname(cursor.getString(cursor.getColumnIndex(KEY_SURNAME)));
        member.setMember_last_name(cursor.getString(cursor.getColumnIndex(KEY_LAST_NAME)));
        member.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
        if (cursor.getString(cursor.getColumnIndex(KEY_CAT)) != null) {
            member.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CAT)));
        }

        if (cursor.getString(cursor.getColumnIndex(KEY_ADDRESS_LINE_1)) != null) {
            member.setAddress_line_1(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS_LINE_1)));
        }

        if (cursor.getString(cursor.getColumnIndex(KEY_ADDRESS_LINE_2)) != null) {
            member.setAddress_line_2(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS_LINE_2)));
        }

        if (cursor.getString(cursor.getColumnIndex(KEY_ADDRESS_LINE_3)) != null) {
            member.setAddress_line_3(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS_LINE_3)));
        }

        if (cursor.getString(cursor.getColumnIndex(KEY_ADDRESS_CITY)) != null) {
            member.setCity(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS_CITY)));
        }

        member.setMember_mobile_1(cursor.getInt(cursor.getColumnIndex(KEY_MOBILE_1)));
        member.setMember_mobile_2(cursor.getInt(cursor.getColumnIndex(KEY_MOBILE_2)));
        member.setMember_nic(cursor.getString(cursor.getColumnIndex(KEY_NIC)));
        member.setMember_dob(cursor.getString(cursor.getColumnIndex(KEY_DOB)));
        member.setMember_age(cursor.getInt(cursor.getColumnIndex(KEY_AGE)));
        member.setMember_gender(cursor.getString(cursor.getColumnIndex(KEY_GENDER)));
        member.setMember_married_status(cursor.getString(cursor.getColumnIndex(KEY_MARRIED_STATUS)));
        member.setMember_profile_image_url(cursor.getString(cursor.getColumnIndex(KEY_PROFILE_IMAGE_URL)));
        member.setMember_height(cursor.getFloat(cursor.getColumnIndex(KEY_HEIGHT)));
        member.setMember_weight(cursor.getFloat(cursor.getColumnIndex(KEY_WEIGHT)));

        member.setLast_payment_date(cursor.getString(cursor.getColumnIndex(KEY_LAST_PAYMENT_DATE)));
        member.setMember_valid_status(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_VALID_STATUS)) == 1);
        member.setMember_active_status(cursor.getInt(cursor.getColumnIndex(KEY_MEMBER_IS_ACTIVE)) == 1);
        member.setMembership_expiry_date(cursor.getString(cursor.getColumnIndex(KEY_MEMBERSHIP_EXPIRY_DATE)));
        member.setMembership_type(cursor.getString(cursor.getColumnIndex(KEY_MEMBERSHIP_TYPE)));
        member.setMember_health_condition(cursor.getString(cursor.getColumnIndex(KEY_COMMENT)));
        member.setGuardian_name(cursor.getString(cursor.getColumnIndex(KEY_GUARDIAN_NAME)));
        member.setGuardian_tel(cursor.getInt(cursor.getColumnIndex(KEY_GUARDIAN_TEL)));
        member.setGuardian_relationship(cursor.getString(cursor.getColumnIndex(KEY_GUARDIAN_RELATIONSHIP)));
        member.setCreated_at(cursor.getString(cursor.getColumnIndex(KEY_CREATED_AT)));
        member.setModified_at(cursor.getString(cursor.getColumnIndex(KEY_MODIFIED_AT)));
        return member;
    }

    //Return content values from member
    private ContentValues setContentValuesForMember(Member newMember, ContentValues values) {

        values.put(KEY_MEMBER_ID, newMember.getMember_id());
        values.put(KEY_MEMBERSHIP_NO, newMember.getMember_membership_no());
        values.put(KEY_MEMBER_RECEIPT_NO, newMember.getMember_receipt_no());
        values.put(KEY_FIRST_NAME, newMember.getMember_first_name());
        values.put(KEY_SURNAME, newMember.getMember_surname());
        values.put(KEY_LAST_NAME, newMember.getMember_last_name());
        values.put(KEY_TYPE, newMember.getType());
        values.put(KEY_CAT, newMember.getCategory());
        values.put(KEY_ADDRESS_LINE_1, newMember.getAddress_line_1());
        values.put(KEY_ADDRESS_LINE_2, newMember.getAddress_line_2());
        values.put(KEY_ADDRESS_LINE_3, newMember.getAddress_line_3());
        values.put(KEY_ADDRESS_CITY, newMember.getCity());
        values.put(KEY_MOBILE_1, newMember.getMember_mobile_1());
        values.put(KEY_MOBILE_2, newMember.getMember_mobile_2());
        values.put(KEY_NIC, newMember.getMember_nic());
        values.put(KEY_DOB, newMember.getMember_dob());
        values.put(KEY_AGE, newMember.getMember_age());
        values.put(KEY_MARRIED_STATUS, newMember.getMember_married_status());
        values.put(KEY_GENDER, newMember.getMember_gender());
        values.put(KEY_HEIGHT, newMember.getMember_height());
        values.put(KEY_WEIGHT, newMember.getMember_weight());
        values.put(KEY_PROFILE_IMAGE_URL, newMember.getMember_profile_image_url());
        values.put(KEY_MEMBERSHIP_TYPE, newMember.getMembership_type());
        values.put(KEY_LAST_PAYMENT_DATE, newMember.getLast_payment_date());
        if (newMember.getLast_payment_date() != null) {
            values.put(KEY_MEMBERSHIP_EXPIRY_DATE, getMembershipExpiryDate(newMember));
        }
        values.put(KEY_MEMBER_VALID_STATUS, newMember.getMember_valid_status());
        values.put(KEY_MEMBER_IS_ACTIVE, newMember.getMember_active_status());
        values.put(KEY_COMMENT, newMember.getMember_health_condition());
        if (newMember.getGuardian_name() != null) {
            values.put(KEY_GUARDIAN_NAME, newMember.getGuardian_name());
        }
        if (newMember.getGuardian_tel() != null) {
            values.put(KEY_GUARDIAN_TEL, newMember.getGuardian_tel());
        }
        if (newMember.getGuardian_relationship() != null) {
            values.put(KEY_GUARDIAN_RELATIONSHIP, newMember.getGuardian_relationship());
        }

        values.put(KEY_CREATED_AT, newMember.getCreated_at());
        values.put(KEY_MODIFIED_AT, newMember.getModified_at());
        return values;
    }

    private ContentValues setContentValuesToPayment(Payment payment, ContentValues values) {
        values.put(KEY_PAYMENT_AMOUNT, payment.getPayment_amount());
        values.put(KEY_MEMBER_ID, payment.getMember_id());
        values.put(KEY_MEMBERSHIP_TYPE, payment.getMembership_type());
        values.put(KEY_LAST_PAYMENT_DATE, payment.getLast_payment_date());
        values.put(KEY_MEMBERSHIP_EXPIRY_DATE, payment.getMembership_expiry_date());
        values.put(KEY_CREATED_AT, payment.getCreated_at());
        values.put(KEY_MODIFIED_AT, payment.getModified_at());

        return values;
    }


}
