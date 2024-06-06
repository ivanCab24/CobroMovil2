package com.Verifone;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Type: Class.
 * Access: Public.
 * Name: BluetoothConnector.
 *
 * @Description.
 * @EndDescription.
 */
public class BluetoothConnector {

    /**
     * The Bluetooth socket.
     */
    private BluetoothSocketWrapper bluetoothSocket;
    /**
     * The Device.
     */
    private BluetoothDevice device;
    /**
     * The Secure.
     */
    private boolean secure;
    /**
     * The Adapter.
     */
    private BluetoothAdapter adapter;
    /**
     * The Uuid candidates.
     */
    private List<UUID> uuidCandidates;
    /**
     * The Candidate.
     */
    private int candidate;


    /**
     * Type: Method.
     * Parent: BluetoothConnector.
     * Name: BluetoothConnector.
     *
     * @param device         @PsiType:BluetoothDevice.
     * @param secure         @PsiType:boolean.
     * @param adapter        @PsiType:BluetoothAdapter.
     * @param uuidCandidates @PsiType:List<UUID>.
     * @Description.
     * @EndDescription.
     */
    public BluetoothConnector(BluetoothDevice device, boolean secure, BluetoothAdapter adapter,
                              List<UUID> uuidCandidates) {
        this.device = device;
        this.secure = secure;
        this.adapter = adapter;
        this.uuidCandidates = uuidCandidates;

        if (this.uuidCandidates == null || this.uuidCandidates.isEmpty()) {
            this.uuidCandidates = new ArrayList<UUID>();
            this.uuidCandidates.add(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        }
    }

    /**
     * Type: Method.
     * Parent: BluetoothConnector.
     * Name: connect.
     *
     * @return bluetooth socket wrapper
     * @throws IOException the io exception
     * @Description.
     * @EndDescription. bluetooth socket wrapper.
     */
    public BluetoothSocketWrapper connect() throws IOException {
        boolean success = false;
        while (selectSocket()) {
            adapter.cancelDiscovery();

            try {
                bluetoothSocket.connect();
                success = true;
                break;
            } catch (IOException e) {
                //try the fallback
                try {
                    bluetoothSocket = new FallbackBluetoothSocket(bluetoothSocket.getUnderlyingSocket());
                    Thread.sleep(500);
                    bluetoothSocket.connect();
                    success = true;
                    break;
                } catch (FallbackException e1) {
                    Log.w("BT", "Could not initialize FallbackBluetoothSocket classes.", e);
                } catch (InterruptedException e1) {
                    Log.w("BT", e1.getMessage(), e1);
                } catch (IOException e1) {
                    Log.w("BT", "Fallback failed. Cancelling.", e1);
                }
            }
        }

        if (!success) {
            throw new IOException("Could not connect to device: " + device.getAddress());
        }

        return bluetoothSocket;
    }

    /**
     * Type: Method.
     * Parent: BluetoothConnector.
     * Name: selectSocket.
     *
     * @return boolean
     * @throws IOException the io exception
     * @Description.
     * @EndDescription. boolean.
     */
    private boolean selectSocket() throws IOException {
        if (candidate >= uuidCandidates.size()) {
            return false;
        }

        BluetoothSocket tmp;
        UUID uuid = uuidCandidates.get(candidate++);

        Log.i("BT", "Attempting to connect to Protocol: " + uuid);
        if (secure) {
            tmp = device.createRfcommSocketToServiceRecord(uuid);
        } else {
            tmp = device.createInsecureRfcommSocketToServiceRecord(uuid);
        }
        bluetoothSocket = new NativeBluetoothSocket(tmp);

        return true;
    }

    /**
     * Type: Interface.
     * Parent: BluetoothConnector.
     * Name: BluetoothSocketWrapper.
     */
    public static interface BluetoothSocketWrapper {

        /**
         * Type: Method.
         * Parent: BluetoothSocketWrapper.
         * Name: getInputStream.
         *
         * @return the input stream
         * @throws IOException the io exception
         * @Description.
         * @EndDescription.
         */
        InputStream getInputStream() throws IOException;

        /**
         * Type: Method.
         * Parent: BluetoothSocketWrapper.
         * Name: getOutputStream.
         *
         * @return the output stream
         * @throws IOException the io exception
         * @Description.
         * @EndDescription.
         */
        OutputStream getOutputStream() throws IOException;

        /**
         * Type: Method.
         * Parent: BluetoothSocketWrapper.
         * Name: getRemoteDeviceName.
         *
         * @return the remote device name
         * @Description.
         * @EndDescription.
         */
        String getRemoteDeviceName();

        /**
         * Type: Method.
         * Parent: BluetoothSocketWrapper.
         * Name: connect.
         *
         * @throws IOException the io exception
         * @Description.
         * @EndDescription.
         */
        void connect() throws IOException;

        /**
         * Type: Method.
         * Parent: BluetoothSocketWrapper.
         * Name: getRemoteDeviceAddress.
         *
         * @return the remote device address
         * @Description.
         * @EndDescription.
         */
        String getRemoteDeviceAddress();

        /**
         * Type: Method.
         * Parent: BluetoothSocketWrapper.
         * Name: close.
         *
         * @throws IOException the io exception
         * @Description.
         * @EndDescription.
         */
        void close() throws IOException;

        /**
         * Type: Method.
         * Parent: BluetoothSocketWrapper.
         * Name: getUnderlyingSocket.
         *
         * @return the underlying socket
         * @Description.
         * @EndDescription.
         */
        BluetoothSocket getUnderlyingSocket();

    }


    /**
     * Type: Class.
     * Access: Public.
     * Name: NativeBluetoothSocket.
     *
     * @Description.
     * @EndDescription.
     */
    public static class NativeBluetoothSocket implements BluetoothSocketWrapper {

        /**
         * The Socket.
         */
        private BluetoothSocket socket;

        /**
         * Type: Method.
         * Parent: NativeBluetoothSocket.
         * Name: NativeBluetoothSocket.
         *
         * @param tmp @PsiType:BluetoothSocket.
         * @Description.
         * @EndDescription.
         */
        public NativeBluetoothSocket(BluetoothSocket tmp) {
            this.socket = tmp;
        }

        /**
         * Type: Method.
         * Parent: NativeBluetoothSocket.
         * Name: getInputStream.
         *
         * @return the input stream
         * @throws IOException the io exception
         * @Description.
         * @EndDescription.
         */
        @Override
        public InputStream getInputStream() throws IOException {
            return socket.getInputStream();
        }

        /**
         * Type: Method.
         * Parent: NativeBluetoothSocket.
         * Name: getOutputStream.
         *
         * @return the output stream
         * @throws IOException the io exception
         * @Description.
         * @EndDescription.
         */
        @Override
        public OutputStream getOutputStream() throws IOException {
            return socket.getOutputStream();
        }

        /**
         * Type: Method.
         * Parent: NativeBluetoothSocket.
         * Name: getRemoteDeviceName.
         *
         * @return the remote device name
         * @Description.
         * @EndDescription.
         */
        @Override
        public String getRemoteDeviceName() {
            return socket.getRemoteDevice().getName();
        }

        /**
         * Type: Method.
         * Parent: NativeBluetoothSocket.
         * Name: connect.
         *
         * @throws IOException the io exception
         * @Description.
         * @EndDescription.
         */
        @Override
        public void connect() throws IOException {
            socket.connect();
        }

        /**
         * Type: Method.
         * Parent: NativeBluetoothSocket.
         * Name: getRemoteDeviceAddress.
         *
         * @return the remote device address
         * @Description.
         * @EndDescription.
         */
        @Override
        public String getRemoteDeviceAddress() {
            return socket.getRemoteDevice().getAddress();
        }

        /**
         * Type: Method.
         * Parent: NativeBluetoothSocket.
         * Name: close.
         *
         * @throws IOException the io exception
         * @Description.
         * @EndDescription.
         */
        @Override
        public void close() throws IOException {
            socket.close();
        }

        /**
         * Type: Method.
         * Parent: NativeBluetoothSocket.
         * Name: getUnderlyingSocket.
         *
         * @return the underlying socket
         * @Description.
         * @EndDescription.
         */
        @Override
        public BluetoothSocket getUnderlyingSocket() {
            return socket;
        }

    }

    /**
     * Type: Class.
     * Access: Public.
     * Name: FallbackBluetoothSocket.
     *
     * @Description.
     * @EndDescription.
     */
    public class FallbackBluetoothSocket extends NativeBluetoothSocket {

        /**
         * The Fallback socket.
         */
        private BluetoothSocket fallbackSocket;

        /**
         * Type: Method.
         * Parent: FallbackBluetoothSocket.
         * Name: FallbackBluetoothSocket.
         *
         * @param tmp @PsiType:BluetoothSocket.
         * @throws FallbackException the fallback exception
         * @Description.
         * @EndDescription.
         */
        public FallbackBluetoothSocket(BluetoothSocket tmp) throws FallbackException {
            super(tmp);
            try {
                Class<?> clazz = tmp.getRemoteDevice().getClass();
                Class<?>[] paramTypes = new Class<?>[]{Integer.TYPE};
                Method m = clazz.getMethod("createRfcommSocket", paramTypes);
                Object[] params = new Object[]{Integer.valueOf(1)};
                fallbackSocket = (BluetoothSocket) m.invoke(tmp.getRemoteDevice(), params);
            } catch (Exception e) {
                throw new FallbackException(e);
            }
        }

        /**
         * Type: Method.
         * Parent: FallbackBluetoothSocket.
         * Name: getInputStream.
         *
         * @return the input stream
         * @throws IOException the io exception
         * @Description.
         * @EndDescription.
         */
        @Override
        public InputStream getInputStream() throws IOException {
            return fallbackSocket.getInputStream();
        }

        /**
         * Type: Method.
         * Parent: FallbackBluetoothSocket.
         * Name: getOutputStream.
         *
         * @return the output stream
         * @throws IOException the io exception
         * @Description.
         * @EndDescription.
         */
        @Override
        public OutputStream getOutputStream() throws IOException {
            return fallbackSocket.getOutputStream();
        }


        /**
         * Type: Method.
         * Parent: FallbackBluetoothSocket.
         * Name: connect.
         *
         * @throws IOException the io exception
         * @Description.
         * @EndDescription.
         */
        @Override
        public void connect() throws IOException {
            fallbackSocket.connect();
        }


        /**
         * Type: Method.
         * Parent: FallbackBluetoothSocket.
         * Name: close.
         *
         * @throws IOException the io exception
         * @Description.
         * @EndDescription.
         */
        @Override
        public void close() throws IOException {
            fallbackSocket.close();
        }

    }

    /**
     * Type: Class.
     * Access: Public.
     * Name: FallbackException.
     *
     * @Description.
     * @EndDescription.
     */
    public static class FallbackException extends Exception {

        /**
         * The constant serialVersionUID.
         */
        private static final long serialVersionUID = 1L;

        /**
         * Type: Method.
         * Parent: FallbackException.
         * Name: FallbackException.
         *
         * @param e @PsiType:Exception.
         * @Description.
         * @EndDescription.
         */
        public FallbackException(Exception e) {
            super(e);
        }

    }

}
