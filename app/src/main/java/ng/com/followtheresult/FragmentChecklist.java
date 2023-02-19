package ng.com.followtheresult;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class FragmentChecklist extends Fragment {

    RelativeLayout arrivalChecklist, processChecklist, resultsChecklist;
    TextView fullname, state, lga;
    SharedPreferences preferences, preferences2;
    LinearLayout lin_lga, lin_lgacount;
    TextView lgaCount;
    String arrival_check, process_check, result_check;

    public FragmentChecklist() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_checklist, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final String got_fullname = preferences.getString("fullname", "not available");
        final String got_state = preferences.getString("state", "not available");
        final String got_lga = preferences.getString("lga", "not available");
        final String usertype = preferences.getString("usertype", "");
        arrival_check = preferences.getString("arrival_check", "not available");
        process_check = preferences.getString("process_check", "not available");
        result_check = preferences.getString("result_submit", "not available");

        preferences2 = getActivity().getSharedPreferences("lga_count", Context.MODE_PRIVATE);
        final String lga_count = preferences2.getString("lgacount", "");

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


        arrivalChecklist = v.findViewById(R.id.arrivalChecklist);
        processChecklist = v.findViewById(R.id.processChecklist);
        resultsChecklist = v.findViewById(R.id.resultsChecklist);

        arrivalChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentArrivalChecklist1 fragmentArrivalChecklist1 = new FragmentArrivalChecklist1();
                FragmentTransaction transaction = fm.beginTransaction();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.replace(R.id.fragmentChecklist, fragmentArrivalChecklist1).addToBackStack(null);
                transaction.commit();

            }
        });


        processChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrival_check.equals("0")){
                    Toast.makeText(getContext(), "Sorry!! Please fill out the arrival checklist before proceeding", Toast.LENGTH_SHORT).show();
                }else{
                    FragmentManager fm = getFragmentManager();
                    FragmentProcessChecklist fragmentProcessChecklist = new FragmentProcessChecklist();
                    FragmentTransaction transaction = fm.beginTransaction();
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    transaction.replace(R.id.fragmentChecklist, fragmentProcessChecklist).addToBackStack(null);
                    transaction.commit();
                }
            }
        });


        resultsChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(process_check.equals("0")){
                    Toast.makeText(getContext(), "Sorry!! Please complete the process checklist before proceeding", Toast.LENGTH_SHORT).show();
                }else{
                    FragmentManager fm = getFragmentManager();
                    FragmentResult fragmentResult = new FragmentResult();
                    FragmentTransaction transaction = fm.beginTransaction();
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    transaction.replace(R.id.fragmentChecklist, fragmentResult).addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        return v;
    }

    public interface OnFragmentInteractionListener {
    }
}