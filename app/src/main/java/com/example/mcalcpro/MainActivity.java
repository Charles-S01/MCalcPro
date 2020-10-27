package com.example.mcalcpro;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Locale;

import ca.roumani.i2c.MPro;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, SensorEventListener {

    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.tts = new TextToSpeech(this, this);

//        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
//        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

    }

    public void onInit(int initStatus)
    {
        this.tts.setLanguage(Locale.US);
    }

    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
    }

    public void onSensorChanged(SensorEvent event)
    {
        double ax = event.values[0];
        double ay = event.values[1];
        double az = event.values[2];

        double a = Math.sqrt(ax*ax + ay*ay + az*az);

        if (a > 20)
        {
            ((EditText) findViewById(R.id.pBox)).setText(null);
            ((EditText) findViewById(R.id.aBox)).setText(null);
            ((EditText) findViewById(R.id.iBox)).setText(null);
            ((TextView) findViewById(R.id.output)).setText(null);
        }
    }

    public void buttonClicked(View V)
    {
        String pBox = ((EditText) findViewById(R.id.pBox)).getText().toString();
        String aBox = ((EditText) findViewById(R.id.aBox)).getText().toString();
        String iBox = ((EditText) findViewById((R.id.iBox))).getText().toString();

        try {
            MPro mp = new MPro();
            mp.setPrinciple(pBox);
            mp.setAmortization(aBox);
            mp.setInterest(iBox);

            String monthlyPay = mp.computePayment("%,.2f");

            String s = "  Monthly Payment = " + monthlyPay;
            s += "\n\n  By making this payment monthly for " + aBox + " years, the mortgage will be paid in full. But if you terminate the mortgage on its nth anniversary, the balance still owing depends on n as shown below:" + "\n\n";

            double amort = Double.parseDouble(aBox);

            for (int i = 0; i <= amort; i++) {
                s += String.format("%8d", i) + mp.outstandingAfter(i, "%,16.0f");
                s += "\n\n";
            }

            ((TextView) findViewById(R.id.output)).setText(s);
            tts.speak(s, TextToSpeech.QUEUE_FLUSH, null);
        }
        catch (Exception e)
        {
            Toast popup = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            popup.show();
        }
    }
}











