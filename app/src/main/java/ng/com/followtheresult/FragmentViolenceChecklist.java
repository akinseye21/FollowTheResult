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

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

public class FragmentViolenceChecklist extends Fragment {

    TextView fullname, state, lga;


    Dialog myDialog, myDialog2;
    AppCompatButton next;
    EditText textInfo;

    public static final String SUBMIT_INCIDENCE = "https://readytoleadafrica.org/rtl_mobile/incidence";
    public static final String GET_QUESTIONS = "https://readytoleadafrica.org/rtl_mobile/get_questions";

    ArrayList<String> arr_id = new ArrayList<>();
    ArrayList<String> arr_question_incidence = new ArrayList<>();
    ArrayList<String> arr_option1_incidence = new ArrayList<>();
    ArrayList<String> arr_option2_incidence = new ArrayList<>();
    int count = 1;
    int counter = 0;
    int convert;

    TextView presentQuestion, totalQuestion;
    TextView question;
    RadioGroup radioGroupYesNo;
    RadioButton radioYes, radioNo;

    AppCompatButton previous;
    AppCompatButton submission;

    String[] responses = new String[7];
    String arrival_check, process_check, result_submit;


    SharedPreferences preferences, preferences2;
    String got_fullname;
    String got_state;
    String got_lga;
    String got_email;
    LinearLayout lin_lga, lin_lgacount;
    TextView lgaCount;
    String usertype;


    public FragmentViolenceChecklist() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_violence_checklist, container, false);

//        arr_question_incidence.add("Was there any incident of election-related violence upon your arrival at your LGA Collation Center?");
//        arr_question_incidence.add("Was there any incident of election-related violence during the process of collation of results at your LGA\n" +
//                "Collation Center?");
//        arr_question_incidence.add("Was there any incident of election-related violence following the announcement of the results at your LGA\n" +
//                "Collation Center?");
//        arr_question_incidence.add("How long did this incident of violence last?");
//        arr_question_incidence.add("Did the violence result in severe injury, loss of life or destruction of electoral materials?");
//        arr_question_incidence.add("Did the violence result in the interruption of the collation or announcement of results?");
//        arr_question_incidence.add("What was the response of security officials at the LGA Collation Center to the violence?");
//
//        arr_option1_process.add("Yes");
//        arr_option1_process.add("Yes");
//        arr_option1_process.add("Yes");
//        arr_option1_process.add("");
//        arr_option1_process.add("Yes");
//        arr_option1_process.add("Yes");
//        arr_option1_process.add("");
//
//        arr_option2_process.add("No");
//        arr_option2_process.add("No");
//        arr_option2_process.add("No");
//        arr_option2_process.add("");
//        arr_option2_process.add("No");
//        arr_option2_process.add("No");
//        arr_option2_process.add("");

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
        textInfo = v.findViewById(R.id.textInfo);

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


        //show Dialog that is loading the information
        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Loading Checklist. Please wait");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

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
                                String option3 = jsonObject.getString("option3");
                                String option4 = jsonObject.getString("option4");
                                String option5 = jsonObject.getString("option5");


                                arr_id.add(id);
                                arr_question_incidence.add(question);
                                arr_option1_incidence.add(option1);
                                arr_option2_incidence.add(option2);
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
                params.put("category", "INCIDENCE");
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

        arr_id.clear();
        arr_question_incidence.clear();
        arr_option1_incidence.clear();
        arr_option2_incidence.clear();



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

        textInfo.setText("");
        radioGroupYesNo.clearCheck();

        if(count == arr_id.size()){
            next.setText("Submit");
            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //send all captures to DB
//                    Toast.makeText(getContext(), "List = "+responses[0]+responses[1]+responses[2]+responses[3]+responses[4]+responses[5]+responses[6]+responses[7], Toast.LENGTH_LONG).show();
                    System.out.println("List = "+responses[0]+responses[1]+responses[2]+responses[3]+responses[4]+responses[5]+responses[6]+responses[7]);
                }
            });
        }

        if(count == 1){
            previous.setVisibility(View.GONE);
        }else{
            previous.setVisibility(View.VISIBLE);
        }

        presentQuestion.setText(String.valueOf(count));
        question.setText(arr_question_incidence.get(counter));


        if (arr_option1_incidence.get(counter).equals("") && !arr_id.get(counter).equals("1")){
            //make the text info visible
            textInfo.setVisibility(View.VISIBLE);
            radioGroupYesNo.setVisibility(View.GONE);
//            radioGroup.setVisibility(View.GONE);
//            relTime.setVisibility(View.GONE);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(textInfo.getText().toString().equals("")){
                        Toast.makeText(getContext(), "Field can not be empty", Toast.LENGTH_SHORT).show();
                    }else{
                        if(count == arr_id.size()){
                            responses[counter] = textInfo.getText().toString();

                            //send all captures to DB
//                            Toast.makeText(getContext(), "List = "+responses[0]+responses[1]+responses[2]+responses[3]+responses[4]+responses[5]+responses[6]+responses[7], Toast.LENGTH_LONG).show();
//                            System.out.println("List = "+responses[0]+responses[1]+responses[2]+responses[3]+responses[4]+responses[5]+responses[6]+responses[7]);

                            //prompt and show users questions and answer, then send on confirmation
                            validateInput();
                        }else{
                            responses[counter] = textInfo.getText().toString();
                            loadViews(count+1, counter+1);
                        }
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
        else{
            //it is a Yes or No option
            radioGroupYesNo.setVisibility(View.VISIBLE);
            textInfo.setVisibility(View.GONE);

            //set the radiobutton text
            radioYes.setText(arr_option1_incidence.get(counter));
            radioNo.setText(arr_option2_incidence.get(counter));

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
//                        Toast.makeText(getContext(), "List = "+responses[0]+responses[1]+responses[2]+responses[3]+responses[4]+responses[5]+responses[6]+responses[7], Toast.LENGTH_LONG).show();
//                        System.out.println("List = "+responses[0]+responses[1]+responses[2]+responses[3]+responses[4]+responses[5]+responses[6]+responses[7]);

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
    }

    public void validateInput(){
        //show a prompt for the result
        //show Dialog
//        System.out.println("Array_Question = "+arr_question+"\nResponses Length = "+responses.length+"\nResponses = "+responses);
        ListViewAdapter listViewAdapter = new ListViewAdapter(getContext(), responses, arr_question_incidence);

        myDialog2 = new Dialog(getContext());
        myDialog2.setContentView(R.layout.custom_popup_validate);
        TextView nameofchecklist = myDialog2.findViewById(R.id.nameofchecklist);
        nameofchecklist.setText("Incidence Checklist");
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

                //send the results to the DB
                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, SUBMIT_INCIDENCE,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    String status = jsonObject.getString("status");
                                    String notification = jsonObject.getString("notification");

                                    if (status.equals("successful")){

                                        myDialog.dismiss();
                                        myDialog2.dismiss();
                                        Toast.makeText(getContext(), notification+" Incidence checklist", Toast.LENGTH_SHORT).show();
//                                            ((Dashboard)getActivity()).navigateFragment(0);
                                        if(usertype.equals("admin")){
                                            //go back to fragment checklist
                                            Intent i = new Intent(getContext(), Dashboard.class);
                                            i.putExtra("from", "state_level");
                                            i.putExtra("state", got_state);
                                            startActivity(i);
                                        }else{
                                            //go back to fragment checklist
                                            Intent i = new Intent(getContext(), Dashboard.class);
                                            i.putExtra("from", "lga_level");
                                            i.putExtra("state", got_state);
                                            startActivity(i);
                                        }
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
                                    Toast.makeText(getContext(), "There was an error somewhere, please try again.", Toast.LENGTH_LONG).show();
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


                //clear the array
                arr_question_incidence.clear();
                arr_option1_incidence.clear();
                arr_option2_incidence.clear();

            }
        });
        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.setCanceledOnTouchOutside(true);
        myDialog2.show();
    }
}