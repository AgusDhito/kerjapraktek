package com.example.user.mana_livechatv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.example.user.mana_livechatv2.R;
import com.example.user.mana_livechatv2.app.EndPoints;
import com.example.user.mana_livechatv2.app.MyApplication;
import com.example.user.mana_livechatv2.model.User;

public class LoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
    private EditText inputName, inputEmail, inputPassword;
//    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private CheckBox saveLoginCheckBox;
    private Button btnLogin, btnToRegister;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Check for login session. It user is already logged in
         * redirect him to main activity
         * */
        if (MyApplication.getInstance().getPrefManager().getUser() != null) {
            startActivity(new Intent(this, TabbedActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
//        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
//        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
//        inputPassword = (EditText) findViewById(R.id.input_password);

        saveLoginCheckBox = (CheckBox)findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnToRegister = (Button) findViewById(R.id.btn_to_register);

//        inputName.addTextChangedListener(new MyTextWatcher(inputName));
//        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            inputName.setText(loginPreferences.getString("name", ""));
            inputEmail.setText(loginPreferences.getString("email", ""));
            saveLoginCheckBox.setChecked(true);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                login();
            }
        });

        btnToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void login() {
//        if (!validateName()) {
//            return;
//        }
//
//        if (!validateEmail()) {
//            return;
//        }

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(inputName.getWindowToken(), 0);

        final String name = inputName.getText().toString().trim();
        final String email = inputEmail.getText().toString().trim();

        if (saveLoginCheckBox.isChecked()) {
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("name", name);
            loginPrefsEditor.putString("email", email);
            loginPrefsEditor.commit();
        } else {
            loginPrefsEditor.clear();
            loginPrefsEditor.commit();
        }

        // Check for empty data in the form
        if (!name.isEmpty() && !email.isEmpty()) {
            // login user
            StringRequest strReq = new StringRequest(Request.Method.POST,
                    EndPoints.LOGIN, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    Log.e(TAG, "response: " + response);

                    try {
                        JSONObject obj = new JSONObject(response);

                        // check for error flag
                        if (obj.getBoolean("error") == false) {
                            // user successfully logged in

                            JSONObject userObj = obj.getJSONObject("user");
                            User user = new User(userObj.getString("user_id"),
                                    userObj.getString("name"),
                                    userObj.getString("email"));

                            // storing user in shared preferences
                            MyApplication.getInstance().getPrefManager().storeUser(user);

                            // start main activity
                            startActivity(new Intent(getApplicationContext(), TabbedActivity.class));
                            finish();


                        } else {
                            // login error - simply toast the message
                            Toast.makeText(getApplicationContext(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                        }

                        // start main activity
                        startActivity(new Intent(getApplicationContext(), TabbedActivity.class));
                        finish();


                    } catch (JSONException e) {
                        Log.e(TAG, "json parsing error: " + e.getMessage());
                        Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    NetworkResponse networkResponse = error.networkResponse;
                    Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                    Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("name", name);
                    params.put("email", email);

                    Log.e(TAG, "params: " + params.toString());
                    return params;
                }
            };

            //Adding request to request queue
            MyApplication.getInstance().addToRequestQueue(strReq);
        } else {
            // Prompt user to enter credentials
            Toast.makeText(getApplicationContext(),
                    "Data belum lengkap!", Toast.LENGTH_LONG).show();
        }

    }

//    private void requestFocus(View view) {
//        if (view.requestFocus()) {
//            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//        }
//    }

//    // Validating name
//    private boolean validateName() {
//        if (inputName.getText().toString().trim().isEmpty()) {
//            inputLayoutName.setError(getString(R.string.err_msg_name));
//            requestFocus(inputName);
//            return false;
//        } else {
//            inputLayoutName.setErrorEnabled(false);
//        }
//
//        return true;
//    }
//
//    // Validating email
//    private boolean validateEmail() {
//        String email = inputEmail.getText().toString().trim();
//
//        if (email.isEmpty() || !isValidEmail(email)) {
//            inputLayoutEmail.setError(getString(R.string.err_msg_email));
//            requestFocus(inputEmail);
//            return false;
//        } else {
//            inputLayoutEmail.setErrorEnabled(false);
//        }
//
//        return true;
//    }

//    private static boolean isValidEmail(String email) {
//        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }

//    private class MyTextWatcher implements TextWatcher {
//
//        private View view;
//        private MyTextWatcher(View view) {
//            this.view = view;
//        }
//
//        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//        }
//
//        public void afterTextChanged(Editable editable) {
//            switch (view.getId()) {
//                case R.id.input_name:
//                    validateName();
//                    break;
//                case R.id.input_email:
//                    validateEmail();
//                    break;
//            }
//        }
//    }

}
