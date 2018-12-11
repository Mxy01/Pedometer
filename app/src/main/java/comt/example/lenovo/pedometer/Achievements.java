package comt.example.lenovo.pedometer;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Vector;

public class Achievements extends AppCompatActivity {
    private ListView listView;
    private Vector<String> querys;
    ArrayList<Item> stepList=new ArrayList<>();
    MyAdapter myAdapter;
    private static final String[] TITLE = {
            "Beginner I",
            "Beginner II",
            "Beginner III",
            "Intermediate I",
            "Intermediate II",
            "Intermediate III",
            "advanced I",
            "advanced II",
            "advanced III",
            "advanced V",

    };
    public void onUserSelect(int index){
        stepList.remove(index);
        myAdapter.notifyDataSetInvalidated();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);
        listView=(ListView) findViewById(R.id.list);
        for(String title :TITLE){
           stepList.add(new Item(title,"Congratulations on completing today's steps !",(R.drawable.medal)));
        }
        myAdapter = new MyAdapter(this,R.layout.list_view_item,stepList);
        listView.setAdapter(myAdapter);
    }
}