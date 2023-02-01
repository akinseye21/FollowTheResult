package ng.com.followtheresult;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class FragmentDashboard extends Fragment {

    TextView fullname, state, lga;
    SharedPreferences preferences;

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

        fullname = v.findViewById(R.id.fullname);
        state = v.findViewById(R.id.state);
        lga = v.findViewById(R.id.lga);

        fullname.setText(got_fullname);
        state.setText(got_state);
        lga.setText(got_lga);

        return v;
    }

    public interface OnFragmentInteractionListener {
    }
}