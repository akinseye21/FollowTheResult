package ng.com.followtheresult;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class FragmentDashboard extends Fragment implements Backpressedlistener {

    public static Backpressedlistener backpressedlistener;

    TextView fullname, state, lga;
    SharedPreferences preferences, preferences2;

    TextView countForArrival, countForProcess, countForResult;
    TextView lgaCount;
    AppCompatButton btn;
//    Dialog myDialog;
    LinearLayout lin_lga, lin_lgacount;
    String email, got_fullname, got_state, got_lga, usertype, arrival_check, process_check, result_check;
    SharedPreferences.Editor myEdit;
    String got_email, got_password;
    int SP_Process_count, process_calc;

    public static final String LOGIN = "https://readytoleadafrica.org/rtl_mobile/login";



    public FragmentDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        myEdit = preferences.edit();
        got_email = preferences.getString("email", "not available");
        got_password = preferences.getString("password", "not available");
        got_fullname = preferences.getString("fullname", "not available");
        got_state = preferences.getString("state", "not available");
        got_lga = preferences.getString("lga", "not available");
        usertype = preferences.getString("usertype", "");
        arrival_check = preferences.getString("arrival_check", "not available");
        process_check = preferences.getString("process_check", "not available");
        result_check = preferences.getString("result_submit", "not available");
//        SP_Process_count = preferences.getInt("process_count", 0);

//        process_calc = SP_Process_count + Integer.parseInt(process_check);

        preferences2 = getActivity().getSharedPreferences("lga_count", Context.MODE_PRIVATE);
        final String lga_count = preferences2.getString("lgacount", "");

//        myDialog = new Dialog(getContext());
//        myDialog.setContentView(R.layout.custom_popup_loading);
//        TextView text = myDialog.findViewById(R.id.text);
//        text.setText("Loading Checklist Update... Please wait");
//        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        myDialog.setCanceledOnTouchOutside(false);
//        myDialog.show();

        //Login again to know the count for arrival, process and result checklist
//        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
////                                progressBar.setVisibility(View.GONE);
//
//                        System.out.println("Login Response = "+response);
//
//                        try{
//                            JSONObject jsonObject = new JSONObject(response);
//
//                            String status = jsonObject.getString("status");
//                            String fullname = jsonObject.getString("fullname");
//                            String phone = jsonObject.getString("phone");
//                            String userType = jsonObject.getString("usertype");
//                            String lga = jsonObject.getString("lga");
//                            String state = jsonObject.getString("state");
//                            String arrival_check = jsonObject.getString("arrival_check");
//                            String process_check = jsonObject.getString("process_check");
//                            String result_submit = jsonObject.getString("result_submit");
//
//
//                            if(status.equals("login successful")){
//
//                                //create shared preference for login
////                                myEdit.putString( "email", got_email);
////                                myEdit.putString("fullname", fullname);
////                                myEdit.putString("state", state);
////                                myEdit.putString("lga", lga);
////                                myEdit.putString("usertype", userType);
//                                myEdit.putString("arrival_check", arrival_check);
//                                myEdit.putString("process_check", process_check);
//                                myEdit.putString("result_submit", result_submit);
//                                myEdit.commit();
//
//                                myDialog.dismiss();
//
//                            }
//
//                        }
//                        catch (JSONException e){
//                            e.printStackTrace();
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//
//                        if(volleyError == null){
//                            return;
//                        }
//                    }
//                }){
//            @Override
//            protected Map<String, String> getParams(){
//                Map<String, String> params = new HashMap<>();
//                params.put("email", got_email);
//                params.put("password", got_password);
//                return params;
//            }
//        };
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        stringRequest.setRetryPolicy(retryPolicy);
//        requestQueue.add(stringRequest);
//        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
//            @Override
//            public void onRequestFinished(Request<Object> request) {
//                requestQueue.getCache().clear();
//            }
//        });



        countForArrival = v.findViewById(R.id.countForArrival);
        countForProcess = v.findViewById(R.id.countForProcess);
        countForResult = v.findViewById(R.id.countForResult);
        btn = v.findViewById(R.id.btn);
        lgaCount = v.findViewById(R.id.lga_count);

        lgaCount.setText(lga_count);

        //check if processes have been submitted
        if (arrival_check.equals("0")){
            countForArrival.setText("0%");
        }else{
            countForArrival.setText("100%");
        }
        if (process_check.equals("0")){
            countForProcess.setText("0%");
        }else{
            countForProcess.setText("100%");
        }
        if (result_check.equals("0")){
            countForResult.setText("0%");
        }else{
            countForResult.setText("100%");
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Dashboard)getActivity()).navigateFragment(1);
            }
        });


        fullname = v.findViewById(R.id.fullname);
        state = v.findViewById(R.id.state);
        lga = v.findViewById(R.id.lga);
        lin_lga = v.findViewById(R.id.lin_lga);
        lin_lgacount = v.findViewById(R.id.lin_lgacount);

        fullname.setText(got_fullname);
        state.setText(got_state);
        lga.setText(got_lga);

        if(usertype.equals("admin")){
            lin_lga.setVisibility(View.GONE);
        }else{
            lin_lgacount.setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public void onBackPressed() {
        Dialog myDialogFinish = new Dialog(getContext());
        myDialogFinish.setContentView(R.layout.custom_popup_prompt);
        TextView text = myDialogFinish.findViewById(R.id.text);
        text.setText("Do you want to logout of the application?");
        AppCompatButton proceed = myDialogFinish.findViewById(R.id.proceed);
        AppCompatButton close = myDialogFinish.findViewById(R.id.close);
        proceed.setText("YES");
        close.setText("NO");
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialogFinish.dismiss();
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent i = new Intent(getContext(), LoginPage.class);
                startActivity(i);
            }
        });
        myDialogFinish.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogFinish.setCanceledOnTouchOutside(false);
        myDialogFinish.show();
    }

    public interface OnFragmentInteractionListener {
    }

}