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
import android.widget.ArrayAdapter;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ResultVerification extends Fragment {

    TextView fullname, state, lga;
    SharedPreferences preferences, preferences2;
    ListView listView;
    String color;
    String usertype;
    LinearLayout lin_lga, lin_lgacount;
    Dialog myDialog;
    TextView lgaCount;

    ArrayList<String> lga_of_state = new ArrayList<>();
    ArrayList<String> color_update = new ArrayList<>();

    public static final String GET_LGA = "https://readytoleadafrica.org/rtl_mobile/getlga";


    public ResultVerification() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_result_verification, container, false);

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
        listView = v.findViewById(R.id.listview);
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

        //show Dialog that is loading the information
        myDialog = new Dialog(getContext());
        myDialog.setContentView(R.layout.custom_popup_loading);
        TextView text = myDialog.findViewById(R.id.text);
        text.setText("Loading LGAs... Please wait");
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.setCanceledOnTouchOutside(false);
        myDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, GET_LGA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray jsonArray = new JSONArray(response);

                            myDialog.dismiss();

                            for(int w=0; w<=jsonArray.length()-1; w++){
                                JSONObject jsonObject = jsonArray.getJSONObject(w);
                                String my_lga = jsonObject.getString("lga");

                                if(w % 2 == 0){
                                    color = "dark";
                                }else{
                                    color = "white";
                                }

                                lga_of_state.add(my_lga);
                                color_update.add(color);
                            }

                            LgaListAdapter lgaListAdapter = new LgaListAdapter(getContext(), lga_of_state, color_update, got_state);
                            listView.setAdapter(lgaListAdapter);

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
                params.put("dstate", got_state);
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


        lga_of_state.clear();
        color_update.clear();

        return v;
    }
}