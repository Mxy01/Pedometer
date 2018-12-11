package comt.example.lenovo.pedometer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<Item> {
    ArrayList<Item> stepList = new ArrayList<>();
    public MyAdapter(Context context, int textViewResourceId, ArrayList<Item> objects) {
        super(context, textViewResourceId, objects);
        stepList = objects;
    }
    @Override
    public int getCount() {
        return super.getCount();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.list_view_item, null);
        TextView title = (TextView) v.findViewById(R.id.Title);
        TextView content=(TextView) v.findViewById(R.id.Content);
        ImageView imageView = (ImageView) v.findViewById(R.id.Medal);
        title.setText(stepList.get(position).getTitle());
        content.setText(stepList.get(position).getContent());
        imageView.setImageResource(stepList.get(position).getMedalImage());
        return v;
    }
}