package com.example.venom.controlrobotwithbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.jdom2.input.SAXBuilder;
import org.opencv.android.OpenCVLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import org.jdom2.*;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Context mAppContext;
    String a;
    Editable b;

    Vector<Sequence> sequences = new Vector<>();

    final static String TAG = "MainActivity";

    final static int FORWARD = 0;
    final static int LEFT = 1;
    final static int RIGHT = 2;
    final static int REVERSE = 3;
    final static int STOP = 4;
    final static int LINEON = 5;
    final static int LINEOFF = 6;
    final static int OBSTACLEON = 7;
    final static int OBSTACLEOFF = 8;
    final static int RCON = 9;
    final static int RCOFF = 10;

    // Used to load the 'native-lib' library on application startup.
    static {
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "opencv not loaded");
        } else {
            Log.d(TAG, "opencv loaded");
        }
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        //TextView tv = (TextView) findViewById(R.id.sample_text);
        //tv.setText(stringFromJNI());

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            Toast.makeText(mAppContext,  "Bluetooth not available", Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // If BT is not on, request that it be enabled.
        // setupChat() will then be called during onActivityResult
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, 3);
            // Otherwise, setup the chat session
        }
    }

    public void SeekBluetooth(View view) {
        final EditText name = (EditText) findViewById(R.id.editText);
        b = name.getText();
        a = b.toString();
        try {
            findBT();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void findBT() throws IOException
    {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // if(mBluetoothAdapter == null)
        //{
        //  myLabel.setText("No bluetooth adapter available");
        //}

        if(!mBluetoothAdapter.isEnabled())
        {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 1);

        }

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0)
        {
            for(BluetoothDevice device : pairedDevices)
            {
                if(device.getName().equals(a))//FireFly-B1A7Change to the name of your bluetooth module (Case sensitive)
                {
                    mmDevice = device;
                    break;
                }
            }
        }
        // myLabel.setText("Bluetooth Device Found");


        UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID

        mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);

        mmSocket.connect();
        mmOutputStream = mmSocket.getOutputStream();
        mmInputStream = mmSocket.getInputStream();

        //  beginListenForData();

        // myLabel.setText("Bluetooth Opened");
    }

    public void SendSequence(View view) {
        try {
            mmOutputStream.write(Sequence.START_SEQUENCE);
            for (int i = 0; i < Sequence.NUMBER_OF_SERVOS; i++) {
                mmOutputStream.write((new Random()).nextInt(180));
            }
            mmOutputStream.write(Sequence.STOP_SEQUENCE);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Connection not established with the robot", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void SendOne(View view) {
        int servo = (new Random()).nextInt(17);

        try {
            mmOutputStream.write(Sequence.START_SEQUENCE);
            for (int i = 0; i < Sequence.NUMBER_OF_SERVOS; i++) {
                if (i == servo) {
                    mmOutputStream.write((new Random()).nextInt(180));
                } else {
                    mmOutputStream.write(0);
                }
            }
            mmOutputStream.write(Sequence.STOP_SEQUENCE);
        } catch (IOException e) {
            Toast.makeText(MainActivity.this, "Connection not established with the robot", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
