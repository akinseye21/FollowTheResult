package ng.com.followtheresult;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

public class EnterCode extends AppCompatActivity {

    EditText num1, num2, num3, num4;
    Button verify;
    Dialog myDialog;
    String code;
    String email, fullname, state, lga;
    TextView resendCode;
    SharedPreferences preferences;

    public static final String VERIFY = "https://readytoleadafrica.org/rtl_mobile/verify";
    public static final String RESEND_CODE = "https://readytoleadafrica.org/rtl_mobile/resend_verification_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final SharedPreferences.Editor myEdit = preferences.edit();

        Intent i = getIntent();
        email = i.getStringExtra("email");
        fullname = i.getStringExtra("fullname");
        state = i.getStringExtra("state");
        lga = i.getStringExtra("lga");

        num1 = findViewById(R.id.edt1);
        num2 = findViewById(R.id.edt2);
        num3 = findViewById(R.id.edt3);
        num4 = findViewById(R.id.edt4);
        verify = findViewById(R.id.verify);
        resendCode = findViewById(R.id.resend_code);

        num1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (num1.getText().toString().length()==1){
                    num2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        num2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (num2.getText().toString().length()==1){
                    num3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        num3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (num3.getText().toString().length()==1){
                    num4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialog
                myDialog = new Dialog(EnterCode.this);
                myDialog.setContentView(R.layout.custom_popup_loading);
                TextView text = myDialog.findViewById(R.id.text);
                text.setText("Verifying... Please wait");
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(false);
                myDialog.show();

                //get code
                code = num1.getText().toString()+num2.getText().toString()+num3.getText().toString()+num4.getText().toString();

                //send code to DB
                StringRequest stringRequest = new StringRequest(Request.Method.POST, VERIFY,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try{
                                    JSONObject jsonObject = new JSONObject(response);

                                    String status = jsonObject.getString("status");
                                    String notification2 = jsonObject.getString("notification");
                                    String email2 = jsonObject.getString("email");
                                    String fullname2 = jsonObject.getString("fullname");
                                    String phone2 = jsonObject.getString("phone");
                                    String userType2 = jsonObject.getString("usertype");
                                    String lga2 = jsonObject.getString("lga");
                                    String state2 = jsonObject.getString("state");
                                    String arrival_check2 = jsonObject.getString("arrival_check");
                                    String process_check2 = jsonObject.getString("process_check");
                                    String result_submit2 = jsonObject.getString("result_submit");

                                    if(status.equals("successful")){
                                        myDialog.dismiss();
                                        //tell user to check email and phone using string notification
                                        Toast.makeText(EnterCode.this, "Verification "+status, Toast.LENGTH_SHORT).show();

                                        //create shared preference for login
                                        myEdit.putString( "email", email);
                                        myEdit.putString("fullname", fullname2);
                                        myEdit.putString("state", state2);
                                        myEdit.putString("lga", lga2);
                                        myEdit.putString("usertype", userType2);
                                        myEdit.putString("arrival_check", arrival_check2);
                                        myEdit.putString("process_check", process_check2);
                                        myEdit.putString("result_submit", result_submit2);
                                        myEdit.commit();

                                        if (userType2.equals("admin")){
                                            //go to state level view
                                            //move to dashboard
                                            Intent i = new Intent(EnterCode.this, Dashboard.class);
                                            i.putExtra("email", email);
                                            i.putExtra("fullname", fullname2);
                                            i.putExtra("state", state2);
                                            i.putExtra("lga", lga2);
                                            i.putExtra("from", "state_level");
                                            startActivity(i);
                                        }else{
                                            //go to normal view
                                            //move to dashboard
                                            Intent i = new Intent(EnterCode.this, Dashboard.class);
                                            i.putExtra("email", email);
                                            i.putExtra("fullname", fullname2);
                                            i.putExtra("state", state2);
                                            i.putExtra("lga", lga2);
                                            i.putExtra("from", "lga_level");
                                            startActivity(i);
                                        }

                                    }else{
                                        myDialog.dismiss();
                                        Toast.makeText(EnterCode.this, "Verification failed. Please try again", Toast.LENGTH_SHORT).show();
                                    }


                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                    myDialog.dismiss();
                                    Toast.makeText(EnterCode.this, "Verification failed", Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                myDialog.dismiss();
                                if(volleyError == null){
                                    return;
                                }
                                Toast.makeText(EnterCode.this, "Failed, check network connectivity and try again", Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        params.put("code", code);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(EnterCode.this);
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

        resendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialog
                myDialog = new Dialog(EnterCode.this);
                myDialog.setContentView(R.layout.custom_popup_loading);
                TextView text = myDialog.findViewById(R.id.text);
                text.setText("Resending Verification Code...");
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(false);
                myDialog.show();

                //send code to DB
                StringRequest stringRequest = new StringRequest(Request.Method.POST, RESEND_CODE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try{
                                    JSONObject jsonObject = new JSONObject(response);

                                    String status = jsonObject.getString("status");

                                    if(status.equals("successful")){
                                        myDialog.dismiss();
                                        //tell user to check email and phone using string notification
                                        Toast.makeText(EnterCode.this, "Code sent successfully", Toast.LENGTH_SHORT).show();

                                    }else{
                                        myDialog.dismiss();
                                        Toast.makeText(EnterCode.this, "Code sending failed!", Toast.LENGTH_SHORT).show();
                                    }


                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                    myDialog.dismiss();
                                    Toast.makeText(EnterCode.this, "Error!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                myDialog.dismiss();
                                if(volleyError == null){
                                    return;
                                }
                                Toast.makeText(EnterCode.this, "Failed, check network connectivity and try again", Toast.LENGTH_SHORT).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(EnterCode.this);
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
    }
}