package ng.com.followtheresult;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private Context context;
    private String[] responses;
    private ArrayList<String> arr_question;

    public ListViewAdapter(Context context, String[] responses, ArrayList<String> arr_question){
        this.context = context;
        this.responses = responses;
        this.arr_question = arr_question;
    }


    @Override
    public int getCount() {
        return arr_question.size();
    }

    @Override
    public Object getItem(int position) {
        return arr_question.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflaInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflaInflater.inflate(R.layout.list_arrival_result, parent, false);
        }

        TextView questionNum = convertView.findViewById(R.id.questionnum);
        TextView question = convertView.findViewById(R.id.question);
        TextView answer = convertView.findViewById(R.id.answer);

        int num = Integer.parseInt(String.valueOf(position))+1;

        questionNum.setText("Question "+num);
        question.setText(arr_question.get(position));
        answer.setText(responses[position]);

        return convertView;
    }
}
