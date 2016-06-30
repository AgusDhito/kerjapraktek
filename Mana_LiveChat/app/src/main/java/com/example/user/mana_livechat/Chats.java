package com.example.user.mana_livechat;

/**
 * Created by user on 21/06/2016.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.user.mana_livechat.ChatAdapter;
import com.example.user.mana_livechat.ChatMessage;
import com.example.user.mana_livechat.CommonMethods;
import com.example.user.mana_livechat.R;
import com.example.user.mana_livechat.AppController;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Chats extends Fragment implements OnClickListener {

    private EditText msg_edittext;
    private String user1 = "upin", user2 = "ipin";
    private Random random;
    public static ArrayList<ChatMessage> chatlist;
    public static ChatAdapter chatAdapter;
    ListView msgListView;
    private TextView statstext;
    private String jsonResponse;
    //Link untuk menerima json array
    private String urlJsonObj = "http://202.56.170.37/mobile-agro/read_post.php";
    private String urlJsonArry = "http://202.56.170.37/mobile-agro/test.php";
    // Progress dialog
    private ProgressDialog pDialog;
    private final String tag_json_arry = "json_array_req";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_layout, container, false);
        Log.d("debug", "masuk kelas chats");

        random = new Random();
//        statstext = (TextView) view.findViewById(R.id.statsText);
//        statstext.setText("Ini text status");

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(
                "Chats");
        msg_edittext = (EditText) view.findViewById(R.id.messageEditText);
        msgListView = (ListView) view.findViewById(R.id.msgListView);
        ImageButton sendButton = (ImageButton) view
                .findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);

        // ----Set autoscroll of listview when a new message arrives----//
        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);
        pDialog = new ProgressDialog(((MainActivity) getActivity()));
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        chatlist = new ArrayList<ChatMessage>();
        chatAdapter = new ChatAdapter(getActivity(), chatlist);
        msgListView.setAdapter(chatAdapter);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    public void sendTextMessage(View v) {
        String message = msg_edittext.getEditableText().toString();
        if (!message.equalsIgnoreCase("")) {
            makeMessage(message);
        }
    }

    private void makeMessage (String message) {
        final ChatMessage chatMessage = new ChatMessage(user1, user2,
                message, "" + random.nextInt(1000), true);
        chatMessage.setMsgID();
        chatMessage.body = message;
        chatMessage.Date = CommonMethods.getCurrentDate();
        chatMessage.Time = CommonMethods.getCurrentTime();
        addMessage(chatMessage);

    }
    private void addMessage(ChatMessage chatMessage) {
        msg_edittext.setText("");
        chatAdapter.add(chatMessage);
        chatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageButton:
                //sendTextMessage(v);
                //makeJsonArrayRequest();
                sendJsonObject();
        }
    }

    private void sendJsonObject() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlJsonObj,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getActivity(),response,Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("u_pengguna","Hari");
                params.put("u_narasumber","Saputra");
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
//        final JSONObject json = new JSONObject();
//        try {
//            json.put("u_narasumber", "Saputra");
//            json.put("u_pengguna", "Hari");
//            makeMessage(json.toString());
//        }
//        catch (JSONException e) {
//            e.printStackTrace();
//            Log.d("debug", "masuk error JsonObject");
//        }
//
//        JsonObjectRequest jsonObjSend = new JsonObjectRequest(Method.POST,
//                urlJsonObj, json,
//                new Response.Listener<JSONObject>() {
//
//            @Override
//            public void onResponse(JSONObject response) {
//                Toast.makeText(getActivity().getApplicationContext(),
//                        "Udah masuk onResponse()", Toast.LENGTH_SHORT).show();
//                try {
//                    String pengguna = response.getString("u_pengguna");
//                    String narasumber = response.getString("u_narasumber");
//                    jsonResponse = "";
//                    jsonResponse += "Pengguna: " + pengguna + "\n\n";
//                    jsonResponse += "Narasumber : " + narasumber + "\n\n";
//                    makeMessage(jsonResponse);
//                }
//                catch (JSONException e) {
//                    e.printStackTrace();
//                    Toast.makeText(((MainActivity) getActivity()).getApplicationContext(),
//                            "Error: " + e.getMessage(),
//                            Toast.LENGTH_LONG).show();
//                }
//
//
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getActivity().getApplicationContext(),
//                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.d("debug", "masuk error JsonObjectRequest");
//                makeMessage(error.getMessage());
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjSend);
    }

    private void makeJsonObjectRequest() {

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    // Parsing json object response
                    // response will be a json object
                    String name = response.getString("name");
                    String email = response.getString("email");
                    JSONObject phone = response.getJSONObject("phone");
                    String home = phone.getString("home");
                    String mobile = phone.getString("mobile");

                    jsonResponse = "";
                    jsonResponse += "Name: " + name + "\n\n";
                    jsonResponse += "Email: " + email + "\n\n";
                    jsonResponse += "Home: " + home + "\n\n";
                    jsonResponse += "Mobile: " + mobile + "\n\n";


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(((MainActivity) getActivity()).getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    /**
     * Method to make json array request where response starts with [
     * */
    private void makeJsonArrayRequest() {
        showpDialog();
        JsonArrayRequest req = new JsonArrayRequest(urlJsonArry,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //Log.d("debug", response.toString());

                        try {
                            // Parsing json array response
                            // loop through each json object
                            jsonResponse = "";
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject product = (JSONObject) response
                                        .get(i);

                                String name = product.getString("id");
                                String email = product.getString("header");
                                String home = product.getString("content");
                                jsonResponse += "Name: " + name + "\n\n";
                                jsonResponse += "Email: " + email + "\n\n";
                                jsonResponse += "Home: " + home + "\n\n";
                            }
                            makeMessage(jsonResponse);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(((MainActivity) getActivity()).getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                        hidepDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("debug", "Error: " + error.getMessage());
                error.printStackTrace();
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidepDialog();
                makeMessage(error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req, tag_json_arry);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
