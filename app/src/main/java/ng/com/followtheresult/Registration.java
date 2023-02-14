package ng.com.followtheresult;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity {

    EditText fullname, email, phone, password;
    Spinner lga, state;
    AppCompatButton register;
    ProgressBar prglga;
    Dialog myDialog;
    TextView verify;

    String user_fullname, user_email, user_phone, user_password, user_lga, user_state;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


    String[] states = new String[]{"ABIA", "ADAMAWA", "AKWA IBOM", "ANAMBRA", "BAUCHI", "BAYELSA", "BENUE", "BORNO", "CROSS RIVER", "DELTA", "EBONYI",
            "EDO", "EKITI", "ENUGU", "FCT", "GOMBE", "IMO", "JIGAWA", "KADUNA", "KANO", "KATSINA", "KEBBI", "KOGI", "KWARA", "LAGOS",
            "NASARAWA", "NIGER", "OGUN", "ONDO", "OSUN", "OYO", "PLATEAU", "RIVERS", "SOKOTO", "TARABA", "YOBE", "ZAMFARA"};


    ArrayAdapter<String> stateadapter;
    ArrayAdapter<String> lgaadapter;

    public static final String SIGNUP = "https://readytoleadafrica.org/rtl_mobile/new_register";
    public static final String GET_STATE = "https://readytoleadafrica.org/rtl_mobile/getstates";
    public static final String GET_LGA = "https://readytoleadafrica.org/rtl_mobile/getlga";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        fullname = findViewById(R.id.edtfullname);
        email = findViewById(R.id.edtemail);
        phone = findViewById(R.id.edtphone);
        password = findViewById(R.id.edtpassword);
        lga = findViewById(R.id.spinnerlga);
        state = findViewById(R.id.spinnerstate);
        register = findViewById(R.id.signup);
        prglga = findViewById(R.id.progressBarLGA);
        verify = findViewById(R.id.verify);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), EmailInput.class);
                startActivity(i);
            }
        });



        //spinner state with states array
        stateadapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_spinner_text, R.id.tx, states);
        state.setAdapter(stateadapter);



        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                prglga.setVisibility(View.VISIBLE);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_LGA,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                System.out.println("Server response for "+state.getSelectedItem().toString()+" = "+response);

                                try{
                                    JSONArray jsonArray = new JSONArray(response);
                                    String[] lgas = new String[jsonArray.length()];
                                    for(int w=0; w<=jsonArray.length()-1; w++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(w);
                                        String my_lga = jsonObject.getString("lga");

                                        lgas[w] = my_lga;
                                    }

                                    lgaadapter = new ArrayAdapter<>(getApplicationContext(), R.layout.simple_spinner_text, R.id.tx, lgas);
                                    lga.setAdapter(lgaadapter);
                                    prglga.setVisibility(View.GONE);
                                }
                                catch (JSONException e){
                                    e.printStackTrace();
                                    prglga.setVisibility(View.GONE);
                                    Toast.makeText(Registration.this, "Error Loading LGA's please try again", Toast.LENGTH_SHORT).show();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {

                                if(volleyError == null){
                                    return;
                                }
                                Toast.makeText(Registration.this, "Error Loading, please check network connectivity", Toast.LENGTH_SHORT).show();

                            }
                        }){
                    @Override
                    protected Map<String, String> getParams(){
                        Map<String, String> params = new HashMap<>();
                        params.put("dstate", state.getSelectedItem().toString());
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

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get all fields to be sent
                user_fullname = fullname.getText().toString().trim();
                user_email = email.getText().toString().trim();
                user_phone = phone.getText().toString().trim();
                user_password = password.getText().toString().trim();
                user_state = state.getSelectedItem().toString();
                user_lga = lga.getSelectedItem().toString();

                if (user_fullname.equals("")){
                    fullname.setError("Wrong entry");
                }
                else if (!user_email.matches(emailPattern)){
                    email.setError("Invalid email address");
                }
                else if (user_phone.equals("") || user_phone.length()<11){
                    phone.setError("Invalid phone number");
                }
                else if (user_password.length()<6){
                    password.setError("Password should be more than 6 characters");
                }else{
                    //show dialog
                    myDialog = new Dialog(Registration.this);
                    myDialog.setContentView(R.layout.custom_popup_loading);
                    TextView text = myDialog.findViewById(R.id.text);
                    text.setText("Signing you up. Please wait");
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.show();

                    //send to server
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGNUP,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    System.out.println("Signup Response = "+response);
//                                    Toast.makeText(Registration.this, "Response = "+response, Toast.LENGTH_SHORT).show();

                                    try{
                                        JSONObject jsonObject = new JSONObject(response);

                                        String status = jsonObject.getString("status");
                                        String notification = jsonObject.getString("notification");

                                        if(status.equals("successful")){
                                            myDialog.dismiss();
                                            //tell user to check email and phone using string notification
                                            Toast.makeText(Registration.this, "Registration "+status+"\n"+notification, Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(Registration.this, EnterCode.class);
                                            i.putExtra("email", user_email);
                                            i.putExtra("fullname", user_fullname);
                                            i.putExtra("state", user_state);
                                            i.putExtra("lga", user_lga);
                                            startActivity(i);

                                        }else{
                                            myDialog.dismiss();
                                            Toast.makeText(Registration.this, "Registration failed!, Please try again with another email", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                    catch (JSONException e){
                                        e.printStackTrace();
                                        myDialog.dismiss();


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
                                    Toast.makeText(Registration.this, "Failed, check network connectivity and try again", Toast.LENGTH_SHORT).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<>();
                            params.put("email", user_email);
                            params.put("phone", user_phone);
                            params.put("password", user_password);
                            params.put("fullname", user_fullname);
                            params.put("dstate", user_state);
                            params.put("lga", user_lga);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(Registration.this);
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