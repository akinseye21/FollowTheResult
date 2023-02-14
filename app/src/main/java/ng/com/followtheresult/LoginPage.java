package ng.com.followtheresult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    AppCompatButton login, signup;
    Dialog myDialog;
    String gotten_email, gotten_password;
    EditText email, password;
    public static final String LOGIN = "https://readytoleadafrica.org/rtl_mobile/login";
    SharedPreferences preferences;
    TextView forgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final SharedPreferences.Editor myEdit = preferences.edit();

        forgotPassword = findViewById(R.id.forgot_password);
        email = findViewById(R.id.edtemail);
        password = findViewById(R.id.edtpassword);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //forgot password page
                Intent i = new Intent(getApplicationContext(), EmailInputForgotPassword.class);
                startActivity(i);
            }
        });

        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotten_email = email.getText().toString().trim();
                gotten_password = password.getText().toString().trim();

                myDialog = new Dialog(LoginPage.this);
                myDialog.setContentView(R.layout.custom_popup_loading);
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(false);
                myDialog.show();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
//                                progressBar.setVisibility(View.GONE);

                                System.out.println("Login Response = "+response);

                                try{
                                    JSONObject jsonObject = new JSONObject(response);

                                    String status = jsonObject.getString("status");
                                    String notification = jsonObject.getString("notification");
                                    String email = jsonObject.getString("email");
                                    String fullname = jsonObject.getString("fullname");
                                    String phone = jsonObject.getString("phone");
                                    String userType = jsonObject.getString("usertype");
                                    String lga = jsonObject.getString("lga");
                                    String state = jsonObject.getString("state");
                                    String arrival_check = jsonObject.getString("arrival_check");
                                    String process_check = jsonObject.getString("process_check");
                                    String result_submit = jsonObject.getString("result_submit");


                                    if(status.equals("login successful")){
                                        myDialog.dismiss();
                                        Toast.makeText(LoginPage.this, status, Toast.LENGTH_SHORT).show();

                                        //create shared preference for login
                                        myEdit.putString( "email", email);
                                        myEdit.putString("fullname", fullname);
                                        myEdit.putString("state", state);
                                        myEdit.putString("lga", lga);
                                        myEdit.putString("usertype", userType);
                                        myEdit.putString("arrival_check", arrival_check);
                                        myEdit.putString("process_check", process_check);
                                        myEdit.putString("result_submit", result_submit);
                                        myEdit.commit();

                                        if (userType.equals("admin")){
                                            //go to state level view
                                            //move to dashboard
                                            Intent i = new Intent(LoginPage.this, Dashboard.class);
                                            i.putExtra("email", email);
                                            i.putExtra("fullname", fullname);
                                            i.putExtra("state", state);
                                            i.putExtra("lga", lga);
                                            i.putExtra("from", "state_level");
                                            startActivity(i);
                                        }else{
                                            //go to normal view
                                            //move to dashboard
                                            Intent i = new Intent(LoginPage.this, Dashboard.class);
                                            i.putExtra("email", email);
                                            i.putExtra("fullname", fullname);
                                            i.putExtra("state", state);
                                            i.putExtra("lga", lga);
                                            i.putExtra("from", "lga_level");
                                            startActivity(i);
                                        }


                                    }else{
                                        Toast.makeText(LoginPage.this, "Login failed. Please try again", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                    myDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Login failed. Please try again", Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                if(volleyError == null){
                                    return;
                                }

                                myDialog.dismiss();
                                Toast.makeText(LoginPage.this,  "Network Error", Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<>();
                        params.put("email", gotten_email);
                        params.put("password", gotten_password);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(retryPolicy);
                requestQueue.add(stringRequest);
                requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                    @Override
                    public void onRequestFinished(Request<Object> request) {
                        requestQueue.getCache().clear();
                    }
                });
            }
        });

        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Registration.class);
                startActivity(i);
            }
        });
    }
}