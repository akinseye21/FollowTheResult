package ng.com.followtheresult;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class LgaListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> lga;
    private ArrayList<String> color;
    private String got_state;

    public LgaListAdapter(Context context, ArrayList<String> lga,  ArrayList<String> color, String got_state){
        this.context = context;
        this.lga = lga;
        this.color = color;
        this.got_state = got_state;
    }


    @Override
    public int getCount() {
        return lga.size();
    }

    @Override
    public Object getItem(int i) {
        return lga.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_lga, parent, false);
        }

        RelativeLayout myrel = convertView.findViewById(R.id.myrel);
        TextView lga_name = convertView.findViewById(R.id.text);
        ImageView mark = convertView.findViewById(R.id.mark);

        lga_name.setText(lga.get(position));
        if (color.get(position).equals("white")){
            myrel.setBackgroundResource(R.drawable.normal_white_corner);
            lga_name.setTextColor(Color.parseColor("#18355f"));
        }else{
            myrel.setBackgroundResource(R.drawable.normal_dark_corner);
            lga_name.setTextColor(Color.parseColor("#ffffff"));
        }

        myrel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                FragmentManager fm = getFragmentManager();
//                FragmentArrivalChecklist1 fragmentArrivalChecklist1 = new FragmentArrivalChecklist1();
//                FragmentTransaction transaction = fm.beginTransaction();
//                fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                transaction.replace(R.id.fragmentChecklist, fragmentArrivalChecklist1).addToBackStack(null);
//                transaction.commit();

                Bundle bundle = new Bundle();
                bundle.putString("state", got_state);
                bundle.putString("lga", lga.get(position));

                FragmentResultState fragmentResultState = new FragmentResultState();
                fragmentResultState.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                FragmentTransaction ft = fragmentManager.beginTransaction();
                fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.replace(R.id.fragmentResultVerification,fragmentResultState);
                ft.addToBackStack(null);
                ft.commit();

            }
        });


        return convertView;
    }
}
