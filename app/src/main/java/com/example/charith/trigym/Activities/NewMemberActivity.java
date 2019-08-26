package com.example.charith.trigym.Activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charith.trigym.Config;
import com.example.charith.trigym.Convertors.CircleTransform;
import com.example.charith.trigym.Convertors.DateTimeSerializer;
import com.example.charith.trigym.Convertors.JSONParser;
import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.DatePickerFragment;
import com.example.charith.trigym.Entities.Address;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.ProfileImageListner;
import com.example.charith.trigym.R;
import com.example.charith.trigym.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import org.apache.http.message.BasicNameValuePair;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewMemberActivity extends AppCompatActivity {

    private static final String TAG = "TryGym";
    TextView tvDOB, tvAge;

    DateTime today = new DateTime();

    Button btnNext, btnEdit;

    ImageView profileImage;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    Gson gson;

    Member member;

    TextInputEditText etMembershipNo, etMembershipReciptNo;

    TextInputEditText etEmail, etGuardianName, etGuardianTelephone, etGuardianRelationship, etFirstName, etLastName, etSurName, etLine1, etLine2, etLine3, etCity, etMobile1, etMobile2, etNIC, etHeight, etWeight;

    LinearLayout studentSection;

    File photoFile;

    public String photoFileName;

    Uri fileProvider;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 200;

    String memberId;

    String navigationType;

    List<String> memberTypeList = new ArrayList<>();
    String memberTypeListString;

    RadioButton radioBtnMarried, radioBtnSingle;
    RadioButton radioBtnMale, radioBtnFemale;

    TextView tvTitle;

    ImageButton backBtn;

    JSONParser jsonParser = new JSONParser();

    Address address;

    String memberCategory;

    Switch userActivateSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);


        navigationType = getIntent().getStringExtra("navigationType");
        memberId = getIntent().getStringExtra("memberId");
        memberTypeListString = getIntent().getStringExtra("memberTypeListString");
        memberCategory = getIntent().getStringExtra("memberCategory");


        init();
    }


    private void init() {
        member = new Member();


        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer());
        gson = builder.create();

        studentSection = findViewById(R.id.studentSection);

        if (!checkMemberIsStudent()) {
            studentSection.setVisibility(View.GONE);
        }

        backBtn = findViewById(R.id.backBtn);
        tvTitle = findViewById(R.id.tvTitle);
        radioBtnMale = findViewById(R.id.radioMale);
        radioBtnFemale = findViewById(R.id.radioFemale);
        radioBtnMarried = findViewById(R.id.radioMarried);
        radioBtnSingle = findViewById(R.id.radioSingle);

        etMembershipNo = findViewById(R.id.etMembershipNo);
        etMembershipReciptNo = findViewById(R.id.etMembershipReciptNo);

        etGuardianName = findViewById(R.id.etGuardianName);
        etGuardianTelephone = findViewById(R.id.etGuardianTel);
        etGuardianRelationship = findViewById(R.id.etGuardianRelationship);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etSurName = findViewById(R.id.etSurname);
        etLine1 = findViewById(R.id.etLine1);
        etLine2 = findViewById(R.id.etLine2);
        etLine3 = findViewById(R.id.etLine3);
        etCity = findViewById(R.id.etCity);
        etMobile1 = findViewById(R.id.etMobile1);
        etMobile2 = findViewById(R.id.etMobile2);
        etNIC = findViewById(R.id.etNIC);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etEmail = findViewById(R.id.etEmail);

        btnNext = findViewById(R.id.btnHistory);
        tvDOB = findViewById(R.id.tvDOB);
        tvAge = findViewById(R.id.tvAge);
        btnEdit = findViewById(R.id.btnEdit);
        profileImage = findViewById(R.id.profileImage);
        userActivateSwitch = findViewById(R.id.userActivateSwitch);


        if (navigationType != null) {
            if (navigationType.equals("edit")) {
                tvTitle.setText(getResources().getString(R.string.edit_user_title));
                DatabaseHandler db = new DatabaseHandler(this);
                member = db.getMemberById(memberId);
                setUserValues();
            }
        } else {

            displayFirstNameNICDialog(null,null);

            userActivateSwitch.setVisibility(View.GONE);
            try {
                JSONArray jsonArray = new JSONArray(memberTypeListString);

                for (int i = 0; i < jsonArray.length(); i++) {
                    String memberType = jsonArray.getString(i);
                    memberTypeList.add(memberType);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Collections.sort(memberTypeList);

            member.setType(TextUtils.join(",", memberTypeList));
            member.setCategory(memberCategory);
            DatabaseHandler db = new DatabaseHandler(this);
            etMembershipNo.setText(getMember_membership_no());
            etMembershipReciptNo.setText(getReciptNo());
            tvTitle.setText(getResources().getString(R.string.new_user_title));
            setTempValues();
        }

        tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDOB();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMember();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayFirstNameNICDialog(member.getMember_first_name(),member.getMember_nic());


            }
        });


        userActivateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    userActivateSwitch.setText("Activated");
                    member.setMember_active_status(true);
                } else {

                    userActivateSwitch.setText("Deactivated");
                    member.setMember_active_status(false);
                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void displayFirstNameNICDialog(String firstName,String NIC) {
        Utils.displayPasswordDialog(NewMemberActivity.this,firstName,NIC, new ProfileImageListner() {
            @Override
            public void onSuccess(DialogPlus dialog, String fistName, String nic) {
                dialog.dismiss();

                etFirstName.setText(fistName);
                etNIC.setText(nic);
                member.setMember_first_name(fistName);
                member.setMember_nic(nic);

                photoFileName = fistName + nic + ".jpg";

                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant

                        return;
                    } else {
                        onLaunchCamera();
                    }
                } else {
                    onLaunchCamera();
                }
            }

            @Override
            public void onCancel(DialogPlus dialogPlus) {
                dialogPlus.dismiss();
                finish();

            }
        });
    }

    private boolean checkMemberIsStudent() {
        for (String str : memberTypeList) {
            if (str.equals("Student")) {
                return true;
            }
        }
        return false;
    }

    private String getMember_membership_no() {

        StringBuilder getMembershipInitials = new StringBuilder();


        for (String str : memberTypeList) {
            String memberTypeInitial = str.substring(0, 1);
            getMembershipInitials.append(memberTypeInitial);
        }

        DatabaseHandler db = new DatabaseHandler(this);
        String membershipNo = getMembershipInitials.toString() + (1 + db.getMembersCount(NewMemberActivity.this));

        return membershipNo;
    }

    private String getReciptNo() {
        DatabaseHandler db = new DatabaseHandler(this);
        String receiptNo = "R" + (1 + db.getMembersCount(this));
        return receiptNo;
    }


    private void setUserValues() {
        if (member.getMember_active_status()) {
            userActivateSwitch.setText("Activated");
            userActivateSwitch.setChecked(true);
        } else {
            userActivateSwitch.setText("Deactivated");
            userActivateSwitch.setChecked(false);
        }
        if (member.getMember_married_status().equals("Single")) {
            radioBtnMarried.setChecked(false);
            radioBtnSingle.setChecked(true);
        } else {
            radioBtnMarried.setChecked(true);
            radioBtnSingle.setChecked(false);
        }

        if (member.getMember_gender().equals("Male")) {
            radioBtnMale.setChecked(true);
            radioBtnFemale.setChecked(false);

        } else {
            radioBtnFemale.setChecked(true);
            radioBtnMale.setChecked(false);

        }

        etMembershipNo.setText(member.getMember_membership_no());
        etMembershipReciptNo.setText(member.getMember_receipt_no());

        etFirstName.setText(member.getMember_first_name());
        etLastName.setText(member.getMember_last_name());
        etSurName.setText(member.getMember_surname());

        tvAge.setText(String.valueOf(member.getMember_age()));
        tvDOB.setText(member.getMember_dob());

        if (!member.getType().equals("Student")) {
            studentSection.setVisibility(View.GONE);
        } else {
            etGuardianName.setText(member.getGuardian_name());
            etGuardianTelephone.setText(member.getGuardian_tel());
            etGuardianRelationship.setText(member.getGuardian_relationship());
        }

        if (member.getMember_profile_image_url() != null) {
            Picasso.get().load(Uri.parse(member.getMember_profile_image_url())).transform(new CircleTransform()).into(profileImage);
        }


        etHeight.setText(String.valueOf(member.getMember_height()));
        etWeight.setText(String.valueOf(member.getMember_weight()));
        etNIC.setText(member.getMember_nic());
        etMobile1.setText(String.valueOf(member.getMember_mobile_1()));
        etMobile2.setText(String.valueOf(member.getMember_mobile_2()));

        DatabaseHandler databaseHandler = new DatabaseHandler(NewMemberActivity.this);

        Address address = databaseHandler.getAddressById(String.valueOf(member.getMember_address_id()));

        etLine1.setText(address.getLine1());
        etLine2.setText(address.getLine2());
        etLine3.setText(address.getLine3());

    }

    private void setTempValues() {

        etFirstName.setText("charith");
        etLastName.setText("vinodya");
        etSurName.setText("vithanage");

        etGuardianName.setText("nihal");
        etGuardianTelephone.setText("0323433445");
        etGuardianRelationship.setText("father");

        etHeight.setText("172");
        etWeight.setText("82");
        etNIC.setText("912349873v");
        etMobile1.setText("0712848384");
        etMobile2.setText("0712848384");

        etLine1.setText("Morawatta");
        etLine2.setText("Ruwanwella");
        etLine3.setText("Kegalle");

    }

    public void onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference to access to future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher


        fileProvider = FileProvider.getUriForFile(NewMemberActivity.this, "com.codepath.fileprovider", photoFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
//
//        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
//        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    private void saveMember() {

        final RadioGroup rbgGender = findViewById(R.id.radioGroupGender);
        final RadioGroup rbgMarriedStatus = findViewById(R.id.radioGroudMarriedStatus);

        int selectedGenderRadioButton = rbgGender.getCheckedRadioButtonId();
        int selectedMarriedStatusRadioButton = rbgMarriedStatus.getCheckedRadioButtonId();
        RadioButton gender = findViewById(selectedGenderRadioButton);
        RadioButton marriedStatus = findViewById(selectedMarriedStatusRadioButton);

        if (gender.getText() != null) {
            member.setMember_gender(gender.getText().toString());
        }

        member.setMember_married_status(marriedStatus.getText().toString());
        member.setEmail(etEmail.getText().toString());
        member.setMember_surname(etSurName.getText().toString());


        if (!TextUtils.isEmpty(etMobile2.getText().toString())) {
            member.setMember_mobile_2(Integer.valueOf(etMobile2.getText().toString()));
        }


        address = new Address();

        if (fileProvider != null) {
            member.setMember_profile_image_url(fileProvider.toString());
        }

        member.setMember_membership_no(etMembershipNo.getText().toString());
        member.setMember_receipt_no(etMembershipReciptNo.getText().toString());
        member.setMember_first_name(etFirstName.getText().toString());
        member.setMember_last_name(etLastName.getText().toString());


        if (member.getType().equals("Student")) {

            member.setGuardian_name(etGuardianName.getText().toString());
            member.setGuardian_tel(Integer.valueOf(etGuardianTelephone.getText().toString()));
            member.setGuardian_relationship(etGuardianRelationship.getText().toString());

        }

        address.setLine1(etLine1.getText().toString());

        if (!TextUtils.isEmpty(etHeight.getText().toString())) {
            member.setMember_height(Float.valueOf(etHeight.getText().toString()));

        }

        if (!TextUtils.isEmpty(etWeight.getText().toString())) {
            member.setMember_weight(Float.valueOf(etWeight.getText().toString()));

        }
        member.setMember_nic(etNIC.getText().toString());

        if (!TextUtils.isEmpty(etMobile1.getText().toString())) {
            member.setMember_mobile_1(Integer.valueOf(etMobile1.getText().toString()));
        }


        if (navigationType != null) {
            if (navigationType.equals("edit")) {
                Intent intent = new Intent(NewMemberActivity.this, MemberBioActivity.class);
                intent.putExtra("memberString", gson.toJson(member));
                intent.putExtra("navigationType", "edit");
                startActivity(intent);
                finish();
            } else {

                navigateToMemberBioActivity();

            }
        } else {
            navigateToMemberBioActivity();
        }


//        databaseHandler.addMember(member, addressId);
    }

    private void navigateToMemberBioActivity() {

        if (member.getMember_membership_no() != null && member.getMember_receipt_no() != null && member.getMember_profile_image_url() != null && member.getMember_mobile_1() != null &&
                member.getMember_first_name() != null && member.getMember_surname() != null && member.getMember_nic() != null && member.getMember_dob() != null && member.getMember_height() != null && member.getMember_weight() != null && address.getLine1() != null) {

            DatabaseHandler databaseHandler = new DatabaseHandler(NewMemberActivity.this);
            Integer addressId;

            address.setLine2(etLine2.getText().toString());
            address.setLine3(etLine3.getText().toString());
            address.setCity(etCity.getText().toString());

            addressId = databaseHandler.addAddress(address).intValue();

            member.setMember_address_id(addressId);
            address.setId(addressId);

            saveAddressToServer(address);


        } else {
            if (member.getMember_membership_no() == null) {
                etMembershipNo.setError(getString(R.string.empty_field_alert));
            }

            if (member.getMember_receipt_no() == null) {
                etMembershipReciptNo.setError(getString(R.string.empty_field_alert));
            }

            if (member.getMember_profile_image_url() == null) {
                Utils.showWarningMessage(NewMemberActivity.this, getString(R.string.capture_image_alert));
            } else {
                if (member.getMember_dob() == null) {
                    Utils.showWarningMessage(NewMemberActivity.this, getString(R.string.not_dob_selected_alert));

                }
            }

            if (member.getMember_mobile_1() == null) {
                etMobile1.setError(getString(R.string.empty_field_alert));
            }

            if (member.getMember_first_name() == null) {
                etFirstName.setError(getString(R.string.empty_field_alert));
            }
            if (member.getMember_surname() == null) {
                etSurName.setError(getString(R.string.empty_field_alert));
            }
            if (member.getMember_nic() == null) {
                etNIC.setError(getString(R.string.empty_field_alert));
            }

            if (member.getMember_height() == null) {
                etHeight.setError(getString(R.string.empty_field_alert));
            }
            if (member.getMember_weight() == null) {
                etWeight.setError(getString(R.string.empty_field_alert));
            }

            if (address.getLine1() == null) {
                etLine1.setError(getString(R.string.empty_field_alert));
            }

        }


    }

    private void saveAddressToServer(Address address) {
        Intent intent = new Intent(NewMemberActivity.this, MemberBioActivity.class);
        intent.putExtra("memberString", gson.toJson(member));
        intent.putExtra("navigationType", "new");

        startActivity(intent);
//        new SaveAddressToServerAsync(address).execute();

    }

    private Integer calculateAge(DateTime dob) {

        Integer age = today.getYear() - dob.getYear();

        return age;
    }

    private void selectDOB() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */

        Bundle args = new Bundle();
        args.putInt("year", today.getYear());
        args.putInt("month", today.getMonthOfYear() - 1);
        args.putInt("day", today.getDayOfMonth());

        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(startDate);
        date.show(getFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener startDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            final DateTime tempDate = new DateTime(year, monthOfYear + 1, dayOfMonth, today.getHourOfDay(), today.getMinuteOfHour());

            tvDOB.setText(tempDate.toString(getString(R.string.date_pattern)));
            member.setMember_age(calculateAge(tempDate));
            tvAge.setText(calculateAge(tempDate).toString() + " Yrs");
            member.setMember_dob(tempDate.toString(getString(R.string.date_time_pattern)));


        }
    };


//    public Intent getPickImageChooserIntent() {

//        // Determine Uri of camera image to save.
//        Uri outputFileUri = getCaptureImageOutputUri();
//
//        // collect all camera intents
//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        if (captureIntent.resolveActivity(getPackageManager()) != null) {
//            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT,outputFileUri);
//        }

//        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        return cameraIntent;
//    }

    /**
     * Get URI to image received from capture by camera.
     */
//    private Uri getCaptureImageOutputUri(String name) {
//        Uri outputFileUri = null;
//
////        File getImage = new File(getFilesDir(), "GYMImage");
//        File getImage = getPrivateAlbumStorageDir(getApplicationContext(),"GYMImage");
//        if (getImage != null) {
//            outputFileUri = Uri.fromFile(new File(getImage.getPath(), name + ".jpeg"));
//        }
//        return outputFileUri;
//    }
//
//    /**
//     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
//     * Will return the correct URI for camera and gallery image.
//     *
//     * @param data the returned data of the activity result
//     */
//    public Uri getPickImageResultUri(Intent data) {
//        boolean isCamera = true;
//        if (data != null && data.getData() != null) {
//            String action = data.getAction();
//            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
//        }
//        return isCamera ? getCaptureImageOutputUri(etFirstName.getText().toString()+etLastName.getText().toString()) : data.getData();
//    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }


    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
//    public boolean isUriRequiresPermissions(Uri uri) {
//        ContentResolver resolver = getContentResolver();
//        InputStream stream = null;
//        try {
//            stream = resolver.openInputStream(uri);
//            stream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // by this point we have the camera photo on disk
            Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
            // RESIZE BITMAP, see section below
            // Load the taken image into a preview
            Picasso.get().load(fileProvider).transform(new CircleTransform()).into(profileImage);

//            profileImage.setImageBitmap(takenImage);
        } else { // Result was a failure
            Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! do the
                    onLaunchCamera();

                } else {

                    // permission denied. Disable the
                    // functionality that depends on this permission.
                    // permission was granted, yay! do the

                    DialogPlus dialog = DialogPlus.newDialog(NewMemberActivity.this)
                            .setContentBackgroundResource(R.drawable.warning_message_bg)
                            .setContentHolder(new ViewHolder(R.layout.error_dialog_bg))
                            .setExpanded(true, ViewGroup.LayoutParams.WRAP_CONTENT).setGravity(Gravity.TOP)  // This will enable the expand feature, (similar to android L share dialog)
                            .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel(DialogPlus dialog) {
                                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                                }
                            }).create();

                    dialog.show();

                    View view = dialog.getHolderView();

                    TextView tvMessage = view.findViewById(R.id.message);
                    tvMessage.setText(getResources().getString(R.string.no_permission_to_camera));
                }
                return;
            }

        }
    }


    private class SaveAddressToServerAsync extends AsyncTask<Void, Void, JSONObject> {

        Address address;

        public SaveAddressToServerAsync(Address address) {
            this.address = address;
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String URL = Config.ServerUrl + Config.save_address_url;

            ArrayList params = new ArrayList();
            params.add(new BasicNameValuePair("id", String.valueOf(address.getId())));
            params.add(new BasicNameValuePair("line1", address.getLine1()));
            params.add(new BasicNameValuePair("line2", address.getLine2()));
            params.add(new BasicNameValuePair("line3", address.getLine3()));
            params.add(new BasicNameValuePair("city", address.getCity()));

            JSONObject json = jsonParser.makeHttpRequest(URL, "POST", params);


            return json;
        }


        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            try {
                if (jsonObject != null) {

                    Intent intent = new Intent(NewMemberActivity.this, MemberBioActivity.class);
                    intent.putExtra("memberString", gson.toJson(member));
                    intent.putExtra("navigationType", "new");

                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Unable to retrieve any data from server", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
