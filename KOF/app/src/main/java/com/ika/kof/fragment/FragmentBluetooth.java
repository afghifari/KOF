package com.ika.kof.fragment;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ika.kof.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Ghifari on 7/6/2017.
 */

public class FragmentBluetooth extends Fragment {

    private BluetoothAdapter myBluetooth = null;
    private BluetoothSocket btSocket = null;

    private Set<BluetoothDevice> pairedDevices;
    private ArrayAdapter mAdapter;
    private Handler bluetoothIn;

    private String address = "";
    private int handlerState = 1;
    private boolean isBtConnected = false;

    private ArrayList mArrayList;
    private ProgressDialog progress;

    private Button btnSearchBluetooth;
    private ListView devicelist;
    private TextView bluetoothTextInfo;

    private static final UUID myUUID3 =
            UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    private static final UUID myUUID2 =
            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_bluetooth, container, false);

        inisialisasiDataAwal(rootView);

        checkBluetoothAvailability();

        inisialisasiListener();


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.w("START", "FRAGMENT onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.w("RESUME", "FRAGMENT onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.w("PAUSE", "FRAGMENT onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.w("STOP", "FRAGMENT onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.w("DESTROY VIEW", "FRAGMENT onDestroyView");


    }

    @Override
    public void onDestroy() {
        Log.w("DESTROY", "FRAGMENT onDestroy");
        bluetoothTextInfo.setText(getText(R.string.tap_search));
        getActivity().unregisterReceiver(mReceiver);
        super.onDestroy();
    }


    public void inisialisasiDataAwal(View rootView) {
        btnSearchBluetooth = (Button) rootView.findViewById(R.id.btnSearch);
        devicelist = (ListView) rootView.findViewById(R.id.listView);
        bluetoothTextInfo = (TextView) rootView.findViewById(R.id.blute_text_info);
    }

    /**
     * cek apakah bluetooth tersedia
     * jika tersedia maka cek untuk dinyalakan
     */
    public void checkBluetoothAvailability() {
        myBluetooth = BluetoothAdapter.getDefaultAdapter();
        if (myBluetooth == null) {
            //Show a mensag. that thedevice has no bluetooth adapter
            Toast.makeText(FragmentBluetooth.this.getActivity(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

        } else {
            if (myBluetooth.isEnabled()) {
            } else {
                //Ask to the user turn the bluetooth on
                Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(turnBTon, 1);
            }
        }
    }

    public void inisialisasiListener() {
        btnSearchBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pairedDevicesList(); //method that will be called
            }
        });
    }

    private void pairedDevicesList() {
        pairedDevices = myBluetooth.getBondedDevices();
        mArrayList = new ArrayList();

        if (pairedDevices.size()>0) {
            for(BluetoothDevice bt : pairedDevices) {
                mArrayList.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else {
            Toast.makeText(FragmentBluetooth.this.getActivity(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        //search bluetooth
        myBluetooth.startDiscovery();

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        getActivity().registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy


        if (!mArrayList.isEmpty()) {
            bluetoothTextInfo.setText(getText(R.string.tap_connect));
        }

        // TODO here : save list bluetooth ke database sementara yang akan dikosongkan jika di destroy


        mAdapter = new ArrayAdapter(FragmentBluetooth.this.getActivity(),android.R.layout.simple_list_item_1, mArrayList);
        devicelist.setAdapter(mAdapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
    }

    public void startBluetooth() {
        new ConnectBT().execute(); //Call the class to connect

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
//                SharedPreferences mPrefs;
//                mPrefs = getSharedPreferences("geloman", Context.MODE_PRIVATE);
//                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                String readMessage = (String) msg.obj;// msg.arg1 = bytes from connect thread
                //toastMsg(readMessage);
                bluetoothTextInfo.setText(readMessage);
                Log.d("pesannya : ",readMessage);


            }
        };
    }

    private void toastMsg(String s) {
        Toast.makeText(FragmentBluetooth.this.getActivity(),s,Toast.LENGTH_LONG).show();
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            Log.d("onBtReceive : ", "1");
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    toastMsg("Enabled");
                }

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

                // TODO here make user can cancel the progress
                progress = ProgressDialog.show(FragmentBluetooth.this.getActivity(), "Searching Bluetooth. . .", "Please wait!");

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mAdapter = new ArrayAdapter(FragmentBluetooth.this.getActivity(),android.R.layout.simple_list_item_1, mArrayList);
                devicelist.setAdapter(mAdapter);
                devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

                progress.dismiss();

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // Add the name and address to an array adapter to show in a ListView
                mArrayList.add(device.getName() + "\n" + device.getAddress());
                Log.d("deviceName : ", "" + device.getName());
                try {
                    if (device != null) {
                        Log.i("UUID : ", "" + device.getUuids()[0].getUuid());
                    } else
                        Log.d("UUID : ", "nullll");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                toastMsg("found : " + device.getName());
            }
        }
    };

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick (AdapterView av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            address = info.substring(info.length() - 17);
            // Make an intent to start next activity.
            startBluetooth();
        }
    };

    private class ConnectBT extends AsyncTask<Void, Void, Void> { // UI thread{
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            //show a progress dialog
            progress = ProgressDialog.show(FragmentBluetooth.this.getActivity(), "Connecting...", "Please wait!");
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess) {
                toastMsg("Connection Failed. Is it a SPP Bluetooth? Try again.");
            }
            else {
                toastMsg("Connected.");
                isBtConnected = true;
                ConnectedThread mConnectedThread;
                mConnectedThread = new ConnectedThread(btSocket);
                mConnectedThread.start();

                //I send a character when resuming.beginning transmission to check device is connected
                //If it is not an exception will be thrown in the write method and finish() will be called
                mConnectedThread.write("x");
            }
            progress.dismiss();
        }
    }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    String readMessage = "";
                    bytes = mmInStream.read(buffer);            //read bytes from input buffer
                    readMessage = new String(buffer, 0, bytes);
                    Log.d("inipesan : ", readMessage);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        // sepertinya ini tidak perlu karena android kita tidak mengirimkan pesan apapun ke device
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(FragmentBluetooth.this.getActivity(), "Connection Failure", Toast.LENGTH_LONG).show();
            }
        }
    }
}