package ng.com.followtheresult;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


public class FragmentUserProfile extends Fragment {

    TextView fullname, state, lga;
    SharedPreferences preferences, preferences2;
    String usertype;
    LinearLayout lin_lga, lin_lgacount;
    TextView lgaCount;
    AppCompatButton signout;

    public FragmentUserProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_user_profile, container, false);

        preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
        final String got_fullname = preferences.getString("fullname", "not available");
        final String got_state = preferences.getString("state", "not available");
        final String got_lga = preferences.getString("lga", "not available");
        usertype = preferences.getString("usertype", "");

        preferences2 = getActivity().getSharedPreferences("lga_count", Context.MODE_PRIVATE);
        final String lga_count = preferences2.getString("lgacount", "");

        fullname = v.findViewById(R.id.fullname);
        state = v.findViewById(R.id.state);
        lga = v.findViewById(R.id.lga);
        lin_lga = v.findViewById(R.id.lin_lga);
        lin_lgacount = v.findViewById(R.id.lin_lgacount);
        lgaCount = v.findViewById(R.id.lga_count);
        signout = v.findViewById(R.id.signout);

        fullname.setText(got_fullname);
        state.setText(got_state);
        lga.setText(got_lga);
        lgaCount.setText(lga_count);

        if(usertype.equals("admin")){
            lin_lga.setVisibility(View.GONE);
        }else{
            lin_lgacount.setVisibility(View.GONE);
        }



        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog myDialogFinish = new Dialog(getContext());
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
                        //on signout
                        SharedPreferences preferences = getActivity().getSharedPreferences("LoginDetails", Context.MODE_PRIVATE);
                        preferences.edit().clear().commit();
                        myDialogFinish.dismiss();

                        Intent i = new Intent(getContext(), LoginPage.class);
                        startActivity(i);
                    }
                });
                myDialogFinish.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                myDialogFinish.setCanceledOnTouchOutside(false);
                myDialogFinish.show();


            }
        });

        return v;
    }

    public interface OnFragmentInteractionListener {
    }
}