package ng.com.followtheresult;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;


public class FragmentDashboard extends Fragment {

    TextView fullname, state, lga;
    SharedPreferences preferences, preferences2;

    TextView countForArrival, countForProcess, countForResult;
    TextView lgaCount;
    AppCompatButton btn;
    LinearLayout lin_lga, lin_lgacount;


    public FragmentDashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final String got_fullname = preferences.getString("fullname", "not available");
        final String got_state = preferences.getString("state", "not available");
        final String got_lga = preferences.getString("lga", "not available");
        final String usertype = preferences.getString("usertype", "");
        final String arrival_check = preferences.getString("arrival_check", "not available");
        final String process_check = preferences.getString("process_check", "not available");
        final String result_check = preferences.getString("result_submit", "not available");

        preferences2 = getActivity().getSharedPreferences("lga_count", Context.MODE_PRIVATE);
        final String lga_count = preferences2.getString("lgacount", "1");

        countForArrival = v.findViewById(R.id.countForArrival);
        countForProcess = v.findViewById(R.id.countForProcess);
        countForResult = v.findViewById(R.id.countForResult);
        btn = v.findViewById(R.id.btn);
        lgaCount = v.findViewById(R.id.lga_count);

        lgaCount.setText(lga_count);

        //check if processes have been submitted
        if (arrival_check.equals("0")){
            countForArrival.setText("0%");
        }else{
            countForArrival.setText("100%");
        }
        if (process_check.equals("0")){
            countForProcess.setText("0%");
        }else{
            countForProcess.setText("100%");
        }
        if (result_check.equals("0")){
            countForResult.setText("0%");
        }else{
            countForResult.setText("100%");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Dashboard)getActivity()).navigateFragment(1);
            }
        });


        fullname = v.findViewById(R.id.fullname);
        state = v.findViewById(R.id.state);
        lga = v.findViewById(R.id.lga);
        lin_lga = v.findViewById(R.id.lin_lga);
        lin_lgacount = v.findViewById(R.id.lin_lgacount);

        fullname.setText(got_fullname);
        state.setText(got_state);
        lga.setText(got_lga);

        if(usertype.equals("admin")){
            lin_lga.setVisibility(View.GONE);
        }else{
            lin_lgacount.setVisibility(View.GONE);
        }

        return v;
    }

    public interface OnFragmentInteractionListener {
    }
}