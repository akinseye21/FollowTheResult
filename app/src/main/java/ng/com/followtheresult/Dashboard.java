package ng.com.followtheresult;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity implements
FragmentDashboard.OnFragmentInteractionListener,
FragmentChecklist.OnFragmentInteractionListener,
FragmentContactCenter.OnFragmentInteractionListener,
FragmentUserProfile.OnFragmentInteractionListener{

    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView pageTitle;
    String email, fullname, state, lga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent i = getIntent();
        email = i.getStringExtra("email");
        fullname = i.getStringExtra("fullname");
        state = i.getStringExtra("state");
        lga = i.getStringExtra("lga");



        pageTitle = findViewById(R.id.pageTitle);
        viewPager = findViewById(R.id.viewpager);
        addTabs(viewPager);

        tabLayout = findViewById(R.id.tabLayout);
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

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic15);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic16);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic17);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic18);
    }

    private void addTabs(ViewPager viewPager) {
        Dashboard.ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new FragmentDashboard(), "");
        adapter.addFrag(new FragmentChecklist(), "");
        adapter.addFrag(new FragmentContactCenter(), "");
        adapter.addFrag(new FragmentUserProfile(), "");
        viewPager.setAdapter(adapter);
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