package com.example.charith.trigym;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charith.trigym.DB.DatabaseHandler;
import com.example.charith.trigym.Entities.Address;
import com.example.charith.trigym.Entities.Member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.joda.time.DateTime;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class NewMemberActivity extends AppCompatActivity {

    private static final String TAG = "TryGym";
    TextView tvDOB, tvAge;

    DateTime today = new DateTime();

    Button btnNext, btnEdit;

    ImageView profileImage;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    Gson gson;

    Member member;

    TextInputEditText etMembershipNo,etMembershipReciptNo;

    TextInputEditText etGuardianName, etGuardianTelephone, etGuardianRelationship, etFirstName, etLastName, etSurName, etLine1, etLine2, etLine3, etCity, etMobile1, etMobile2, etNIC, etHeight, etWeight;

    LinearLayout studentSection;

    File photoFile;

    public String photoFileName;

    Uri fileProvider;

    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);


        init();
    }

    private void init() {
        member = new Member();

        String memberType = getIntent().getStringExtra("memberType");
        member.setType(memberType);
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer());
        gson = builder.create();

        studentSection = findViewById(R.id.studentSection);

        if (!memberType.equals("Student")) {
            studentSection.setVisibility(View.GONE);
        }

        etMembershipNo=findViewById(R.id.etMembershipNo);
        etMembershipReciptNo=findViewById(R.id.etMembershipReciptNo);

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

        btnNext = findViewById(R.id.btnNext);
        tvDOB = findViewById(R.id.tvDOB);
        tvAge = findViewById(R.id.tvAge);
        btnEdit = findViewById(R.id.btnEdit);
        profileImage = findViewById(R.id.profileImage);


        setTempValues();

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

                if(!TextUtils.isEmpty(etFirstName.getText().toString())&&!TextUtils.isEmpty(etNIC.getText().toString())){
                    photoFileName = etFirstName.getText().toString() + etNIC.getText().toString() + ".jpg";

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
                }else {
                    Utils.showWarningMessage(NewMemberActivity.this,getString(R.string.profileImageSelectWarningMessage));
                }


            }
        });
    }

    private void setTempValues() {

        etMembershipNo.setText("m1");
        etMembershipReciptNo.setText("r1");

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

        member.setGender(gender.getText().toString());
        member.setMarriedStatus(marriedStatus.getText().toString());

        DatabaseHandler databaseHandler = new DatabaseHandler(NewMemberActivity.this);
        Integer addressId;

        Address address = new Address();
        address.setLine1(etLine1.getText().toString());
        address.setLine2(etLine2.getText().toString());
        address.setLine3(etLine3.getText().toString());
        address.setCity(etCity.getText().toString());

        addressId = databaseHandler.addAddress(address).intValue();

        member.setFirstName(etFirstName.getText().toString());
        member.setLastName(etLastName.getText().toString());
        member.setSurName(etSurName.getText().toString());

        member.setGuardianName(etGuardianName.getText().toString());
        member.setGuardianTel(Integer.valueOf(etGuardianTelephone.getText().toString()));
        member.setGuardianRelationship(etGuardianRelationship.getText().toString());

        member.setAddress(address);
        member.setAddressId(address.getId());

        member.setHeight(Float.valueOf(etHeight.getText().toString()));
        member.setWeight(Float.valueOf(etWeight.getText().toString()));
        member.setNIC(etNIC.getText().toString());

        if (!TextUtils.isEmpty(etMobile1.getText().toString())) {
            member.setMobile1(Integer.valueOf(etMobile1.getText().toString()));
        }

        if (!TextUtils.isEmpty(etMobile2.getText().toString())) {
            member.setMobile2(Integer.valueOf(etMobile2.getText().toString()));
        }
        member.setProfileImage(fileProvider.toString());

        member.setAddressId(addressId);


        Intent intent = new Intent(NewMemberActivity.this, MemberBioActivity.class);
        intent.putExtra("memberString", gson.toJson(member));
        startActivity(intent);

//        databaseHandler.addMember(member, addressId);
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
            member.setAge(calculateAge(tempDate));
            tvAge.setText(calculateAge(tempDate).toString() + " Yrs");
            member.setDOB(tempDate.toString(getString(R.string.date_pattern)));


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
            profileImage.setImageBitmap(takenImage);
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


}
