package com.example.user.mana_livechatv2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.mana_livechatv2.app.EndPoints;
import com.example.user.mana_livechatv2.app.MyApplication;
import com.example.user.mana_livechatv2.helper.SendEmail;
import com.example.user.mana_livechatv2.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by USER on 21/07/2016.
 */
public class RegisterActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
    private EditText inputName, inputEmail, inputFullname, inputPassword, inputPassword2;
    //    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private Button btnToLogin, btnRegister;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        inputName = (EditText) findViewById(R.id.input_name);
        inputFullname = (EditText) findViewById(R.id.input_fullname);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputPassword2 = (EditText) findViewById(R.id.input_password2);

        btnToLogin = (Button) findViewById(R.id.btn_to_login);
        btnRegister = (Button) findViewById(R.id.btn_register);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                register();
            }
        });

        btnToLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void register() {
//        if (!validateName()) {
//            return;
//        }
//
//        if (!validateEmail()) {
//            return;
//        }

        pDialog.setMessage("Mendaftarkan...");
        showDialog();

        final String name = inputName.getText().toString().trim();
        final String email = inputEmail.getText().toString().trim();
        final String fullname = inputFullname.getText().toString().trim();
        final String password = inputPassword.getText().toString().trim();
        final String password2 = inputPassword2.getText().toString().trim();

        Random rd = new Random();
        int kodeRegis = rd.nextInt(999999-100000) + 100000;
        final String kode = Integer.toString(kodeRegis);

        // Check for empty data in the form
        if (!name.isEmpty() && !email.isEmpty() && !fullname.isEmpty() &&
                !password.isEmpty() && !password2.isEmpty()) {
            if (password.equals(password2)) {
                // login user
                StringRequest strReq = new StringRequest(Request.Method.POST,
                        EndPoints.REGISTER, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "response: " + response);
                        hideDialog();

                        sendEmail();
//                        try {
//                            JSONObject obj = new JSONObject(response);
//
//                            // check for error flag
//                            if (obj.getBoolean("error") == false) {
//                                // user successfully logged in
//
//                                JSONObject userObj = obj.getJSONObject("user");
//                                User user = new User(userObj.getString("user_id"),
//                                        userObj.getString("name"),
//                                        userObj.getString("email"),
//                                        userObj.getString("fullname"));
//
//                                // storing user in shared preferences
//                                MyApplication.getInstance().getPrefManager().storeUser(user);

                                // start main activity
//                                startActivity(new Intent(getApplicationContext(), TabbedActivity.class));
//                                finish();


//                            } else {
//                                // login error - simply toast the message
//                                Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
//                            }

                            // start main activity
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
//
//
//                        } catch (JSONException e) {
//                            Log.e(TAG, "json parsing error: " + e.getMessage());
//                            Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        NetworkResponse networkResponse = error.networkResponse;
                        Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                        hideDialog();
                        Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", name);
                        params.put("email", email);
                        params.put("fullname", fullname);
                        params.put("password", password);
                        params.put("kode", kode);


                        Log.e(TAG, "params: " + params.toString());
                        return params;
                    }
                };

                //Adding request to request queue
                MyApplication.getInstance().addToRequestQueue(strReq);
            }
            else {
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(),
                        "Password tidak sama!", Toast.LENGTH_LONG).show();
            }
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),
                    "Data belum lengkap!", Toast.LENGTH_LONG).show();
        }

    }

    private void sendEmail() {
        //Getting content for email
        String name = inputName.getText().toString().trim();
        String email = inputEmail.getText().toString().trim();
        String password = inputEmail.getText().toString().trim();

        String subject = "Konfirmasi Registrasi MANA";
        String message = "Selamat!";

        //Log.d("send email","masuk method");
        //Creating SendMail object
        SendEmail sm = new SendEmail(this, email, subject, message);

        //Executing sendmail to send email
        sm.execute();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
