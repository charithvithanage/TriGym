package com.example.charith.trigym;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.system.ErrnoException;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.charith.trigym.Entities.Address;
import com.example.charith.trigym.Entities.Member;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.ViewHolder;

import org.joda.time.DateTime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NewMemberActivity extends AppCompatActivity {
    Spinner genderSpinner;

    TextView tvDOB, tvAge;

    DateTime today = new DateTime();

    Button btnNext, btnEdit;

    ImageView profileImage;

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 2;

    Gson gson;

    Member member;

    TextInputEditText etName, etLine1, etLine2, etLine3, etCity, etMobile1, etMobile2, etNIC, etHeight, etWeight, etHealthCondition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_member);

        init();
    }

    private void init() {
        member = new Member();
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new DateTimeSerializer());
        gson = builder.create();

        etName = findViewById(R.id.etName);
        etLine1 = findViewById(R.id.etLine1);
        etLine2 = findViewById(R.id.etLine2);
        etLine3 = findViewById(R.id.etLine3);
        etCity = findViewById(R.id.etCity);
        etMobile1 = findViewById(R.id.etMobile1);
        etMobile2 = findViewById(R.id.etMobile2);
        etNIC = findViewById(R.id.etNIC);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etHealthCondition = findViewById(R.id.etHealthConditoin);

        genderSpinner = findViewById(R.id.spinner);
        btnNext = findViewById(R.id.btnNext);
        tvDOB = findViewById(R.id.tvDOB);
        tvAge = findViewById(R.id.tvAge);
        btnEdit = findViewById(R.id.btnEdit);
        profileImage = findViewById(R.id.profileImage);

        List<String> gender = new ArrayList<>();
        gender.add("Male");
        gender.add("Female");

        member.setGender(gender.get(0));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gender);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(arrayAdapter);

        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                member.setGender(item);

                Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                MY_PERMISSIONS_REQUEST_CAMERA);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant

                        return;
                    } else {
                        startActivityForResult(getPickImageChooserIntent(), 200);
                    }
                } else {
                    startActivityForResult(getPickImageChooserIntent(), 200);
                }
            }
        });
    }

    private void saveMember() {
        Address address = new Address();
        address.setLine1(etLine1.getText().toString());
        address.setLine2(etLine2.getText().toString());
        address.setLine3(etLine3.getText().toString());
        address.setCity(etCity.getText().toString());

        member.setName(etName.getText().toString());
        member.setAddress(address);
        member.setHealthCondition(etHealthCondition.getText().toString());
        member.setHeight(Float.valueOf(etHeight.getText().toString()));
        member.setWeight(Float.valueOf(etWeight.getText().toString()));
        member.setNIC(etNIC.getText().toString());

        if (!TextUtils.isEmpty(etMobile1.getText().toString())) {
            member.setMobile1(Integer.valueOf(etMobile1.getText().toString()));
        }

        if (!TextUtils.isEmpty(etMobile2.getText().toString())) {
            member.setMobile1(Integer.valueOf(etMobile2.getText().toString()));
        }

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("memberString", gson.toJson(member));
        editor.apply();
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

            tvDOB.setText(tempDate.toString("YYYY/MM/dd"));
            member.setAge(calculateAge(tempDate));
            tvAge.setText(calculateAge(tempDate).toString() + " Yrs");
            member.setDOB(tempDate);


        }
    };

    public Intent getPickImageChooserIntent() {

//        // Determine Uri of camera image to save.
//        Uri outputFileUri = getCaptureImageOutputUri();
//
//        // collect all camera intents
//        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        if (captureIntent.resolveActivity(getPackageManager()) != null) {
//            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT,outputFileUri);
//        }

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        return cameraIntent;
    }

    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "pickImageResult.jpeg"));
        }
        return outputFileUri;
    }

    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null && data.getData() != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }


    /**
     * Test if we can open the given Android URI to test if permission required error is thrown.<br>
     */
    public boolean isUriRequiresPermissions(Uri uri) {
        ContentResolver resolver = getContentResolver();
        InputStream stream = null;
        try {
            stream = resolver.openInputStream(uri);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
//            Uri imageUri = getPickImageResultUri(data);
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            profileImage.setImageBitmap(photo);
            // For API >= 23 we need to check specifically that we have permissions to read external storage,
            // but we don't know if we need to for the URI so the simplest is to try open the stream and see if we get error.
            boolean requirePermissions = false;
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
//                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                    isUriRequiresPermissions(imageUri)) {
//                // request permissions and handle the result in onRequestPermissionsResult()
//                requirePermissions = true;
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
//            }

            if (!requirePermissions) {
//                profileImage.setImageURI(imageUri);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! do the
                    startActivityForResult(getPickImageChooserIntent(), 200);

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
