package comt.example.lenovo.pedometer;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    private static PedometerDBHelper dbHelper;
    SQLiteDatabase db;
    private SensorManager sensorManager;
    private Sensor stepCounter;//步伐总数传感器
    private Sensor stepDetector;//单次步伐传感器
    private SensorEventListener stepCounterListener;//步伐总数传感器事件监听器
    private SensorEventListener stepDetectorListener;//单次步伐传感器事件监听器
    private SimpleDateFormat simpleDateFormat;//时间格式化
    private TextView steps, yesterday_steps, total_steps;
    private int StepDetector = 0;
    private int StepCounter = 0;
    private Date today;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        steps = (TextView) findViewById(R.id.Steps);

        yesterday_steps = (TextView) findViewById(R.id.Yesterday_steps);
        total_steps = (TextView) findViewById(R.id.Total_steps);
        //图表数据
       BarChart mBarChart = (BarChart) findViewById(R.id.Barchart);
       // mBarChart.addBar(new BarModel(date,StepDetector,0xFF98F898));
//        mBarChart.addBar(new BarModel(2.f, 0xFF00FF7F));
//        mBarChart.addBar(new BarModel(3.3f, 0xFF00FF7F));
//        mBarChart.addBar(new BarModel(1.1f, 0xFF87CEFA));
//        mBarChart.addBar(new BarModel(2.7f, 0xFF87CEFA));
//        mBarChart.addBar(new BarModel(2.f, 0xFF87CEFA));
//        mBarChart.addBar(new BarModel(0.4f, 0xFF87CEFA));
//        mBarChart.addBar(new BarModel(4.f, 0xFF87CEFA));
//       mBarChart.startAnimation();
        initData();
        initListener();
        dbHelper=new PedometerDBHelper(this,3);
        db=getDB();
        //昨天和总的步数
        Cursor cursor = db.query("Pedometer",
                null,
                null,
                null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                date = cursor.getString(cursor.getColumnIndex("date"));
                float StepDetector = cursor.getFloat(cursor.getColumnIndex("steps"));
                mBarChart.addBar(new BarModel(date,StepDetector,0xFF00FF7F));
                StepCounter+=StepDetector;
            } while (cursor.moveToNext());
            total_steps.setText(StepCounter);
            cursor.close();
        }
        Calendar calendar = Calendar.getInstance();
        today=(Date) calendar.getTime();
        calendar.setTime(today);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String defaultStartDate = simpleDateFormat.format(calendar.getTime());
        Cursor cursor1 = db.query("Pedometer",
                null,
                 defaultStartDate,
                null,
                null, null, null);
        if (cursor1.moveToFirst()) {
            do {
                 date = cursor.getString(cursor.getColumnIndex("date"));
                float StepDetector = cursor.getFloat(cursor.getColumnIndex("steps"));
            } while (cursor.moveToNext());
           yesterday_steps.setText(StepDetector);
            cursor.close();
            db.close();
            mBarChart.startAnimation();
        }
    }
    private void registerSensor(){
        //注册传感器事件监听器
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)&&
                getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)){
           sensorManager.registerListener(stepDetectorListener,stepDetector,SensorManager.SENSOR_DELAY_FASTEST);
            sensorManager.registerListener(stepCounterListener,stepCounter,SensorManager.SENSOR_DELAY_FASTEST);
        }
    }
    protected void initData() {
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器系统服务
        stepCounter=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);//获取计步总数传感器
        stepDetector=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);//获取单次计步传感器
    }
    protected void initListener() {
        stepCounterListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.e("Counter-SensorChanged",event.values[0]+"---"+event.accuracy+"---"+event.timestamp);
                steps.setText(""+ event.values[0]);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.e("Counter-Accuracy",sensor.getName()+"---"+accuracy);
               // steps.setText("");
            }
        };

        stepDetectorListener=new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.e("Detector-SensorChanged",event.values[0]+"---"+event.accuracy+"---"+event.timestamp);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                Log.e("Detector-Accuracy",sensor.getName()+"---"+accuracy);
            }
        };
    }
    private void unregisterSensor(){
        //解注册传感器事件监听器
        if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_COUNTER)&&
                getPackageManager().hasSystemFeature(PackageManager.FEATURE_SENSOR_STEP_DETECTOR)){
            sensorManager.unregisterListener(stepCounterListener);
            sensorManager.unregisterListener(stepDetectorListener);
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        unregisterSensor();
        db = getDB();
        simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = simpleDateFormat.format(System.currentTimeMillis());
        //Date date = new Date(System.currentTimeMillis());
        ContentValues values;
        values = new ContentValues();
        values.put("date",currentDate);
        values.put("steps", Integer.parseInt(steps.getText().toString()));
        db.insert("Pedometer", null, values);
        db.close();
    }
    @Override
    public void onResume(){
        super.onResume();
        registerSensor();
    }
    public static SQLiteDatabase getDB() {return dbHelper.getWritableDatabase(); }

    public void click(View view) {
        Intent intent=new Intent(this,Achievements.class);
        startActivity(intent);
    }
}



