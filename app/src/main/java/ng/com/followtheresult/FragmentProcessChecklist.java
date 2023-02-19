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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


public class FragmentProcessChecklist extends Fragment {

    TextView fullname, state, lga;


    Dialog myDialog, myDialog2;
    AppCompatButton next;
    public static final String GET_QUESTIONS = "https://readytoleadafrica.org/rtl_mobile/get_questions";
    public static final String SUBMIT_PROCESS = "https://readytoleadafrica.org/rtl_mobile/process_checks";
    public static final String SUBMIT_PROCESS_STATE = "https://readytoleadafrica.org/rtl_mobile/process_checks_state";

    ArrayList<String> arr_id;
    ArrayList<String> arr_question_process;
    ArrayList<String> arr_category_process;
    ArrayList<String> arr_option1_process;
    ArrayList<String> arr_option2_process;
    int count = 1;
    int counter = 0;
    int convert;

    TextView presentQuestion, totalQuestion;
    TextView question;
    RadioGroup radioGroupYesNo;
    RadioButton radioYes, radioNo;

    AppCompatButton previous;
    AppCompatButton submission;

    String[] responses;
    String arrival_check, process_check, result_submit;


    SharedPreferences preferences, preferences2;
    String got_fullname;
    String got_state;
    String got_lga;
    String got_email;
    LinearLayout lin_lga, lin_lgacount;
    TextView lgaCount;
    String usertype;

    public FragmentProcessChecklist() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_process_checklist, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_fullname = preferences.getString("fullname", "not available");
        got_state = preferences.getString("state", "not available");
        got_lga = preferences.getString("lga", "not available");
        got_email = preferences.getString("email", "not available");
        usertype = preferences.getString("usertype", "");
        arrival_check = preferences.getString("arrival_check", "0");
        process_check = preferences.getString("process_check", "0");
        result_submit = preferences.getString("result_submit", "0");

        preferences2 = getActivity().getSharedPreferences("lga_count", Context.MODE_PRIVATE);
        final String lga_count = preferences2.getString("lgacount", "1");

        fullname = v.findViewById(R.id.fullname);
        state = v.findViewById(R.id.state);
        lga = v.findViewById(R.id.lga);
        lin_lga = v.findViewById(R.id.lin_lga);
        lin_lgacount = v.findViewById(R.id.lin_lgacount);
        lgaCount = v.findViewById(R.id.lga_count);

        fullname.setText(got_fullname);
        state.setText(got_state);
        lga.setText(got_lga);
        lgaCount.setText(lga_count);

        if(usertype.equals("admin")){
            lin_lga.setVisibility(View.GONE);
        }else{
            lin_lgacount.setVisibility(View.GONE);
        }

//get the views
        presentQuestion = v.findViewById(R.id.presentquestion);
        totalQuestion = v.findViewById(R.id.numofquestions);
        question = v.findViewById(R.id.question);
        radioGroupYesNo = v.findViewById(R.id.radiogroupYesNo);
        radioYes = v.findViewById(R.id.radioYes);
        radioNo = v.findViewById(R.id.radioNo);
        previous = v.findViewById(R.id.previous);
        next = v.findViewById(R.id.next);

        //for popup
//        popup = v.findViewById(R.id.popup);
//        listView = v.findViewById(R.id.list);


        //show Dialog that is loading the information
        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Loading Checklist. Please wait");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        arr_id = new ArrayList<>();
        arr_question_process = new ArrayList<>();
        arr_category_process = new ArrayList<>();
        arr_option1_process = new ArrayList<>();
        arr_option2_process = new ArrayList<>();

        //load checklist question
        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_QUESTIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                                progressBar.setVisibility(View.GONE);
                        try{


                            JSONArray jsonArray = new JSONArray(response);
                            totalQuestion.setText(String.valueOf(jsonArray.length()));
                            for(int v=0; v<= jsonArray.length()-1; v++){
                                JSONObject jsonObject = jsonArray.getJSONObject(v);
                                String id = jsonObject.getString("id");
                                String question = jsonObject.getString("question");
                                String category = jsonObject.getString("category");
                                String option1 = jsonObject.getString("option1");
                                String option2 = jsonObject.getString("option2");


                                arr_id.add(id);
                                arr_question_process.add(question);
                                arr_category_process.add(category);
                                arr_option1_process.add(option1);
                                arr_option2_process.add(option2);
                            }

                            myDialog.dismiss();
                            responses = new String[arr_id.size()];

                            loadViews(1,0);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            myDialog.dismiss();
                            Toast.makeText(getContext(), "There was a problem loading the checklist.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "There was a problem loading the checklist.", Toast.LENGTH_SHORT).show();

                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("category", "PROCESS");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);
        requestQueue.add(stringRequest);
        requestQueue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue.getCache().clear();
            }
        });




        //set the views based on what was gotten from the server
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = count+1;
                counter = counter+1;

                loadViews(count, counter);
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = count-1;
                counter = counter-1;

                loadViews(count, counter);
            }
        });


        return v;
    }

    public void loadViews(int count, int counter){

        radioGroupYesNo.clearCheck();

        if(count == arr_id.size()){
            next.setText("Submit");
        }

        if(count == 1){
            previous.setVisibility(View.GONE);
        }else{
            previous.setVisibility(View.VISIBLE);
        }

        presentQuestion.setText(String.valueOf(count));
        question.setText(arr_question_process.get(counter));

        //it is a Yes or No option
        //set the radiobutton text
        radioYes.setText(arr_option1_process.get(counter));
        radioNo.setText(arr_option2_process.get(counter));

        radioGroupYesNo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId){
                    case R.id.radioYes:
                        // do operations specific to this selection
//                            Toast.makeText(getContext(), radioYes.getText().toString(), Toast.LENGTH_SHORT).show();
                        responses[counter] = radioYes.getText().toString();
                        break;
                    case R.id.radioNo:
                        // do operations specific to this selection
//                            Toast.makeText(getContext(), radioNo.getText().toString(), Toast.LENGTH_SHORT).show();
                        responses[counter] = radioNo.getText().toString();
                        break;
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(count == arr_id.size()){
                    //send all captures to DB
                    //prompt and show users questions and answer, then send on confirmation
                    validateInput();
                }else{
                    loadViews(count+1, counter+1);
                }
            }
        });
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadViews(count-1, counter-1);
            }
        });
    }

    public void validateInput(){
        //show a prompt for the result
        //show Dialog
//        System.out.println("Array_Question = "+arr_question+"\nResponses Length = "+responses.length+"\nResponses = "+responses);
        ListViewAdapter listViewAdapter = new ListViewAdapter(getContext(), responses, arr_question_process);

        myDialog2 = new Dialog(getContext());
        myDialog2.setContentView(R.layout.custom_popup_validate);
        TextView nameofchecklist = myDialog2.findViewById(R.id.nameofchecklist);
        nameofchecklist.setText("Process Checklist");
        ListView listView = myDialog2.findViewById(R.id.list);
        listView.setAdapter(listViewAdapter);
        submission = myDialog2.findViewById(R.id.submit);
        submission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show Dialog that is loading the information
                myDialog = new Dialog(getContext());
                myDialog.setContentView(R.layout.custom_popup_loading);
                TextView text = myDialog.findViewById(R.id.text);
                text.setText("Submitting Response... Please wait");
                myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialog.setCanceledOnTouchOutside(false);
                myDialog.show();

                if(usertype.equals("admin")){
                    //send the results to the DB
                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, SUBMIT_PROCESS_STATE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        String notification = jsonObject.getString("notification");

                                        if (status.equals("successful")){
                                            //convert arrival check to integer
                                            convert = Integer.parseInt(process_check);
                                            //add 1 to the integer value of arrival check
                                            convert = convert + 1;
                                            //pass the string value to the sharedpreference
                                            SharedPreferences.Editor myEdit2 = preferences.edit();
                                            myEdit2.putString("process_check", String.valueOf(convert));
                                            myEdit2.commit();

                                            myDialog.dismiss();
                                            myDialog2.dismiss();
                                            Toast.makeText(getContext(), notification+" Process checklist", Toast.LENGTH_SHORT).show();
//                                            ((Dashboard)getActivity()).navigateFragment(0);
                                            //go back to fragment checklist
                                            Intent i = new Intent(getContext(), Dashboard.class);
                                            i.putExtra("from", "state_level");
                                            i.putExtra("state", got_state);
                                            startActivity(i);
                                        }else{
                                            myDialog.dismiss();
                                            myDialog2.dismiss();
                                            Toast.makeText(getContext(), "Sending Failed! please try again", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    catch (JSONException e){
                                        e.printStackTrace();
                                        myDialog.dismiss();
                                        myDialog2.dismiss();
                                        Toast.makeText(getContext(), "You have submitted a response before... You can not submit again", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getContext(), Dashboard.class);
                                        i.putExtra("from", "state_level");
                                        i.putExtra("state", got_state);
                                        startActivity(i);
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
                                    myDialog2.dismiss();
                                    Toast.makeText(getContext(), "Error! Please check network connectivity and try again", Toast.LENGTH_SHORT).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<>();
                            params.put("dstate", got_state);
                            params.put("user", got_email);
                            params.put("q1", responses[0]);
                            params.put("q2", responses[1]);
                            params.put("q3", responses[2]);
                            params.put("q4", responses[3]);
                            params.put("q5", responses[4]);
                            params.put("q6", responses[5]);
                            params.put("q7", responses[6]);
                            params.put("q8", responses[7]);
                            params.put("q9", responses[8]);
                            return params;
                        }
                    };

                    RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
                    DefaultRetryPolicy retryPolicy2 = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                    stringRequest2.setRetryPolicy(retryPolicy2);
                    requestQueue2.add(stringRequest2);
                    requestQueue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                        @Override
                        public void onRequestFinished(Request<Object> request) {
                            requestQueue2.getCache().clear();
                        }
                    });
                }else{
                    //send the results to the DB
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, SUBMIT_PROCESS,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        String notification = jsonObject.getString("notification");

                                        if (status.equals("successful")){
                                            //convert arrival check to integer
                                            convert = Integer.parseInt(process_check);
                                            //add 1 to the integer value of arrival check
                                            convert = convert + 1;
                                            //pass the string value to the sharedpreference
                                            SharedPreferences.Editor myEdit2 = preferences.edit();
                                            myEdit2.putString("process_check", String.valueOf(convert));
                                            myEdit2.commit();

                                            myDialog.dismiss();
                                            myDialog2.dismiss();
                                            Toast.makeText(getContext(), notification+" Process checklist", Toast.LENGTH_SHORT).show();
//                                            ((Dashboard)getActivity()).navigateFragment(0);
                                            //go back to fragment checklist
                                            Intent i = new Intent(getContext(), Dashboard.class);
                                            i.putExtra("from", "lga_level");
                                            i.putExtra("state", got_state);
                                            startActivity(i);
                                        }else{
                                            myDialog.dismiss();
                                            myDialog2.dismiss();
                                            Toast.makeText(getContext(), "Sending Failed! please try again", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                    catch (JSONException e){
                                        e.printStackTrace();
                                        myDialog.dismiss();
                                        myDialog2.dismiss();
                                        Toast.makeText(getContext(), "You have submitted a response before... You can not submit again", Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getContext(), Dashboard.class);
                                        i.putExtra("from", "lga_level");
                                        i.putExtra("state", got_state);
                                        startActivity(i);
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
                                    myDialog2.dismiss();
                                    Toast.makeText(getContext(), "Error! Please check network connectivity and try again", Toast.LENGTH_SHORT).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams(){
                            Map<String, String> params = new HashMap<>();
                            params.put("lga", got_lga);
                            params.put("dstate", got_state);
                            params.put("user", got_email);
                            params.put("q1", responses[0]);
                            params.put("q2", responses[1]);
                            params.put("q3", responses[2]);
                            params.put("q4", responses[3]);
                            params.put("q5", responses[4]);
                            params.put("q6", responses[5]);
                            params.put("q7", responses[6]);
                            params.put("q8", responses[7]);
                            params.put("q9", responses[8]);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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


                //clear the array
                arr_id.clear();
                arr_question_process.clear();
                arr_category_process.clear();
                arr_option1_process.clear();
                arr_option2_process.clear();

            }
        });
        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.setCanceledOnTouchOutside(true);
        myDialog2.show();
    }

}