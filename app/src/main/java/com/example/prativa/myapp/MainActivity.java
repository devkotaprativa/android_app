package com.example.prativa.myapp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.os.Environment;

public class MainActivity extends Activity {

    EditText editTextAddress;
    Button buttonConnect, buttonApprove, buttonDecline;
    //TextView textPort;

    static final int SocketServerPORT = 6000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonConnect = (Button) findViewById(R.id.connect);
        buttonConnect.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                ClientRxThread clientRxThread =
                        new ClientRxThread("52.70.92.152",
                                SocketServerPORT);

                clientRxThread.start();
            }});
        buttonApprove = (Button) findViewById(R.id.approve);
        buttonDecline = (Button) findViewById(R.id.decline);
        buttonApprove.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                uploadauthenticationstatustoserver authenticate = new uploadauthenticationstatustoserver("52.70.92.152", 6000, "yesbutton", buttonApprove, buttonDecline);
                authenticate.execute();
            }
        });
        buttonDecline.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                uploadauthenticationstatustoserver authenticate = new uploadauthenticationstatustoserver("52.70.92.152", 6000, "nobutton", buttonApprove, buttonDecline);
                authenticate.execute();
            }
        });
    }
    private class ClientRxThread extends Thread {
        String dstAddress;
        int dstPort;

        ClientRxThread(String address, int port) {
            dstAddress = address;
            dstPort = port;
        }

        @Override
        public void run() {
            Socket socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());

                Log.d("ClientActivity", "C: Connecting...");
                byte[] bytes = new byte[1024];
                InputStream is = socket.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is,1024);
              final Bitmap bitmap = BitmapFactory.decodeStream(bis);
                socket.close();
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        ImageView image = (ImageView) findViewById(R.id.userimage);
                        image.setImageBitmap(bitmap);
                    }});


            } catch (IOException e) {

                e.printStackTrace();

                final String eMsg = "Something wrong: " + e.getMessage();
                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                eMsg,
                                Toast.LENGTH_LONG).show();
                    }});

            } finally {
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }



}



