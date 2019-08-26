package com.example.charith.trigym;

public enum Config {

    Instance;

    public static String ServerUrl="http://192.168.8.100:3000/";
    public static String save_address_url="address_index.php";

    public static String save_member_url=ServerUrl+"members/savemember";
    public static String update_member_url=ServerUrl+"members/updatemember";
    public static String get_all_members_url=ServerUrl+"members/getallmembers";
    public static String get_all_payments_url=ServerUrl+"payments/getallpayments";
    public static String update_payments_url=ServerUrl+"payments/updatepayments";
    public static String update_members_url=ServerUrl+"members/updatemembers";
}
