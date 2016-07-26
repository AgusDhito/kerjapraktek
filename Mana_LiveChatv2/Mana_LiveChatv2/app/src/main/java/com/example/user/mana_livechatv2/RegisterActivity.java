package com.example.user.mana_livechatv2;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import com.example.user.mana_livechatv2.app.EndPoints;
import com.example.user.mana_livechatv2.app.MyApplication;
import com.example.user.mana_livechatv2.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by USER on 21/07/2016.
 */
public class RegisterActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();
    private EditText inputName, inputEmail, inputPassword, inputPassword2;
    //    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private Button btnToLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
//        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
//        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputName = (EditText) findViewById(R.id.input_name);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputPassword2 = (EditText) findViewById(R.id.input_password2);

        btnToLogin = (Button) findViewById(R.id.btn_to_login);
        btnRegister = (Button) findViewById(R.id.btn_register);

//        inputName.addTextChangedListener(new MyTextWatcher(inputName));
//        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));

        btnToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

}
