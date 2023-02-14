package ng.com.followtheresult;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
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

public class EnterCodeForgetPassword extends AppCompatActivity {

    EditText num1, num2, num3, num4;
    Button verify;
    Dialog myDialog;
    String code;
    String email, fullname, state, lga;
    TextView resendCode;

    public static final String VERIFY = "https://readytoleadafrica.org/rtl_mobile/verify_reset_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code_forget_password);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        email = i.getStringExtra("email");

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
                myDialog = new Dialog(EnterCodeForgetPassword.this);
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

                                    if(status.equals("successful")){
                                        myDialog.dismiss();
                                        //tell user to check email and phone using string notification
                                        Toast.makeText(EnterCodeForgetPassword.this, "Verification "+status, Toast.LENGTH_SHORT).show();

                                        //create shared preference for login
//                                        myEdit.putString( "email", email);
//                                        myEdit.putString("fullname", fullname);
//                                        myEdit.putString("state", state);
//                                        myEdit.putString("lga", lga);
//                                        myEdit.commit();

                                        Intent i = new Intent(EnterCodeForgetPassword.this, ReesetPassword.class);
                                        i.putExtra("email", email);
                                        startActivity(i);

                                    }else{
                                        myDialog.dismiss();
                                        Toast.makeText(EnterCodeForgetPassword.this, "Verification failed. Please try again", Toast.LENGTH_SHORT).show();
                                    }


                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                    myDialog.dismiss();
                                    Toast.makeText(EnterCodeForgetPassword.this, "Verification failed", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(EnterCodeForgetPassword.this, "Failed, check network connectivity and try again", Toast.LENGTH_SHORT).show();
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

                RequestQueue requestQueue = Volley.newRequestQueue(EnterCodeForgetPassword.this);
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