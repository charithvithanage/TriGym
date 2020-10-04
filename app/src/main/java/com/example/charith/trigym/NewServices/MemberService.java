package com.example.charith.trigym.NewServices;

import android.content.Context;
import android.util.Log;

import com.example.charith.trigym.Constants;
import com.example.charith.trigym.Convertors.BooleanTypeAdapter;
import com.example.charith.trigym.Entities.Member;
import com.example.charith.trigym.Interfaces.ResponseListener;
import com.example.charith.trigym.Interfaces.SuccessErrorListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.example.charith.trigym.Utils.createEntityDirectory;
import static com.example.charith.trigym.Utils.createMembersDirectory;
import static com.example.charith.trigym.Utils.readJSONFile;
import static com.example.charith.trigym.Utils.readMembersJSONFile;
import static com.example.charith.trigym.Utils.saveJSONFile;

public class MemberService {
    private static final String TAG = "TriGym";

    private static Gson gson;

    private static MemberService memberService = new MemberService();

    public MemberService() {

    }

    public static MemberService getInstance() {
        GsonBuilder builder = new GsonBuilder()
                .registerTypeAdapter(DateTime.class, new BooleanTypeAdapter());
        gson = builder.create();
        return memberService;
    }

    public void saveMember(Context context, Member member, final ResponseListener listner) {

        String jsonString = gson.toJson(member);
        Log.d(TAG, jsonString);


        File mediaStorageDir = createEntityDirectory(context, Constants.MEMBERS);

        readJSONFile(context, mediaStorageDir, new ResponseListener() {
            @Override
            public void onSuccess(Context activity, String existingMembers) {
                List<Member> tempArray = new ArrayList<>();

                if (existingMembers != null & !existingMembers.equals("")) {
                    try {
                        JSONArray jsonArrayExisting = new JSONArray(existingMembers);

                        for (int i = 0; i < jsonArrayExisting.length(); i++) {
                            Member member = gson.fromJson(jsonArrayExisting.getString(i), Member.class);
                            Log.d(TAG, gson.toJson(member));
                            tempArray.add(member);
                        }

                        tempArray.add(member);


                        saveJSONFile(gson.toJson(tempArray), mediaStorageDir, new SuccessErrorListener() {
                            @Override
                            public void onSuccess() {
                                listner.onSuccess(activity, null);

                            }

                            @Override
                            public void onError(String error) {
                                listner.onError(activity, error);

                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    tempArray.add(member);

                    saveJSONFile(gson.toJson(tempArray), mediaStorageDir, new SuccessErrorListener() {
                        @Override
                        public void onSuccess() {
                            listner.onSuccess(activity, null);
                        }

                        @Override
                        public void onError(String error) {
                            listner.onError(activity, error);
                        }
                    });

                }
            }

            @Override
            public void onError(Context activity, String error) {
                listner.onError(context, error);
            }
        });

    }

    public void deleteMember(Context context, Member selectedMember, final ResponseListener listner) {
        File mediaStorageDir = createMembersDirectory(context, Constants.MEMBERS);

        readMembersJSONFile(context, mediaStorageDir, new ResponseListener() {
            @Override
            public void onSuccess(Context activity, String existingMembers) {
                List<Member> updateServerObjects = new ArrayList<>();

                try {
                    JSONArray jsonArray = new JSONArray(existingMembers);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        Member member = gson.fromJson(jsonArray.getString(i), Member.class);

                        if (!member.getMember_id().equals(selectedMember.getMember_id())) {
                            updateServerObjects.add(member);
                        }

                    }

                    listner.onSuccess(context, gson.toJson(updateServerObjects));

                } catch (JSONException e) {
                    e.printStackTrace();
                    listner.onError(context, e.toString());

                }
            }

            @Override
            public void onError(Context activity, String error) {
                listner.onError(context, error);
            }
        });


    }

    public void getAllMembers(Context context, final ResponseListener listner) {
        File mediaStorageDir = createMembersDirectory(context, Constants.MEMBERS);

        readMembersJSONFile(context, mediaStorageDir, new ResponseListener() {
            @Override
            public void onSuccess(Context activity, String existingMembers) {
                listner.onSuccess(activity, existingMembers);
            }

            @Override
            public void onError(Context activity, String error) {
                listner.onError(context, error);
            }
        });


    }

    //    public void updateMembers(Context context, String toJson, int size, final VolleyCallback callback) {
//        RequestQueue queue = Volley.newRequestQueue(context);
//
//        Log.d(TAG, toJson);
//        Log.d(TAG, Config.update_members_url);
//
//        JSONArray object = null;
//
//        try {
//            object = new JSONArray(toJson);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//
//        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.PUT, Config.update_members_url + "/" + size, object, new Response.Listener<JSONArray>() {
//            @Override
//            public void onResponse(JSONArray response) {
//                callback.onSuccess(response);
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                callback.onError(error.toString());
//            }
//        });
//        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(50000, 5, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//        queue.add(jsonArrayRequest);
//    }
//
    public void addMembers(Context context, String toJson, final ResponseListener listner) {
        Log.d(TAG, toJson);

        File mediaStorageDir = createMembersDirectory(context, Constants.MEMBERS);

        saveJSONFile(toJson, mediaStorageDir, new SuccessErrorListener() {
            @Override
            public void onSuccess() {
                listner.onSuccess(context, toJson);
            }

            @Override
            public void onError(String error) {
                listner.onError(context, error);
            }
        });
    }

}
