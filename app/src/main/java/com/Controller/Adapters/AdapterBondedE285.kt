package com.Controller.Adapters

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Interfaces.PinpadE285BondedSelectionListener
import com.innovacion.eks.beerws.databinding.BondedPinpadItemBinding

class AdapterBondedE285(var btDevices: MutableList<BluetoothDevice>, private val onClickListener: PinpadE285BondedSelectionListener) :
    RecyclerView.Adapter<AdapterBondedE285.ViewHolderBondedE285>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBondedE285 {
        val binding = BondedPinpadItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderBondedE285(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolderBondedE285, position: Int) {
        val device = btDevices[position]
        holder.bind(device)
    }

    override fun getItemCount(): Int = btDevices.size

    class ViewHolderBondedE285(
        private val itemBinding: BondedPinpadItemBinding,
        private val onClickListener: PinpadE285BondedSelectionListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(btDevice: BluetoothDevice) {

            itemBinding.constraintLayoutPinpad.setOnClickListener {
                onClickListener.selectedBondedDevice(
                    btDevice
                )
            }
            itemBinding.textViewPinpadName.text = btDevice.name
            itemBinding.textViewPinpadBTAddress.text = btDevice.address

        }

    }

}