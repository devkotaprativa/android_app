package com.example.prativa.myapp;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Prativa on 11/8/2017.
 */

public class uploadauthenticationstatustoserver extends AsyncTask<Void, Void, Void> {

    String destinationAddress;
    int destinationPort;

    byte[] b;
    String id="";
    Button buttonNo,buttonYes;

    uploadauthenticationstatustoserver(String address, int port,String id,Button buttonNo,Button buttonYes) {
        Log.d("con","yes");
        destinationAddress = address;
        destinationPort = port;
        this.id=id;
        this.buttonNo=buttonNo;
        this.buttonYes=buttonYes;
    }


    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            socket = new Socket(destinationAddress, destinationPort);
            Log.d("soc upload","connected");

            Log.d("soc upload",socket.getLocalAddress().toString());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                    1024);
            byte[] buffer = new byte[1024];

            OutputStream outputStream=socket.getOutputStream();

            DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
            if(id.equals("nobutton"))
            {
                dataOutputStream.write('0');
            }
            else{
                dataOutputStream.write('1');
            }

            dataOutputStream.flush();
            Log.d("data",String.valueOf(dataOutputStream));
            dataOutputStream.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        buttonNo.setVisibility(View.VISIBLE);
        buttonYes.setVisibility(View.VISIBLE);



    }

}

