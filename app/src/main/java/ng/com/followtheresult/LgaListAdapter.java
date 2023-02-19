package ng.com.followtheresult;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class LgaListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> lga;
    private ArrayList<String> status;
    private ArrayList<String> color;
    private String got_state;

    public LgaListAdapter(Context context, ArrayList<String> lga, ArrayList<String> status,  ArrayList<String> color, String got_state){
        this.context = context;
        this.lga = lga;
        this.status = status;
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

        if (status.get(position).equals("0")){
            mark.setImageResource(R.drawable.question);
//            mark.setImageTintMode(PorterDuff.Mode.MULTIPLY);
//            mark.setColorFilter(ContextCompat.getColor(context, R.color.gray), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
        else if (status.get(position).equals("2")){
            mark.setImageResource(R.drawable.check);
        }
        else if (status.get(position).equals("1")){
            mark.setImageResource(R.drawable.xmark);
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
                bundle.putString("status", status.get(position));

                if (status.get(position).equals("0")){
                    //pop up to say result has not been sent by the LGA staff yet
                    Dialog myDialog = new Dialog(parent.getContext());
                    myDialog.setContentView(R.layout.custom_popup_prompt);
                    TextView text = myDialog.findViewById(R.id.text);
                    AppCompatButton proceed = myDialog.findViewById(R.id.proceed);
                    AppCompatButton close = myDialog.findViewById(R.id.close);
                    text.setText("Result not yet available to verify. \nDo you want to proceed with result verification?");
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();
                        }
                    });
                    proceed.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();
                            //go to fragment to verify result
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
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.show();
                }
                if (status.get(position).equals("1")){
                    //go to fragment to verify result
                    FragmentResultState fragmentResultState = new FragmentResultState();
                    fragmentResultState.setArguments(bundle);
                    FragmentManager fragmentManager = ((FragmentActivity)context).getSupportFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    ft.replace(R.id.fragmentResultVerification,fragmentResultState);
                    ft.addToBackStack(null);
                    ft.commit();
                }
                if(status.get(position).equals("2")){
                    //pop up to show that you have already verified the result from the LGA
                    Dialog myDialog = new Dialog(parent.getContext());
                    myDialog.setContentView(R.layout.custom_popup_prompt);
                    TextView text = myDialog.findViewById(R.id.text);
                    AppCompatButton proceed = myDialog.findViewById(R.id.proceed);
                    AppCompatButton close = myDialog.findViewById(R.id.close);
                    text.setText("Sorry, you have already verified this LGA result.\n\nContact the service center if you have further concern.");
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            myDialog.dismiss();
                        }
                    });
                    proceed.setVisibility(View.GONE);
                    myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    myDialog.setCanceledOnTouchOutside(false);
                    myDialog.show();
                }

            }
        });


        return convertView;
    }
}
