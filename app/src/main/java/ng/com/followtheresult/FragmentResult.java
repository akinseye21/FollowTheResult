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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentResult extends Fragment{

    TextView fullname, state, lga;

    Dialog myDialog;
    ArrayList<String> party_name = new ArrayList<>();
    ArrayList<String> party_logo = new ArrayList<>();

    SharedPreferences preferences, preferences2;
    String got_fullname;
    String got_state;
    String got_lga;
    String got_email;
    String usertype;
    LinearLayout lin_lga, lin_lgacount;
    TextView lgaCount;


    CircleImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12, img13, img14, img15, img16, img17, img18;
    TextView pname1, pname2, pname3, pname4, pname5, pname6, pname7, pname8, pname9, pname10, pname11, pname12, pname13, pname14, pname15, pname16, pname17, pname18;
    EditText num1, num2, num3, num4, num5, num6, num7, num8, num9, num10, num11, num12, num13, num14, num15, num16, num17, num18;
    String input1, input2, input3, input4, input5, input6, input7, input8, input9, input10, input11, input12, input13, input14, input15, input16, input17, input18;

    AppCompatButton submit;

    public static final String GET_PARTY = "https://readytoleadafrica.org/rtl_mobile/getallparties";
    public static final String SUBMIT_RESULT = "https://readytoleadafrica.org/rtl_mobile/submit_results";
    public static final String SUBMIT_RESULT_STATE = "https://readytoleadafrica.org/rtl_mobile/submit_state_results";

    public FragmentResult() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        got_fullname = preferences.getString("fullname", "not available");
        got_state = preferences.getString("state", "not available");
        got_lga = preferences.getString("lga", "not available");
        got_email = preferences.getString("email", "not available");
        usertype = preferences.getString("usertype", "");

        preferences2 = getActivity().getSharedPreferences("lga_count", Context.MODE_PRIVATE);
        final String lga_count = preferences2.getString("lgacount", "1");

        fullname = v.findViewById(R.id.fullname);
        state = v.findViewById(R.id.state);
        lga = v.findViewById(R.id.lga);
        lin_lga = v.findViewById(R.id.lin_lga);
        lin_lgacount = v.findViewById(R.id.lin_lgacount);
        submit = v.findViewById(R.id.submit);
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

        img1 = v.findViewById(R.id.image1);
        img2 = v.findViewById(R.id.image2);
        img3 = v.findViewById(R.id.image3);
        img4 = v.findViewById(R.id.image4);
        img5 = v.findViewById(R.id.image5);
        img6 = v.findViewById(R.id.image6);
        img7 = v.findViewById(R.id.image7);
        img8 = v.findViewById(R.id.image8);
        img9 = v.findViewById(R.id.image9);
        img10 = v.findViewById(R.id.image10);
        img11 = v.findViewById(R.id.image11);
        img12 = v.findViewById(R.id.image12);
        img13 = v.findViewById(R.id.image13);
        img14 = v.findViewById(R.id.image14);
        img15 = v.findViewById(R.id.image15);
        img16 = v.findViewById(R.id.image16);
        img17 = v.findViewById(R.id.image17);
        img18 = v.findViewById(R.id.image18);

        pname1 = v.findViewById(R.id.party_name1);
        pname2 = v.findViewById(R.id.party_name2);
        pname3 = v.findViewById(R.id.party_name3);
        pname4 = v.findViewById(R.id.party_name4);
        pname5 = v.findViewById(R.id.party_name5);
        pname6 = v.findViewById(R.id.party_name6);
        pname7 = v.findViewById(R.id.party_name7);
        pname8 = v.findViewById(R.id.party_name8);
        pname9 = v.findViewById(R.id.party_name9);
        pname10 = v.findViewById(R.id.party_name10);
        pname11 = v.findViewById(R.id.party_name11);
        pname12 = v.findViewById(R.id.party_name12);
        pname13 = v.findViewById(R.id.party_name13);
        pname14 = v.findViewById(R.id.party_name14);
        pname15 = v.findViewById(R.id.party_name15);
        pname16 = v.findViewById(R.id.party_name16);
        pname17 = v.findViewById(R.id.party_name17);
        pname18 = v.findViewById(R.id.party_name18);

        num1 = v.findViewById(R.id.edtnum1);
        num2 = v.findViewById(R.id.edtnum2);
        num3 = v.findViewById(R.id.edtnum3);
        num4 = v.findViewById(R.id.edtnum4);
        num5 = v.findViewById(R.id.edtnum5);
        num6 = v.findViewById(R.id.edtnum6);
        num7 = v.findViewById(R.id.edtnum7);
        num8 = v.findViewById(R.id.edtnum8);
        num9 = v.findViewById(R.id.edtnum9);
        num10 = v.findViewById(R.id.edtnum10);
        num11 = v.findViewById(R.id.edtnum11);
        num12 = v.findViewById(R.id.edtnum12);
        num13 = v.findViewById(R.id.edtnum13);
        num14 = v.findViewById(R.id.edtnum14);
        num15 = v.findViewById(R.id.edtnum15);
        num16 = v.findViewById(R.id.edtnum16);
        num17 = v.findViewById(R.id.edtnum17);
        num18 = v.findViewById(R.id.edtnum18);


        //show Dialog that is loading the information
        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Loading Political Parties, Please wait");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_PARTY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for(int w=0; w<=jsonArray.length()-1; w++){
                                JSONObject jsonObject = jsonArray.getJSONObject(w);
                                String name = jsonObject.getString("accronym");
                                String logo = jsonObject.getString("logo");

                                party_name.add(name);
                                party_logo.add(logo);
                            }
                            myDialog.dismiss();

                            pname1.setText("Votes for party "+party_name.get(0));
                            pname2.setText("Votes for party "+party_name.get(1));
                            pname3.setText("Votes for party "+party_name.get(2));
                            pname4.setText("Votes for party "+party_name.get(3));
                            pname5.setText("Votes for party "+party_name.get(4));
                            pname6.setText("Votes for party "+party_name.get(5));
                            pname7.setText("Votes for party "+party_name.get(6));
                            pname8.setText("Votes for party "+party_name.get(7));
                            pname9.setText("Votes for party "+party_name.get(8));
                            pname10.setText("Votes for party "+party_name.get(9));
                            pname11.setText("Votes for party "+party_name.get(10));
                            pname12.setText("Votes for party "+party_name.get(11));
                            pname13.setText("Votes for party "+party_name.get(12));
                            pname14.setText("Votes for party "+party_name.get(13));
                            pname15.setText("Votes for party "+party_name.get(14));
                            pname16.setText("Votes for party "+party_name.get(15));
                            pname17.setText("Votes for party "+party_name.get(16));
                            pname18.setText("Votes for party "+party_name.get(17));

                            Glide.with(getContext()).load(party_logo.get(0)).into(img1);
                            Glide.with(getContext()).load(party_logo.get(1)).into(img2);
                            Glide.with(getContext()).load(party_logo.get(2)).into(img3);
                            Glide.with(getContext()).load(party_logo.get(3)).into(img4);
                            Glide.with(getContext()).load(party_logo.get(4)).into(img5);
                            Glide.with(getContext()).load(party_logo.get(5)).into(img6);
                            Glide.with(getContext()).load(party_logo.get(6)).into(img7);
                            Glide.with(getContext()).load(party_logo.get(7)).into(img8);
                            Glide.with(getContext()).load(party_logo.get(8)).into(img9);
                            Glide.with(getContext()).load(party_logo.get(9)).into(img10);
                            Glide.with(getContext()).load(party_logo.get(10)).into(img11);
                            Glide.with(getContext()).load(party_logo.get(11)).into(img12);
                            Glide.with(getContext()).load(party_logo.get(12)).into(img13);
                            Glide.with(getContext()).load(party_logo.get(13)).into(img14);
                            Glide.with(getContext()).load(party_logo.get(14)).into(img15);
                            Glide.with(getContext()).load(party_logo.get(15)).into(img16);
                            Glide.with(getContext()).load(party_logo.get(16)).into(img17);
                            Glide.with(getContext()).load(party_logo.get(17)).into(img18);

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                            myDialog.dismiss();
                            Toast.makeText(getContext(), "Error loading party information. Please try again.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getContext(), "Error! Please check internet connectivity and try again", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check for no input
                input1 = num1.getText().toString().trim();
                input2 = num2.getText().toString().trim();
                input3 = num3.getText().toString().trim();
                input4 = num4.getText().toString().trim();
                input5 = num5.getText().toString().trim();
                input6 = num6.getText().toString().trim();
                input7 = num7.getText().toString().trim();
                input8 = num8.getText().toString().trim();
                input9 = num9.getText().toString().trim();
                input10 = num10.getText().toString().trim();
                input11 = num11.getText().toString().trim();
                input12 = num12.getText().toString().trim();
                input13 = num13.getText().toString().trim();
                input14 = num14.getText().toString().trim();
                input15 = num15.getText().toString().trim();
                input16 = num16.getText().toString().trim();
                input17 = num17.getText().toString().trim();
                input18 = num18.getText().toString().trim();

                if(input1.equals("") || input2.equals("") || input3.equals("") || input4.equals("") || input5.equals("") ||
                        input6.equals("") || input7.equals("") || input8.equals("") || input9.equals("") || input10.equals("") ||
                        input11.equals("") || input12.equals("") || input13.equals("") || input14.equals("") || input15.equals("") ||
                        input16.equals("") || input17.equals("") || input18.equals("")){

                    Toast.makeText(getContext(), "One or more fields are empty. All fields must be filled", Toast.LENGTH_SHORT).show();

                }else{

                    //show Dialog that is loading the information
                    myDialog = new Dialog(getContext());
                    myDialog.setContentView(R.layout.custom_popup_loading);
                    TextView text = myDialog.findViewById(R.id.text);
                    text.setText("Sending Result, Please wait");
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.show();

                    if (usertype.equals("admin")){
                        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, SUBMIT_RESULT_STATE,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("success")){
                                            myDialog.dismiss();
                                            Toast.makeText(getContext(), "Results saved successfully", Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(getContext(), Dashboard.class);
                                            startActivity(i);
                                        }else{
                                            myDialog.dismiss();
                                            Toast.makeText(getContext(), "Saving failed, please try again", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getContext(), "Error! Please check internet connectivity and try again", Toast.LENGTH_SHORT).show();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams(){
                                Map<String, String> params = new HashMap<>();
                                params.put("dstate", got_state);
                                params.put("user", got_email);
                                params.put("a", input1);
                                params.put("aa", input2);
                                params.put("aac", input3);
                                params.put("adc", input4);
                                params.put("adp", input5);
                                params.put("apc", input6);
                                params.put("apga", input7);
                                params.put("apm", input8);
                                params.put("app", input9);
                                params.put("bp", input10);
                                params.put("lp", input11);
                                params.put("nnpp", input12);
                                params.put("nrm", input13);
                                params.put("pdp", input14);
                                params.put("prp", input15);
                                params.put("sdp", input16);
                                params.put("ypp", input17);
                                params.put("zlp", input18);
                                return params;
                            }
                        };

                        RequestQueue requestQueue1 = Volley.newRequestQueue(getContext());
                        DefaultRetryPolicy retryPolicy1 = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                        stringRequest1.setRetryPolicy(retryPolicy1);
                        requestQueue1.add(stringRequest1);
                        requestQueue1.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                            @Override
                            public void onRequestFinished(Request<Object> request) {
                                requestQueue1.getCache().clear();
                            }
                        });
                    }
                    else{
                        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, SUBMIT_RESULT,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        if (response.equals("success")){
                                            myDialog.dismiss();
                                            Toast.makeText(getContext(), "Results saved successfully", Toast.LENGTH_SHORT).show();

                                            Intent i = new Intent(getContext(), Dashboard.class);
                                            startActivity(i);
                                        }else{
                                            myDialog.dismiss();
                                            Toast.makeText(getContext(), "Saving failed, please try again", Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(getContext(), "Error! Please check internet connectivity and try again", Toast.LENGTH_SHORT).show();
                                    }
                                }){
                            @Override
                            protected Map<String, String> getParams(){
                                Map<String, String> params = new HashMap<>();
                                params.put("lga", got_lga);
                                params.put("dstate", got_state);
                                params.put("user", got_email);
                                params.put("a", input1);
                                params.put("aa", input2);
                                params.put("aac", input3);
                                params.put("adc", input4);
                                params.put("adp", input5);
                                params.put("apc", input6);
                                params.put("apga", input7);
                                params.put("apm", input8);
                                params.put("app", input9);
                                params.put("bp", input10);
                                params.put("lp", input11);
                                params.put("nnpp", input12);
                                params.put("nrm", input13);
                                params.put("pdp", input14);
                                params.put("prp", input15);
                                params.put("sdp", input16);
                                params.put("ypp", input17);
                                params.put("zlp", input18);
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
                    }

                }


            }
        });

        return v;
    }

}