package com.example.quallet_v2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import java.io.IOException;
import java.util.UUID;

public class Home extends AppCompatActivity {

    String address = null;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
   // String CHANNEL_ID = "Quallet";


    private boolean isBtConnected = false;

    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    TextView pairing;
    ProgressBar progressBar;
    ImageView logo;
    TextView card1View;
    TextView card2View;
    TextView card3View;
    TextView card4View;
    TextView qualletOne;
    private boolean card1 = true;
    private boolean card2 = true;
   // private boolean card3 = true;
    //private boolean card4 = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent newint = getIntent();
        address = newint.getStringExtra(PairingActivity.EXTRA_ADDRESS); //receive the address of the bluetooth device

        new ConnectBT().execute();

        pairing = (TextView)findViewById(R.id.textView);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        logo = (ImageView)findViewById(R.id.logo);
        card1View = (TextView)findViewById(R.id.card1);
        card2View = (TextView)findViewById(R.id.card2);
        card3View = (TextView)findViewById(R.id.card3);
        card4View = (TextView)findViewById(R.id.card4);
        qualletOne = (TextView)findViewById(R.id.quallet);

     //   createNotificationChannel();


        pairing.setVisibility(View.VISIBLE);
        pairing.postDelayed(new Runnable() {
            public void run() {
                pairing.setVisibility(View.INVISIBLE);
            }
        }, 3500);

        progressBar.setVisibility(View.VISIBLE);
        progressBar.postDelayed(new Runnable() {
            public void run() {
                progressBar.setVisibility(View.INVISIBLE);
            }
        }, 3500);

        logo.setVisibility(View.VISIBLE);
        logo.postDelayed(new Runnable() {
            public void run() {
                logo.setVisibility(View.INVISIBLE);
            }
        }, 3500);

        card1View.setVisibility(View.INVISIBLE);
        card1View.postDelayed(new Runnable() {
            public void run() {
                card1View.setVisibility(View.VISIBLE);
            }
        }, 3500);

        card2View.setVisibility(View.INVISIBLE);
        card2View.postDelayed(new Runnable() {
            public void run() {
                card2View.setVisibility(View.VISIBLE);
            }
        }, 3500);

        card3View.setVisibility(View.INVISIBLE);
//        card3View.postDelayed(new Runnable() {
//            public void run() {
//                card3View.setVisibility(View.VISIBLE);
//            }
//        }, 3500);
//
        card4View.setVisibility(View.INVISIBLE);
//        card4View.postDelayed(new Runnable() {
//            public void run() {
//                card4View.setVisibility(View.VISIBLE);
//            }
//        }, 3500);

        qualletOne.setVisibility(View.INVISIBLE);
        qualletOne.postDelayed(new Runnable() {
            public void run() {
                qualletOne.setVisibility(View.VISIBLE);
            }
        }, 3500);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ArduinoRead();
            }
        }, 5000);





    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            Toast.makeText(getApplicationContext(), "Connecting", Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice quallet = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = quallet.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                Toast.makeText(getApplicationContext(), "Failed to Connect Try Again", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(),PairingActivity.class);
                startActivity(intent);
                finish();
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
                isBtConnected = true;
            }

        }
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { Log.e("error","error");}
        }
        finish(); //return to the first layout

    }

    private void ArduinoRead() {

      //  Log.e("Aashish", "1");

       // while (true) {

            int bytes;
           // StringBuilder readMessage = new StringBuilder();
//        Log.e("Aashish", "1");
//
//
//            Log.e("Aashish", "2");
//            byte[] buffer = new byte[512];
//            int bytes;
//            StringBuilder readMessage = new StringBuilder();
//            while (true) {
//                Log.e("Aashish", "2");
//                try {

//                    bytes = inputStream.read(buffer);
//                    String readed = new String(buffer, 0, bytes);
//                    readMessage.append(readed);
//                    Log.d("Aashish", readed);
//
//                    // маркер конца команды - вернуть ответ в главный поток
//                    if (readed.contains("\n")) {
//                       // mHandler.obtainMessage(DeviceControlActivity.MESSAGE_READ, bytes, -1, readMessage.toString()).sendToTarget();
//                        arduino.setText(readMessage.toString());
//                        readMessage.setLength(0);
//                    }
//
//                } catch (IOException e) {
//                    Log.e("error", "IOexception Read", e);
//                    break;
//                }
//        }

            if (btSocket != null) {


                try {

                    byte[] buffer = new byte[512];
                    bytes = btSocket.getInputStream().read(buffer);
                    String readed = new String(buffer, 0, bytes);

                    String [] codes = readed.split("\n");

                    for(String code: codes){
                        Log.e("Aashish", code);
                       // int numCode = Integer.parseInt(code);
                        updateUI(code);
                    }


//                    if (readed.contains("\n")) {
//                       // mHandler.obtainMessage(DeviceControlActivity.MESSAGE_READ, bytes, -1, readMessage.toString()).sendToTarget();
//                        arduino.setText("hello");
//                        switch (readMessage.toString()) {
//                            case "10":
//
//                        }



                        //readMessage.setLength(0);
                    //}
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Do something after 5s = 5000ms
                            ArduinoRead();
                        }
                    }, 1000);


                } catch (IOException e) {
                    Log.e("error", "IOexception Read", e);
                }
            }


        //}
    }

    private void changeCard1State(){
        if(card1){

            card1View.setText("Debit Card: In");
            card1View.setBackgroundResource(R.drawable.rounded_corner_in);
            //card1View.setBackgroundColor(getResources().getColor(R.color.cardIn));

        }
        else{

            card1View.setText("Debit Card: Out");
            card1View.setBackgroundResource(R.drawable.rounded_corner_out);
           // card1View.setBackgroundColor(getResources().getColor(R.color.cardOut));

        }
    }

    private void changeCard2State(){
        if(card2){
            card2View.setText("School ID: In");
            card2View.setBackgroundResource(R.drawable.rounded_corner_in);
           // card2View.setBackgroundColor(getResources().getColor(R.color.cardIn));
        }
        else{

            card2View.setText("School ID: Out");
            card2View.setBackgroundResource(R.drawable.rounded_corner_out);
          //  card2View.setBackgroundColor(getResources().getColor(R.color.cardOut));

        }
    }

//    private void changeCard3State(){
//        if(card3){
//            card3View.setText("Card 3 In");
//            card3View.setBackgroundResource(R.drawable.rounded_corner_in);
//        }
//        else{
//
//            card3View.setText("Card 3 Out");
//            card3View.setBackgroundResource(R.drawable.rounded_corner_out);
//
//        }
//    }
//
//    private void changeCard4State(){
//        if(card4){
//            card4View.setText("Card 4 In");
//            card4View.setBackgroundResource(R.drawable.rounded_corner_in);
//        }
//        else{
//
//            card4View.setText("Card 4 Out");
//            card4View.setBackgroundResource(R.drawable.rounded_corner_out);
//
//        }
//    }

    private void updateUI(String input){

        String inputTest = input.replaceAll("[^0-9]", "");




        switch (inputTest) {


            case "10":
                if (card1){
                    card1 = !card1;
                    changeCard1State();
                    //notifyCard1();
                    cardTakenOut();
                }
                break;

            case "11":

                if (!card1){
                    card1 = !card1;
                    changeCard1State();
                }
                break;
            case "20":
                if (card2){
                    card2 = !card2;
                    changeCard2State();
                    //notifyCard2();
                    cardTakenOut();
                }
                break;
            case "21":
                if (!card2){
                    card2 = !card2;
                    changeCard2State();
                }
                break;
//            case "30":
//                if (card3) {
//                    card3 = !card3;
//                    changeCard3State();
//                }
//                break;
//            case "31":
//                if (!card3) {
//                    card3 = !card3;
//                    changeCard3State();
//                }
//                break;
//            case "40":
//                if (card4){
//                    card4 = !card4;
//                    changeCard4State();
//                }
//                break;
//            case "41":
//                if (!card4){
//                    card4 = !card4;
//                    changeCard4State();
//                }
//                break;
            default:
                Log.e("UpdateUI", "Default on, Cases not working");

        }

    }

//
//    private void notifyCard1(){
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.quallet_logo)
//                .setContentTitle("Debit Card Out")
//                .setContentText("Debit Card has been taken out")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//// notificationId is a unique int for each notification that you must define
//        notificationManager.notify(1, builder.build());
//
//    }
//
//    private void notifyCard2(){
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setSmallIcon(R.drawable.quallet_logo)
//                .setContentTitle("School ID Out")
//                .setContentText("School ID has been taken out")
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//
//// notificationId is a unique int for each notification that you must define
//        notificationManager.notify(2, builder.build());
//
//    }
//
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = getString(R.string.channel_name);
//            String description = getString(R.string.channel_description);
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
//            channel.setDescription(description);
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

    private void cardTakenOut() {
        if (Build.VERSION.SDK_INT >= 26) {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            ((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(200);
        }
    }
//



}
