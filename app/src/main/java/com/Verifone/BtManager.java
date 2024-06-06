package com.Verifone;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Type: Class.
 * Access: Public.
 * Name: BtManager.
 *
 * @Description.
 * @EndDescription.
 */
public class BtManager implements BluetoothConnector.BluetoothSocketWrapper {

    /**
     * The constant TAG.
     */
    private static final String TAG = "BtManager";
    /**
     * The Spp uuid.
     */
    final UUID sppUuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    /**
     * The constant mmServerSocket.
     */
    private static BluetoothServerSocket mmServerSocket;
    /**
     * The Socket.
     */
    BluetoothSocket socket;
    /**
     * The constant btAdapter.
     */
    private static BluetoothAdapter btAdapter;
    /**
     * The Bt address.
     */
    private String btAddress = "";
    /**
     * The constant gct.
     */
    private static Context gct;

    /**
     * The Bluetooth connector.
     */
    BluetoothConnector bluetoothConnector;

    /**
     * The Writer.
     */
    BufferedOutputStream writer;
    /**
     * The Reader.
     */
    BufferedInputStream reader;


    /**
     * The enum Bt retult.
     */
    public enum BT_RETULT {
        /**
         * The Success.
         */
        SUCCESS,
        /**
         * The Fail.
         */
        FAIL
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: BtManager.
     *
     * @param gct       @PsiType:Context.
     * @param btAddress @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    public BtManager(Context gct, String btAddress) {


        gct = gct.getApplicationContext();
        this.btAddress = btAddress;

        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btAdapter == null) {
            Log.i("BT_MAN", "Bluetooth adapter is not available.");
            return;
        }

        if (!btAdapter.isEnabled()) {
            Log.i("BT_MAN", "Bluetooth is disabled. Check configuration.");
            return;
        }

        try {
            mmServerSocket = btAdapter.listenUsingRfcommWithServiceRecord("Bluetooth", sppUuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: BtSppWaitClient.
     *
     * @return bt retult
     * @Description.
     * @EndDescription. bt retult.
     */
    public BT_RETULT BtSppWaitClient() {
        BT_RETULT inRet = BT_RETULT.FAIL;
        BluetoothDevice device;
        Log.i(TAG, "BtSppWaitClient: " + btAddress);
        device = btAdapter.getRemoteDevice(btAddress);
        UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // bluetooth serial port service
        List<UUID> lista = new ArrayList<>();
        lista.add(SERIAL_UUID);
        bluetoothConnector = new BluetoothConnector(device, true, btAdapter, lista);
        try {
            socket = bluetoothConnector.connect().getUnderlyingSocket();
            writer = new BufferedOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            reader = new BufferedInputStream(new BufferedInputStream(socket.getInputStream()));
            return BT_RETULT.SUCCESS;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "BtSppWaitClient: Fallo La Clase Connector", e);
            inRet = BT_RETULT.FAIL;
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
            inRet = BT_RETULT.FAIL;
        }
        return inRet;
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: BtCLose.
     *
     * @Description.
     * @EndDescription.
     */
    public void BtCLose() {
        if (socket.isConnected()) {
            try {
                socket.close();
                Log.i("BT_MAN", "CONEXION CERRADA");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: BtSendData.
     *
     * @param chDataOut @PsiType:byte[].
     * @return bt retult
     * @Description.
     * @EndDescription. bt retult.
     */
    public BT_RETULT BtSendData(byte[] chDataOut) {

        Log.i("INFO:", "Entra BtSendData-------");
        BT_RETULT inBtRes = BT_RETULT.FAIL;
        try {
            Log.i("INFO:", "Entra try BtSendData-------");
            //writer = new BufferedWriter(new OutputStreamWriter(btSocket.getOutputStream(), "ASCII"));
            writer.write(chDataOut);
            writer.flush();
            LogHex(chDataOut, chDataOut.length);

            //Log.i("BT_MAN", "SEND: " + String.valueOf(chDataOut));
            inBtRes = BT_RETULT.SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
            inBtRes = BT_RETULT.FAIL;
            //VerifoneTaskManager.limpiarTerminal();
        }

        return inBtRes;
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: isBtConected.
     *
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean isBtConected() {
        if (socket != null)
            return socket.isConnected();
        else
            return false;
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: isBtEnable.
     *
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean isBtEnable() {
        return btAdapter.isEnabled();
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: BtEnable.
     *
     * @return boolean
     * @Description.
     * @EndDescription. boolean.
     */
    public boolean BtEnable() {
        return btAdapter.enable();
    }


    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: BtRecvData.
     *
     * @param chDataIn @PsiType:byte[].
     * @param inTOms   @PsiType:int.
     * @return array list
     * @Description.
     * @EndDescription. array list.
     */
    public ArrayList<String> BtRecvData(byte[] chDataIn, int inTOms) {
        ArrayList<String> datos = new ArrayList<>();
        ArrayList<String> logHex = new ArrayList<>();
        int inBtRes = 0;
        int inTime = 0;
        String s;
        try {

            while (inTime < inTOms) {
                if (reader.available() >= 1) {
                    inBtRes = reader.read(chDataIn);
                    logHex = LogHex(chDataIn, inBtRes);
                    break;
                } else {

                    try {
                        Thread.sleep(100);
                        inTime += 100;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.i("TAmanoBT", String.valueOf(logHex.size()));
            if (inBtRes == 0 && inTime >= inTOms) {
                Log.i("BT_MAN", "TIME OUT");
                inBtRes = -6;
                logHex.add("Error timeout");
            }

        } catch (IOException e) {
            e.printStackTrace();
            logHex.add("Error IO");
        } catch (NullPointerException e) {
            e.printStackTrace();
            logHex.add("Error NullPointer");
        } catch (RuntimeException e) {
            e.printStackTrace();
            logHex.add("Error Runtime");
        }

        return logHex;
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: LogHex.
     *
     * @param Data @PsiType:byte[].
     * @param iLen @PsiType:int.
     * @return array list
     * @Description.
     * @EndDescription. array list.
     */
    private ArrayList<String> LogHex(byte[] Data, int iLen) {
        //Log.i("INFO: ", "Entra a LogHex");
        int offset = 0;
        StringBuilder hex = new StringBuilder();
        ArrayList<String> arrayLlogHex = new ArrayList<>();

        //Log.i(sTraceMsg, "[" + String.valueOf(iLen) + "]");

        for (int i = 0; i < iLen; i++) {
            hex.append(Integer.toHexString((int) Data[i]));
            offset += 1;
            if (offset == 1) {
                //Log.i("->", hex.toString());
                arrayLlogHex.add(hex.toString());
                hex.setLength(0);
                offset = 0;
            }

        }

        /*for (int i = 0; i<arrayLlogHex.size();i++){
            Log.i("ArrayLogHex",arrayLlogHex.get(i));
        }*/

        return arrayLlogHex;
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: getInputStream.
     *
     * @return the input stream
     * @throws IOException the io exception
     * @Description.
     * @EndDescription.
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return bluetoothConnector.connect().getInputStream();
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: getOutputStream.
     *
     * @return the output stream
     * @throws IOException the io exception
     * @Description.
     * @EndDescription.
     */
    @Override
    public OutputStream getOutputStream() throws IOException {
        return bluetoothConnector.connect().getOutputStream();
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: getRemoteDeviceName.
     *
     * @return the remote device name
     * @Description.
     * @EndDescription.
     */
    @Override
    public String getRemoteDeviceName() {
        try {
            return bluetoothConnector.connect().getRemoteDeviceName();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: connect.
     *
     * @throws IOException the io exception
     * @Description.
     * @EndDescription.
     */
    @Override
    public void connect() throws IOException {
        bluetoothConnector.connect();
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: getRemoteDeviceAddress.
     *
     * @return the remote device address
     * @Description.
     * @EndDescription.
     */
    @Override
    public String getRemoteDeviceAddress() {
        try {
            return bluetoothConnector.connect().getRemoteDeviceAddress();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: close.
     *
     * @throws IOException the io exception
     * @Description.
     * @EndDescription.
     */
    @Override
    public void close() throws IOException {
        bluetoothConnector.connect().close();
    }

    /**
     * Type: Method.
     * Parent: BtManager.
     * Name: getUnderlyingSocket.
     *
     * @return the underlying socket
     * @Description.
     * @EndDescription.
     */
    @Override
    public BluetoothSocket getUnderlyingSocket() {
        try {
            return bluetoothConnector.connect().getUnderlyingSocket();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
