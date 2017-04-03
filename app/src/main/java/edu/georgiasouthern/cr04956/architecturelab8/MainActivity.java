package edu.georgiasouthern.cr04956.architecturelab8;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RhodesSensorEventListener[] handlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handlers = new RhodesSensorEventListener[4];

        TextView tempVal = (TextView) findViewById(R.id.txtTemperatureVal);
        TextView pressureVal = (TextView) findViewById(R.id.txtPressureVal);
        TextView humidityVal = (TextView) findViewById(R.id.txtHumidityVal);
        TextView lightVal = (TextView) findViewById(R.id.txtLightVal);

        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);

        Sensor tempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        Sensor pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE);
        Sensor humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        Sensor lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        handlers[0] = new RhodesSensorEventListener(tempVal, Sensor.STRING_TYPE_AMBIENT_TEMPERATURE, tempSensor, sensorManager);
        handlers[1] = new RhodesSensorEventListener(pressureVal, Sensor.STRING_TYPE_PRESSURE, pressureSensor, sensorManager);
        handlers[2] = new RhodesSensorEventListener(humidityVal, Sensor.STRING_TYPE_RELATIVE_HUMIDITY, humiditySensor, sensorManager);
        handlers[3] = new RhodesSensorEventListener(lightVal, Sensor.STRING_TYPE_LIGHT, lightSensor, sensorManager);



    }

    @Override
    protected void onResume() {
        super.onResume();
        for (RhodesSensorEventListener listen: handlers)
        {
           listen.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (RhodesSensorEventListener listen: handlers)
        {
            listen.onPause();
        }
    }

    private class RhodesSensorEventListener implements SensorEventListener {
        private TextView outView;
//        private ArrayList values;
        private float value;
        private String sensorName;
        private Sensor theSensor;
        private SensorManager theManager;

        RhodesSensorEventListener(TextView out, String name, Sensor sensor, SensorManager manager) {
            super();
            outView = out;
            sensorName = name;
            theSensor = sensor;
            theManager = manager;
            if(sensor == null)
                out.setText(R.string.text_null_sensor);
        }

        public void setSensorName(String name) {
            sensorName = name;
        }

        public float getValue() {
            return value;
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            value = event.values[0];

            outView.setText(Float.toString(value));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

            Toast.makeText(getApplicationContext(), sensorName + "Accuracy Changed", Toast.LENGTH_SHORT).show();
        }

        protected void onResume() {
            theManager.registerListener(this, theSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

        protected void onPause() {
            theManager.unregisterListener(this);
        }
    }
}
