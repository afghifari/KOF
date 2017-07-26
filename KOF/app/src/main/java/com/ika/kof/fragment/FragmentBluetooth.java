package com.ika.kof.fragment;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ika.kof.Database.DataGraphic;
import com.ika.kof.MainActivity;
import com.ika.kof.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    private ArrayAdapter mAdapterPair;
    private Handler bluetoothIn;

    private FragmentGraph fragmentGraph;

    private String tabGraph;
    private String TabOfFragmentGraph;
    private final String currentDate = "currenttime";
    private final String sumpress = "sumpress";
    private String address = "";
    private int handlerState = 1;
    private boolean isBtConnected = false;
    private boolean isBtOn = false;
    private boolean isBtChecked = false;

    private ArrayList mArrayListPaired;
    private ArrayList mArrayListSearched;
    private ProgressDialog progress;

    private LinearLayout layoutwarning;
    private Switch aSwitch;
    private Button btnSearchBluetooth;
    private ListView pairedDevicelist;
    private ListView searchedDevicelist;
    private TextView bluetoothTextInfo;
    private ProgressBar spinner;

    private DataGraphic dataGraphic;

    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    public FragmentBluetooth() {

    }

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.w("VIEW CREATED", "FRAGMENT onViewCreated");
        pairedDevicesList();
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
        Log.w("DESTROY VIEW1", "FRAGMENT onDestroyView");

        if (myBluetooth.isDiscovering()) {
            Log.d("destroyview : ", "cancelDiscoveryBt");
            spinner.setVisibility(View.GONE);
            myBluetooth.cancelDiscovery();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.w("DESTROY1", "FRAGMENT onDestroy");
        bluetoothTextInfo.setText(getText(R.string.tap_search));
        getActivity().unregisterReceiver(mReceiver);
    }


    public void inisialisasiDataAwal(View rootView) {
        layoutwarning = (LinearLayout) rootView.findViewById(R.id.linearwarning);
        btnSearchBluetooth = (Button) rootView.findViewById(R.id.btnSearch);
        pairedDevicelist = (ListView) rootView.findViewById(R.id.listViewPair);
        searchedDevicelist = (ListView) rootView.findViewById(R.id.listViewSearch);
        bluetoothTextInfo = (TextView) rootView.findViewById(R.id.blute_text_info);
        aSwitch = (Switch) rootView.findViewById(R.id.switch_blute);
        spinner = (ProgressBar) rootView.findViewById(R.id.progressbar);
        spinner.setVisibility(View.GONE);

        TabOfFragmentGraph = ((MainActivity)getActivity()).getTabGraph();
        fragmentGraph = (FragmentGraph)getActivity()
                .getSupportFragmentManager()
                .findFragmentByTag(TabOfFragmentGraph);

        ((MainActivity)getActivity()).setTabGraph(tabGraph);
        dataGraphic = new DataGraphic(FragmentBluetooth.this.getActivity());

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

        getActivity().registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }

    private void showWarning(boolean status) {
        if (status) {
            layoutwarning.setVisibility(View.VISIBLE);
        } else {
            layoutwarning.setVisibility(View.GONE);
        }
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
        }
        else {
            if (myBluetooth.isEnabled()) {
                aSwitch.setChecked(true);
                showWarning(false);
            } else {
                aSwitch.setChecked(false);
                showWarning(true);
            }
        }
    }

    public void inisialisasiListener() {
        btnSearchBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Handler bluteHandler = new Handler();
                bluteHandler.post(handlerBluetooth);

                bluetoothInputHandler();

//                if (myBluetooth.isEnabled())
//                    searchNearestBluetooth();
//                else
//                    toastMsg(getResources().getString(R.string.please_blute));
            }
        });

        //attach a listener to check for changes in state
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if(isChecked){

                    myBluetooth.enable();
                    isBtChecked = true;

                }else{
                    myBluetooth.disable();
                }
            }
        });
    }

    private void pairedDevicesList() {
        if (myBluetooth.isEnabled()) {
            pairedDevices = myBluetooth.getBondedDevices();
            mArrayListPaired = new ArrayList();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice bt : pairedDevices) {
                    mArrayListPaired.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
                }
            } else {
                Toast.makeText(FragmentBluetooth.this.getActivity(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
            }

            mAdapter = new ArrayAdapter(FragmentBluetooth.this.getActivity(), android.R.layout.simple_list_item_1, mArrayListPaired);
            pairedDevicelist.setAdapter(mAdapter);
            pairedDevicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked
        }
    }

    private void searchNearestBluetooth() {
        mArrayListSearched = new ArrayList();
        //search bluetooth
        myBluetooth.startDiscovery();
    }

    private String getDateString() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String currentTime = df.format(c.getTime());

        return currentTime;
    }

    private void bluetoothInputHandler() {
        Context context = getActivity();
        SharedPreferences mPrefs;
        mPrefs = context.getSharedPreferences(getString(R.string.this_app), Context.MODE_PRIVATE);

        String date = mPrefs.getString(currentDate,null);
        int integerSumPress = mPrefs.getInt(sumpress,0);
        String currentTime = getDateString();

        SharedPreferences.Editor prefsEditor = mPrefs.edit();

        if (date == null) {
            Log.d("date: ","null");

            prefsEditor.putString(currentDate,getDateString());
            prefsEditor.putInt(sumpress,1);

        } else if (date != null && date.contentEquals(currentTime)) {
            Log.d("date: ","date!=null & = curentime");

            integerSumPress++;
            prefsEditor.putInt(sumpress,integerSumPress);

            Log.d("1myDate : ",date);
            Log.d("1integersumpress : ",integerSumPress + "");

        } else if (date != null && !date.contentEquals(currentTime) ){
            Log.d("date: ","date!=null & != curentime");
            //TODO simpan ke database sqlite

            boolean insertData = dataGraphic.addData(date,Integer.toString(integerSumPress));

            if (insertData) {
                Log.i("Database :","inserted succesfully");
            } else {
                Log.i("Database :","failed to insert data");
            }

            Log.d("2myDate : ",date);
            Log.d("2integersumpress : ",integerSumPress + "");
//                    Handler bluteHandler = new Handler();
//                    bluteHandler.post(handlerBluetooth);

        }

        prefsEditor.commit();
    }

    public void startBluetooth() {
        new ConnectBT().execute(); //Call the class to connect

        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                bluetoothInputHandler();

                String readMessage = (String) msg.obj;// msg.arg1 = bytes from connect thread

                bluetoothTextInfo.setText(readMessage);
                Log.d("pesannya : ",readMessage);
                toastMsg(readMessage);

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
                Log.w("btReceiver : ", "action_state_changed");
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                if (state == BluetoothAdapter.STATE_ON) {
                    isBtOn = true;
                    showWarning(false);
                    toastMsg("Enabled");
                    isBtChecked = false;
                    pairedDevicesList();
                } else {
                    isBtOn = false;
                    showWarning(true);
                }

                if (!isBtChecked)
                    aSwitch.setChecked(isBtOn);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                Log.w("btReceiver : ", "ACTION_DISCOVERY_STARTED");

                spinner.setVisibility(View.VISIBLE);

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.w("btReceiver : ", "ACTION_DISCOVERY_Finished");
                spinner.setVisibility(View.GONE);

            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                Log.w("btReceiver : ", "ACTION_found");
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                // Add the name and address to an array adapter to show in a ListView
                mArrayListSearched.add(device.getName() + "\n" + device.getAddress());

                mAdapterPair = new ArrayAdapter(FragmentBluetooth.this.getActivity(),android.R.layout.simple_list_item_1, mArrayListSearched);
                searchedDevicelist.setAdapter(mAdapterPair);
                searchedDevicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

                Log.d("deviceName : ", "" + device.getName());
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
//                    bluetoothTextInfo.setText(readMessage);
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

    int x = 3;
    int y = 5;

    Runnable handlerBluetooth = new Runnable() {
        @Override
        public void run() {

            Log.d("tes : ", x + "," + y);
            fragmentGraph.addDataGraph(x,y);
            x++;
            y++;
        }
    };

}