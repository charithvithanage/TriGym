package com.example.charith.trigym;

public enum Config {

    Instance;

    public static String ServerUrl="http://10.0.2.2:3000/";
//    public static String ServerUrl="http://192.168.8.100:3000/";

    public static String save_member_url=ServerUrl+"members/savemember";
    public static String update_member_url=ServerUrl+"members/updatemember";
    public static String get_all_members_url=ServerUrl+"members/getallmembers";
    public static String update_members_url=ServerUrl+"members/updatemembers";
    public static String add_members_url=ServerUrl+"members/savemembers";


    public static String get_all_payments_url=ServerUrl+"payments/getallpayments";
    public static String update_payments_url=ServerUrl+"payments/updatepayments";
    public static String save_payment_url=ServerUrl+"payments/savepayment";
    public static String add_payments_url=ServerUrl+"payments/savepayments";

    public static String update_addresses_url=ServerUrl+"addresses/updateaddresses";
    public static String update_address_url=ServerUrl+"addresses/updateaddress";
    public static String get_all_addresses_url=ServerUrl+"addresses/getalladdresses";
    public static String add_addresses_url=ServerUrl+"addresses/saveaddresses";
    public static String save_address_url=ServerUrl+"addresses/saveaddress";

    public static String update_health_conditions_url=ServerUrl+"healthconditions/updatehealthconditions";
    public static String add_health_conditions_url=ServerUrl+"healthconditions/savehealthconditions";
    public static String save_health_condition_url=ServerUrl+"healthconditions/savehealthcondition";
    public static String update_health_condition_url=ServerUrl+"healthconditions/updatehealthcondition";
    public static String get_all_healthconditions_url=ServerUrl+"healthconditions/get_all_healthconditions";

}
