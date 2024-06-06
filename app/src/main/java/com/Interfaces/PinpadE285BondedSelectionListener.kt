package com.Interfaces

import android.bluetooth.BluetoothDevice

interface PinpadE285BondedSelectionListener {

    fun selectedBondedDevice(device: BluetoothDevice)

}