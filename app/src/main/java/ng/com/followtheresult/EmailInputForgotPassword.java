package ng.com.followtheresult;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

public class EmailInputForgotPassword extends AppCompatActivity {

    EditText email;
    Button verify;
    Dialog myDialog;
    String got_email;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static final String RESEND_CODE = "https://readytoleadafrica.org/rtl_mobile/reset_password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_input_forgot_password);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        email = findViewById(R.id.edtemail);
        verify = findViewById(R.id.verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get email
                got_email = email.getText().toString().trim();
                if(got_email.equals("") || !got_email.matches(emailPattern)){
                    email.setError("Wrong email address");
                }else{
                    //show dialog
                    myDialog = new Dialog(EmailInputForgotPassword.this);
                    myDialog.setContentView(R.layout.custom_popup_loading);
                    TextView text = myDialog.findViewById(R.id.text);
                    text.setText("Sending code to your email... Please wait");
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
                                            Toast.makeText(EmailInputForgotPassword.this, "Code sent successfully", Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(getApplicationContext(), EnterCodeForgetPassword.class);
                                            i.putExtra("email", got_email);
                                            startActivity(i);

                                        }else{
                                            myDialog.dismiss();
                                            Toast.makeText(EmailInputForgotPassword.this, "Code sending failed!, Please try again", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                    catch (JSONException e){
                                        e.printStackTrace();
                                        myDialog.dismiss();
                                        Toast.makeText(EmailInputForgotPassword.this, "Error!", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(EmailInputForgotPassword.this, "Failed, check network connectivity and try again", Toast.LENGTH_SHORT).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<>();
                            params.put("email", got_email);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(EmailInputForgotPassword.this);
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


            }
        });
    }
}