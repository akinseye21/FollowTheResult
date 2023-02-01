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
    SharedPreferences preferences;

    Dialog myDialog, myDialog2;
    AppCompatButton next;
    public static final String GET_QUESTIONS = "https://readytoleadafrica.org/rtl_mobile/get_questions";

    ArrayList<String> arr_id;
    ArrayList<String> arr_question_process;
    ArrayList<String> arr_category_process;
    ArrayList<String> arr_option1_process;
    ArrayList<String> arr_option2_process;
    int count = 1;
    int counter = 0;

    TextView presentQuestion, totalQuestion;
    TextView question;
    RadioGroup radioGroupYesNo;
    RadioButton radioYes, radioNo;

    AppCompatButton previous;
    AppCompatButton submission;

    String[] responses;

    public FragmentProcessChecklist() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_process_checklist, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final String got_fullname = preferences.getString("fullname", "not available");
        final String got_state = preferences.getString("state", "not available");
        final String got_lga = preferences.getString("lga", "not available");

        fullname = v.findViewById(R.id.fullname);
        state = v.findViewById(R.id.state);
        lga = v.findViewById(R.id.lga);

        fullname.setText(got_fullname);
        state.setText(got_state);
        lga.setText(got_lga);

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
                //send the results to the DB


                //go back to fragment checklist
                Intent i = new Intent(getContext(), Dashboard.class);
                startActivity(i);

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