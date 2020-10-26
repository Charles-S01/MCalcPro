package com.example.mcalcpro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ca.roumani.i2c.MPro;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        }
        catch (Exception e)
        {
            Toast popup = Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT);
            popup.show();
        }
    }
}











