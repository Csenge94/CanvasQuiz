package edu.ubbcluj.canvas.view.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.instructure.canvasapi.model.Quiz;

import edu.ubbcluj.canvasAndroid.R;

public class CustomArrayAdapterQuizzes extends ArrayAdapter<Quiz> {
    private final Context context;
    private final List<Quiz> values;

    public CustomArrayAdapterQuizzes(Context context, List<Quiz> values) {
        super(context, R.layout.row_layout, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        double rowWidth;
        int labelWidth;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.assignment_row_layout, parent, false);

        rowWidth = (double) parent.getWidth();
        labelWidth = (int) (rowWidth * 65 / 100);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView textViewName = (TextView) rowView.findViewById(R.id.label);
        textViewName.setText(values.get(position).getTitle());
        textViewName.getLayoutParams().width = labelWidth;

        TextView textViewOutOf = (TextView) rowView.findViewById(R.id.outof);
        textViewOutOf.setText(values.get(position).getTitle());
        imageView.setImageResource(R.drawable.submission);

        return rowView;
    }

}