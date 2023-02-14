package ng.com.followtheresult;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class FragmentResultState extends Fragment {

    TextView fullname, state, lga, lgaName;
    SharedPreferences preferences, preferences2;
    String usertype;
    LinearLayout lin_lga, lin_lgacount;
    Dialog myDialog;
    ArrayList<String> party_name = new ArrayList<>();
    ArrayList<String> party_logo = new ArrayList<>();
    TextView lgaCount;

    CircleImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12, img13, img14, img15, img16, img17, img18;
    TextView pname1, pname2, pname3, pname4, pname5, pname6, pname7, pname8, pname9, pname10, pname11, pname12, pname13, pname14, pname15, pname16, pname17, pname18;
    EditText count1, count2, count3, count4, count5, count6, count7, count8, count9;
    EditText count10, count11, count12, count13, count14, count15, count16, count17, count18;
    String count_A, count_AA, count_AAC, count_ADC, count_ADP;
    String count_APC, count_APGA, count_APM, count_APP, count_BP;
    String count_LP, count_NNPP, count_NRM, count_PDP, count_PRP;
    String count_SDP, count_YPP, count_ZLP;

    public static final String GET_PARTY = "https://readytoleadafrica.org/rtl_mobile/getallparties";
    public static final String GET_ALL_LGA_RESULT = "https://readytoleadafrica.org/rtl_mobile/getlgaresults";

    public FragmentResultState() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result_state, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final String got_fullname = preferences.getString("fullname", "not available");
        final String got_state = preferences.getString("state", "not available");
        final String got_lga = preferences.getString("lga", "not available");
        usertype = preferences.getString("usertype", "");

        preferences2 = getActivity().getSharedPreferences("lga_count", Context.MODE_PRIVATE);
        final String lga_count = preferences2.getString("lgacount", "1");

        fullname = v.findViewById(R.id.fullname);
        state = v.findViewById(R.id.state);
        lga = v.findViewById(R.id.lga);
        lin_lga = v.findViewById(R.id.lin_lga);
        lin_lgacount = v.findViewById(R.id.lin_lgacount);
        lgaName = v.findViewById(R.id.lgaName);
        lgaCount = v.findViewById(R.id.lga_count);

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



        count1 = v.findViewById(R.id.count1);
        count2 = v.findViewById(R.id.count2);
        count3 = v.findViewById(R.id.count3);
        count4 = v.findViewById(R.id.count4);
        count5 = v.findViewById(R.id.count5);
        count6 = v.findViewById(R.id.count6);
        count7 = v.findViewById(R.id.count7);
        count8 = v.findViewById(R.id.count8);
        count9 = v.findViewById(R.id.count9);
        count10 = v.findViewById(R.id.count10);
        count11 = v.findViewById(R.id.count11);
        count12 = v.findViewById(R.id.count12);
        count13 = v.findViewById(R.id.count13);
        count14 = v.findViewById(R.id.count14);
        count15 = v.findViewById(R.id.count15);
        count16 = v.findViewById(R.id.count16);
        count17 = v.findViewById(R.id.count17);
        count18 = v.findViewById(R.id.count18);

        fullname.setText(got_fullname);
        state.setText(got_state);
        lga.setText(got_lga);
        lgaCount.setText(lga_count);

        if(usertype.equals("admin")){
            lin_lga.setVisibility(View.GONE);
        }else{
            lin_lgacount.setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        //show Dialog that is loading the information
        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Loading LGA results... Please wait");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        Bundle args = getArguments();
        if (args != null) {

            lgaName.setText(args.getString("lga")+" RESULT VERIFICATION");
            //get party
            StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_PARTY,
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
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                            if(volleyError == null){
                                return;
                            }
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




            //fetch lga result in the state
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, GET_ALL_LGA_RESULT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try{
                                JSONArray jsonArray = new JSONArray(response);
                                myDialog.dismiss();

                                for(int w=0; w<=jsonArray.length()-1; w++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(w);
                                    String my_lga = jsonObject.getString("lga");

                                    if(my_lga.equals(args.getString("lga"))){
                                        count_A = jsonObject.getString("A");
                                        count_AA = jsonObject.getString("AA");
                                        count_AAC = jsonObject.getString("AAC");
                                        count_ADC = jsonObject.getString("ADC");
                                        count_ADP = jsonObject.getString("ADP");
                                        count_APC = jsonObject.getString("APC");
                                        count_APGA = jsonObject.getString("APGA");
                                        count_APM = jsonObject.getString("APM");
                                        count_APP = jsonObject.getString("APP");
                                        count_BP = jsonObject.getString("BP");
                                        count_LP = jsonObject.getString("LP");
                                        count_NNPP = jsonObject.getString("NNPP");
                                        count_NRM = jsonObject.getString("NRM");
                                        count_PDP = jsonObject.getString("PDP");
                                        count_PRP = jsonObject.getString("PRP");
                                        count_SDP = jsonObject.getString("SDP");
                                        count_YPP = jsonObject.getString("YPP");
                                        count_ZLP = jsonObject.getString("ZLP");

                                        count1.setText(count_A);
                                        count2.setText(count_AA);
                                        count3.setText(count_AAC);
                                        count4.setText(count_ADC);
                                        count5.setText(count_ADP);
                                        count6.setText(count_APC);
                                        count7.setText(count_APGA);
                                        count8.setText(count_APM);
                                        count9.setText(count_APP);
                                        count10.setText(count_BP);
                                        count11.setText(count_LP);
                                        count12.setText(count_NNPP);
                                        count13.setText(count_NRM);
                                        count14.setText(count_PDP);
                                        count15.setText(count_PRP);
                                        count16.setText(count_SDP);
                                        count17.setText(count_YPP);
                                        count18.setText(count_ZLP);

                                    }
                                }

                            }
                            catch (JSONException e){
                                e.printStackTrace();
                                Toast.makeText(getContext(), "Error Loading LGAs. Please try again", Toast.LENGTH_SHORT).show();
                                myDialog.dismiss();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                            if(volleyError == null){
                                return;
                            }
                            Toast.makeText(getContext(), "Error in connection. Check network connectivity", Toast.LENGTH_SHORT).show();
                            myDialog.dismiss();
                        }
                    }){
                @Override
                protected Map<String, String> getParams(){
                    Map<String, String> params = new HashMap<>();
                    params.put("dstate", args.getString("state"));
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