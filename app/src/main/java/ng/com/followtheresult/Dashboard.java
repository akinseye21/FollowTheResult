package ng.com.followtheresult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
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
    String sent_from;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        usertype = i.getStringExtra("from");
        state = i.getStringExtra("state");


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
                            else if(tab.getPosition() == 4){
                                pageTitle.setText("INCIDENCE CHECKLIST");
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
                            else if(tab.getPosition() == 4){
                                pageTitle.setText("INCIDENCE CHECKLIST");
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
                            else if(tab.getPosition() == 5){
                                pageTitle.setText("INCIDENCE CHECKLIST");
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
                            else if(tab.getPosition() == 5){
                                pageTitle.setText("INCIDENCE CHECKLIST");
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
        tabLayout.getTabAt(4).setIcon(R.drawable.forbidden);
    }

    private void setupTabIcons_state() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic15);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic16);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic17);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic22);
        tabLayout.getTabAt(4).setIcon(R.drawable.ic18);
        tabLayout.getTabAt(5).setIcon(R.drawable.forbidden);
    }

    private void addTabs(ViewPager viewPager) {
        Dashboard.ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentDashboard(), "");
        adapter.addFrag(new FragmentChecklist(), "");
        adapter.addFrag(new FragmentContactCenter(), "");
        adapter.addFrag(new FragmentUserProfile(), "");
        adapter.addFrag(new FragmentViolenceChecklist(), "");
        viewPager.setAdapter(adapter);
    }

    private void addTabs_state(ViewPager viewPager) {
        Dashboard.ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentDashboard(), "");
        adapter.addFrag(new FragmentChecklist(), "");
        adapter.addFrag(new FragmentContactCenter(), "");
        adapter.addFrag(new ResultVerification(), "");
        adapter.addFrag(new FragmentUserProfile(), "");
        adapter.addFrag(new FragmentViolenceChecklist(), "");
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

    @Override
    public void onBackPressed() {
        Dialog myDialogFinish = new Dialog(Dashboard.this);
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
                SharedPreferences preferences = getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                preferences.edit().clear().commit();

                Intent i = new Intent(Dashboard.this, LoginPage.class);
                startActivity(i);
            }
        });
        myDialogFinish.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialogFinish.setCanceledOnTouchOutside(false);
        myDialogFinish.show();
    }
}