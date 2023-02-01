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
import android.widget.RelativeLayout;
import android.widget.TextView;


public class FragmentChecklist extends Fragment {

    RelativeLayout arrivalChecklist, processChecklist, resultsChecklist;
    TextView fullname, state, lga;
    SharedPreferences preferences;

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

        fullname = v.findViewById(R.id.fullname);
        state = v.findViewById(R.id.state);
        lga = v.findViewById(R.id.lga);

        fullname.setText(got_fullname);
        state.setText(got_state);
        lga.setText(got_lga);


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

//                Intent i = new Intent(getContext(), ArrivalChecklist.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
            }
        });


        processChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentProcessChecklist fragmentProcessChecklist = new FragmentProcessChecklist();
                FragmentTransaction transaction = fm.beginTransaction();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.replace(R.id.fragmentChecklist, fragmentProcessChecklist).addToBackStack(null);
                transaction.commit();

//                Intent i = new Intent(getContext(), ProcessChecklist.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(i);
            }
        });


        resultsChecklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                FragmentResult fragmentResult = new FragmentResult();
                FragmentTransaction transaction = fm.beginTransaction();
                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                transaction.replace(R.id.fragmentChecklist, fragmentResult).addToBackStack(null);
                transaction.commit();
            }
        });

        return v;
    }

    public interface OnFragmentInteractionListener {
    }
}