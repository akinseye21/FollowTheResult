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
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.RelativeLayout;
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

public class FragmentArrivalChecklist1 extends Fragment {

    TextView fullname, state, lga;
    SharedPreferences preferences, preferences2;
    String usertype;
    LinearLayout lin_lga, lin_lgacount;

    Dialog myDialog, myDialog2;
    AppCompatButton next;
    public static final String GET_QUESTIONS = "https://readytoleadafrica.org/rtl_mobile/get_questions";
    public static final String SUBMIT_ARRIVAL_LGA = "https://readytoleadafrica.org/rtl_mobile/arrival_checks";
    public static final String SUBMIT_ARRIVAL_STATE = "https://readytoleadafrica.org/rtl_mobile/arrival_checks_state";

    ArrayList<String> arr_id;
    ArrayList<String> arr_question;
    ArrayList<String> arr_category;
    ArrayList<String> arr_option1;
    ArrayList<String> arr_option2;
    ArrayList<String> arr_option3;
    ArrayList<String> arr_option4;
    ArrayList<String> arr_option5;
    int count = 1;
    int counter = 0;
    int convert;

    TextView presentQuestion, totalQuestion;
    TextView question;
    RadioGroup radioGroup, radioGroupYesNo;
    RadioButton radio1, radio2, radio3, radio4, radio5, radioYes, radioNo;
    RelativeLayout relTime;
    EditText num1, num2, num3, num4;
    EditText textInfo;
    AppCompatButton previous;
    TextView lgaCount;
    String arrival_check, process_check, result_submit;

    LinearLayout popup;
    ListView listView;
    AppCompatButton submission;

    String[] responses;
    String got_fullname, got_state, got_lga, got_email;


    public FragmentArrivalChecklist1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_arrival_checklist1, container, false);

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
        radioGroup = v.findViewById(R.id.radiogroup);
        radioGroupYesNo = v.findViewById(R.id.radiogroupYesNo);
        radio1 = v.findViewById(R.id.radio1);
        radio2 = v.findViewById(R.id.radio2);
        radio3 = v.findViewById(R.id.radio3);
        radio4 = v.findViewById(R.id.radio4);
        radio5 = v.findViewById(R.id.radio5);
        radioYes = v.findViewById(R.id.radioYes);
        radioNo = v.findViewById(R.id.radioNo);
        relTime = v.findViewById(R.id.relTime);
        num1 = v.findViewById(R.id.num1);
        num2 = v.findViewById(R.id.num2);
        num3 = v.findViewById(R.id.num3);
        num4 = v.findViewById(R.id.num4);
        textInfo = v.findViewById(R.id.textInfo);
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
        arr_question = new ArrayList<>();
        arr_category = new ArrayList<>();
        arr_option1 = new ArrayList<>();
        arr_option2 = new ArrayList<>();
        arr_option3 = new ArrayList<>();
        arr_option4 = new ArrayList<>();
        arr_option5 = new ArrayList<>();

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
                                arr_question.add(question);
                                arr_category.add(category);
                                arr_option1.add(option1);
                                arr_option2.add(option2);
                                arr_option3.add(option3);
                                arr_option4.add(option4);
                                arr_option5.add(option5);
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
                params.put("category", "ARRIVAL");
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
        arr_question.clear();
        arr_category.clear();
        arr_option1.clear();
        arr_option2.clear();
        arr_option3.clear();
        arr_option4.clear();
        arr_option5.clear();


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

//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FragmentArrivalChecklist2 fragmentArrivalChecklist2 = new FragmentArrivalChecklist2();
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragmentChecklist1, fragmentArrivalChecklist2);
//                transaction.commit();
//            }
//        });

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
        question.setText(arr_question.get(counter));


        if (arr_option1.get(counter).equals("") && !arr_id.get(counter).equals("1")){
            //make the text info visible
            textInfo.setVisibility(View.VISIBLE);
            radioGroupYesNo.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            relTime.setVisibility(View.GONE);

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
        else if(!arr_option1.get(counter).equals("") && arr_option3.get(counter).equals("")){
            //it is a Yes or No option
            radioGroupYesNo.setVisibility(View.VISIBLE);
            textInfo.setVisibility(View.GONE);
            radioGroup.setVisibility(View.GONE);
            relTime.setVisibility(View.GONE);

            //set the radiobutton text
            radioYes.setText(arr_option1.get(counter));
            radioNo.setText(arr_option2.get(counter));

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


//            int selectedId = radioGroupYesNo.getCheckedRadioButtonId();
//            radioButton = getView().findViewById(selectedId);
//            Toast.makeText(getContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();

        }
        else if(!arr_option5.get(counter).equals("")){
            radioGroup.setVisibility(View.VISIBLE);
            radioGroupYesNo.setVisibility(View.GONE);
            textInfo.setVisibility(View.GONE);
            relTime.setVisibility(View.GONE);

            //set the radiobutton text
            radio1.setText(arr_option1.get(counter));
            radio2.setText(arr_option2.get(counter));
            radio3.setText(arr_option3.get(counter));
            radio4.setText(arr_option4.get(counter));
            radio5.setText(arr_option5.get(counter));

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup1, int checkedId) {
                    switch(checkedId){
                        case R.id.radio1:
                            // do operations specific to this selection
//                            Toast.makeText(getContext(), radio1.getText().toString(), Toast.LENGTH_SHORT).show();
                            responses[counter] = radio1.getText().toString();
                            break;
                        case R.id.radio2:
                            // do operations specific to this selection
//                            Toast.makeText(getContext(), radio2.getText().toString(), Toast.LENGTH_SHORT).show();
                            responses[counter] = radio2.getText().toString();
                            break;
                        case R.id.radio3:
                            // do operations specific to this selection
//                            Toast.makeText(getContext(), radio3.getText().toString(), Toast.LENGTH_SHORT).show();
                            responses[counter] = radio3.getText().toString();
                            break;
                        case R.id.radio4:
                            // do operations specific to this selection
//                            Toast.makeText(getContext(), radio4.getText().toString(), Toast.LENGTH_SHORT).show();
                            responses[counter] = radio4.getText().toString();
                            break;
                        case R.id.radio5:
                            // do operations specific to this selection
//                            Toast.makeText(getContext(), radio5.getText().toString(), Toast.LENGTH_SHORT).show();
                            responses[counter] = radio5.getText().toString();
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
        if(arr_id.get(counter).equals("1")){
            relTime.setVisibility(View.VISIBLE);
            radioGroup.setVisibility(View.GONE);
            radioGroupYesNo.setVisibility(View.GONE);
            textInfo.setVisibility(View.GONE);

            //carry out the edittext one whole movement here
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

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(num1.getText().toString().equals("") || num2.getText().toString().equals("") || num3.getText().toString().equals("") || num4.getText().toString().equals("")){
                        Toast.makeText(getContext(), "Time fields can not be empty", Toast.LENGTH_SHORT).show();
                    }else{
                        if(count == arr_id.size()){
                            responses[counter] = num1.getText().toString()+num2.getText().toString()+":"+num3.getText().toString()+num4.getText().toString()+"PM";
                            //send all captures to DB
//                            Toast.makeText(getContext(), "List = "+responses[0]+responses[1]+responses[2]+responses[3]+responses[4]+responses[5]+responses[6]+responses[7], Toast.LENGTH_LONG).show();
//                            System.out.println("List = "+responses[0]+responses[1]+responses[2]+responses[3]+responses[4]+responses[5]+responses[6]+responses[7]);

                            //prompt and show users questions and answer, then send on confirmation
                            validateInput();
                        }else{
                            //get input in each
                            responses[counter] = num1.getText().toString()+num2.getText().toString()+":"+num3.getText().toString()+num4.getText().toString()+"PM";
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
    }

    public void validateInput(){
        //show a prompt for the result
        //show Dialog
//        System.out.println("Array_Question = "+arr_question+"\nResponses Length = "+responses.length+"\nResponses = "+responses);
        ListViewAdapter listViewAdapter = new ListViewAdapter(getContext(), responses, arr_question);

        myDialog2 = new Dialog(getContext());
        myDialog2.setContentView(R.layout.custom_popup_validate);
        TextView nameofchecklist = myDialog2.findViewById(R.id.nameofchecklist);
        nameofchecklist.setText("Arrival Checklist");
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

                if (usertype.equals("admin")){
                    //send the result to the database using the API for state level
                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, SUBMIT_ARRIVAL_STATE,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        String notification = jsonObject.getString("notification");

                                        if (status.equals("successful")){
                                            //convert arrival check to integer
                                            convert = Integer.parseInt(arrival_check);
                                            //add 1 to the integer value of arrival check
                                            convert = convert + 1;
                                            //pass the string value to the sharedpreference
                                            SharedPreferences.Editor myEdit2 = preferences.edit();
                                            myEdit2.putString("arrival_check", String.valueOf(convert));
                                            myEdit2.commit();

                                            myDialog.dismiss();
                                            myDialog2.dismiss();
                                            Toast.makeText(getContext(), notification+" Arrival checklist", Toast.LENGTH_SHORT).show();
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
//                                        ((Dashboard)getActivity()).navigateFragment(0);
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
                            params.put("observer_arrival", responses[0]);
                            params.put("collation_arrival", responses[1]);
                            params.put("obs_permit", responses[2]);
                            params.put("sum_collation_officer", responses[3]);
                            params.put("female_collation_officer", responses[4]);
                            params.put("security", responses[5]);
                            params.put("party_agents", responses[6]);
                            params.put("pwd_access", responses[7]);
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
                    //send the results to the DB using the API for LGA level
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, SUBMIT_ARRIVAL_LGA,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try{
                                        JSONObject jsonObject = new JSONObject(response);
                                        String status = jsonObject.getString("status");
                                        String notification = jsonObject.getString("notification");

                                        if (status.equals("successful")){
                                            //convert arrival check to integer
                                            convert = Integer.parseInt(arrival_check);
                                            //add 1 to the integer value of arrival check
                                            convert = convert + 1;
                                            //pass the string value to the sharedpreference
                                            SharedPreferences.Editor myEdit2 = preferences.edit();
                                            myEdit2.putString("arrival_check", String.valueOf(convert));
                                            myEdit2.commit();

                                            myDialog.dismiss();
                                            myDialog2.dismiss();
                                            Toast.makeText(getContext(), notification+" Arrival checklist", Toast.LENGTH_SHORT).show();
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
//                                        ((Dashboard)getActivity()).navigateFragment(0);
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
                            params.put("observer_arrival", responses[0]);
                            params.put("collation_arrival", responses[1]);
                            params.put("obs_permit", responses[2]);
                            params.put("sum_collation_officer", responses[3]);
                            params.put("female_collation_officer", responses[4]);
                            params.put("security", responses[5]);
                            params.put("party_agents", responses[6]);
                            params.put("pwd_access", responses[7]);
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
                arr_question.clear();
                arr_category.clear();
                arr_option1.clear();
                arr_option2.clear();
                arr_option3.clear();
                arr_option4.clear();
                arr_option5.clear();
            }
        });
        myDialog2.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog2.setCanceledOnTouchOutside(true);
        myDialog2.show();


    }
}