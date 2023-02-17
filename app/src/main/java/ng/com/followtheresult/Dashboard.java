package ng.com.followtheresult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements
FragmentDashboard.OnFragmentInteractionListener,
FragmentChecklist.OnFragmentInteractionListener,
FragmentContactCenter.OnFragmentInteractionListener,
FragmentUserProfile.OnFragmentInteractionListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView pageTitle;
    String usertype;
    String state;
    SharedPreferences sharedPreferences;

    public static final String GET_ALL_LGA_RESULT = "https://readytoleadafrica.org/rtl_mobile/getlgaresults";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        usertype = i.getStringExtra("from");
        state = i.getStringExtra("state");

        sharedPreferences = getSharedPreferences("lga_count", this.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

        //get the count of state
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, GET_ALL_LGA_RESULT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try{
                            JSONArray jsonArray = new JSONArray(response);
                            int numOfLga = jsonArray.length();
                            myEdit.putString( "lgacount", String.valueOf(numOfLga));
                            myEdit.commit();
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
                params.put("dstate", state);
                return params;
            }
        };

        RequestQueue requestQueue2 = Volley.newRequestQueue(getApplicationContext());
        DefaultRetryPolicy retryPolicy2 = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest2.setRetryPolicy(retryPolicy2);
        requestQueue2.add(stringRequest2);
        requestQueue2.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                requestQueue2.getCache().clear();
            }
        });








        pageTitle = findViewById(R.id.pageTitle);
        viewPager = findViewById(R.id.viewpager);


        tabLayout = findViewById(R.id.tabLayout);


        if (usertype.equals("lga_level")){

            addTabs(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons();

            tabLayout.setOnTabSelectedListener(
                    new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            if (tab.getPosition() == 0){
                                pageTitle.setText("HOME");
                            }
                            else if(tab.getPosition() == 1){
                                pageTitle.setText("CHECKLISTS");
                            }
                            else if(tab.getPosition() == 2){
                                pageTitle.setText("CONTACT CENTRE");
                            }
                            else if(tab.getPosition() == 3){
                                pageTitle.setText("USER PROFILE");
                            }
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
//                        super.onTabReselected(tab);
                            if(tab.getPosition() == 0){
                                pageTitle.setText("HOME");
                            }
                            else if(tab.getPosition() == 1){
                                pageTitle.setText("CHECKLISTS");
                            }
                            else if(tab.getPosition() == 2){
                                pageTitle.setText("CONTACT CENTRE");
                            }
                            else if(tab.getPosition() == 3){
                                pageTitle.setText("USER PROFILE");
                            }
                        }
                    }
            );
        }

        if(usertype.equals("state_level")){


            addTabs_state(viewPager);
            tabLayout.setupWithViewPager(viewPager);
            setupTabIcons_state();

            tabLayout.setOnTabSelectedListener(
                    new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {

                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            if (tab.getPosition() == 0){
                                pageTitle.setText("HOME");
                            }
                            else if(tab.getPosition() == 1){
                                pageTitle.setText("CHECKLISTS");
                            }
                            else if(tab.getPosition() == 2){
                                pageTitle.setText("CONTACT CENTRE");
                            }
                            else if(tab.getPosition() == 3){
                                pageTitle.setText("RESULT VERIFICATION");
                            }
                            else if(tab.getPosition() == 4){
                                pageTitle.setText("USER PROFILE");
                            }
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
//                        super.onTabReselected(tab);
                            if(tab.getPosition() == 0){
                                pageTitle.setText("HOME");
                            }
                            else if(tab.getPosition() == 1){
                                pageTitle.setText("CHECKLISTS");
                            }
                            else if(tab.getPosition() == 2){
                                pageTitle.setText("CONTACT CENTRE");
                            }
                            else if(tab.getPosition() == 3){
                                pageTitle.setText("RESULT VERIFICATION");
                            }
                            else if(tab.getPosition() == 4){
                                pageTitle.setText("USER PROFILE");
                            }
                        }
                    }
            );
        }

    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic15);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic16);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic17);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic18);
    }

    private void setupTabIcons_state() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic15);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic16);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic17);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic22);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic18);
    }

    private void addTabs(ViewPager viewPager) {
        Dashboard.ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentDashboard(), "");
        adapter.addFrag(new FragmentChecklist(), "");
        adapter.addFrag(new FragmentContactCenter(), "");
        adapter.addFrag(new FragmentUserProfile(), "");
        viewPager.setAdapter(adapter);
    }

    private void addTabs_state(ViewPager viewPager) {
        Dashboard.ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentDashboard(), "");
        adapter.addFrag(new FragmentChecklist(), "");
        adapter.addFrag(new FragmentContactCenter(), "");
        adapter.addFrag(new ResultVerification(), "");
        adapter.addFrag(new FragmentUserProfile(), "");
        viewPager.setAdapter(adapter);
    }

    public void navigateFragment(int position){
        viewPager.setCurrentItem(position, true);
    }


    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}