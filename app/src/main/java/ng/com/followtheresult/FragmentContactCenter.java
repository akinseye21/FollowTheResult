package ng.com.followtheresult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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


public class FragmentContactCenter extends Fragment {

    TextView fullname, state, lga;
    SharedPreferences preferences, preferences2;
    LinearLayout lin_lga, lin_lgacount;
    TextView lgaCount;
    TextView phone1, phone2, phone3;
    String got_state;

    ArrayList<String> arr_phone1 = new ArrayList<>();
    ArrayList<String> arr_phone2 = new ArrayList<>();
    ArrayList<String> arr_phone3 = new ArrayList<>();

    public static final String GET_NUM = "https://readytoleadafrica.org/rtl_mobile/get_call_center";

    public FragmentContactCenter() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_contact_center, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final String got_fullname = preferences.getString("fullname", "not available");
        got_state = preferences.getString("state", "not available");
        final String got_lga = preferences.getString("lga", "not available");
        final String usertype = preferences.getString("usertype", "");

        preferences2 = getActivity().getSharedPreferences("lga_count", Context.MODE_PRIVATE);
        final String lga_count = preferences2.getString("lgacount", "");

        fullname = v.findViewById(R.id.fullname);
        state = v.findViewById(R.id.state);
        lga = v.findViewById(R.id.lga);
        lin_lga = v.findViewById(R.id.lin_lga);
        lin_lgacount = v.findViewById(R.id.lin_lgacount);
        lgaCount = v.findViewById(R.id.lga_count);
        phone1 = v.findViewById(R.id.phone_1);
        phone2 = v.findViewById(R.id.phone_2);
        phone3 = v.findViewById(R.id.phone_3);

        fullname.setText(got_fullname);
        state.setText(got_state);
        lga.setText(got_lga);
        lgaCount.setText(lga_count);

        if(usertype.equals("admin")){
            lin_lga.setVisibility(View.GONE);
        }else{
            lin_lgacount.setVisibility(View.GONE);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_NUM,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            for(int w=0; w<=jsonArray.length()-1; w++){
                                JSONObject jsonObject = jsonArray.getJSONObject(w);
                                String name = jsonObject.getString("name");
                                String phone_1 = jsonObject.getString("phone1");
                                String phone_2 = jsonObject.getString("phone2");
                                String phone_3 = jsonObject.getString("phone3");

                                if (name.equals(got_state)){
                                    phone1.setText(phone_1);
                                    phone2.setText(phone_2);
                                    phone3.setText(phone_3);
                                }
                            }

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

        phone1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone1.getText().toString()));
                startActivity(intent);
            }
        });

        phone2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone2.getText().toString()));
                startActivity(intent);
            }
        });

        phone3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone3.getText().toString()));
                startActivity(intent);
            }
        });


        return v;
    }

    public interface OnFragmentInteractionListener {
    }
}