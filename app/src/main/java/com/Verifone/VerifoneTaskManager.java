/*
 * *
 *  * Created by Gerardo Ruiz on 12/16/20 4:41 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 12/13/20 1:06 AM
 *
 */

package com.Verifone;


import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.Constants.ConstantsPreferences;
import com.Presenter.PinpadTaskPresenter;
import com.Utilities.Utilities;
import com.Utilities.Utils;
import com.View.Fragments.ContentFragment;
import com.webservicestasks.ToksWebServiceErrors;
import com.webservicestasks.ToksWebServiceErrorsDescriptions;
import com.webservicestasks.ToksWebServicesConnection;


/**
 * Type: Class.
 * Access: Public.
 * Name: VerifoneTaskManager.
 *
 * @Description.
 * @EndDescription.
 */
public class VerifoneTaskManager extends AppCompatActivity implements ToksWebServicesConnection, ToksWebServiceErrors, ToksWebServiceErrorsDescriptions, VerifonePinpadInterface {

    /**
     * The constant TAG.
     */
    private static final String TAG = "IngenicoTaskManager";

    /**
     * Type: Method.
     * Parent: VerifoneTaskManager.
     * Name: VerifoneTaskManager.
     *
     * @Description.
     * @EndDescription.
     */
    public VerifoneTaskManager() {
    }

    /**
     * Type: Method.
     * Parent: VerifoneTaskManager.
     * Name: restPinpad.
     *
     * @Description.
     * @EndDescription.
     */
    public static void restPinpad() {

        byte[] chDataOut;
        byte[] chDataIn = new byte[1024];
        byte[] chAck = {0x06};

        chDataOut = MSG_72;

        BtManager btManager = ContentFragment.contentFragment.btManager;

        if (btManager != null) {

            Log.i(TAG, "restPinpad: ");

            btManager.BtSendData(chDataOut);
            btManager.BtRecvData(chDataIn, 30000);
            chDataOut = MSG_Z2;
            btManager.BtSendData(chDataOut);
            btManager.BtRecvData(chDataIn, 30000);

            //ENVIO ACK DE RECIBIDO
            btManager.BtSendData(chAck);

        }

    }


    public static void sendACK2(BtManager btManager ) {

        byte[] chDataOut;
        byte[] chDataIn = new byte[1024];
        byte[] chAck = {0x06};

        chDataOut = MSG_72;

        if (btManager != null) {
            Log.d("sssss","pruba ack");
            btManager.BtSendData(chAck);
            //ENVIO ACK DE RECIBIDO

        }

    }

    /**
     * Type: Method.
     * Parent: VerifoneTaskManager.
     * Name: restPinpad.
     *
     * @param btManager @PsiType:BtManager.
     * @Description.
     * @EndDescription.
     */
    public static void restPinpad(BtManager btManager) {

        byte[] chDataOut;
        byte[] chDataIn = new byte[1024];
        byte[] chAck = {0x06};

        chDataOut = MSG_72;

        if (btManager != null) {

            btManager.BtSendData(chDataOut);
            btManager.BtRecvData(chDataIn, 30000);
            chDataOut = MSG_Z2;
            btManager.BtSendData(chDataOut);
            btManager.BtRecvData(chDataIn, 30000);

            //ENVIO ACK DE RECIBIDO
            btManager.BtSendData(chAck);

        }

    }

    /**
     * Type: Method.
     * Parent: VerifoneTaskManager.
     * Name: desconectaPinpad.
     *
     * @Description.
     * @EndDescription.
     */
    public static void desconectaPinpad() {
        BtManager btManager = ContentFragment.contentFragment.btManager;
        if (btManager != null) {
            Log.i(TAG, "desconectaPinpad: ");
            btManager.BtCLose();
            btManager = null;
        }
    }

    public static void sendACK() {
        BtManager btManager = ContentFragment.contentFragment.btManager;
        btManager.BtSendData(ACK);
    }

    /**
     * Type: Method.
     * Parent: VerifoneTaskManager.
     * Name: desconectaPinpad.
     *
     * @param btManager @PsiType:BtManager.
     * @Description.
     * @EndDescription.
     */
    public static void desconectaPinpad(BtManager btManager) {

        if (btManager != null) {
            if (btManager.isBtConected()) {

                btManager.BtCLose();
                btManager = null;
                //Toast.makeText(this, "BT Desconectado", Toast.LENGTH_SHORT).show();

            }
        }
    }

    /**
     * Type: Method.
     * Parent: VerifoneTaskManager.
     * Name: cancelaVenta.
     *
     * @Description.
     * @EndDescription.
     */
    public static void cancelaVenta() {

        byte[] chDataIn = new byte[1024];
        byte[] chAck = {0x06};
        BtManager btManager = ContentFragment.contentFragment.btManager;

        if (btManager != null) {
            Log.i(TAG, "cancelaVenta: ");
            btManager.BtSendData(MSG_C54_ABORTED);
            btManager.BtRecvData(chDataIn, 30000);
            btManager.BtSendData(chAck);
        }
    }

    /**
     * Type: Method.
     * Parent: VerifoneTaskManager.
     * Name: limpiarVenta.
     *
     * @Description.
     * @EndDescription.
     */
        public static void limpiarVenta() {

        cancelaVenta();
        restPinpad();
        desconectaPinpad();

    }

    /**
     * Type: Method.
     * Parent: VerifoneTaskManager.
     * Name: limpiarTerminal.
     *
     * @Description.
     * @EndDescription.
     */
    public static void limpiarTerminal() {

        restPinpad();
        desconectaPinpad();

    }

    /**
     * Type: Method.
     * Parent: VerifoneTaskManager.
     * Name: limpiarTerminal.
     *
     * @param btManager @PsiType:BtManager.
     * @Description.
     * @EndDescription.
     */
    public static void limpiarTerminal(BtManager btManager) {

        restPinpad(btManager);
        desconectaPinpad(btManager);

    }

}
