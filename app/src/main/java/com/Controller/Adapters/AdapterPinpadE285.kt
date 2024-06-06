package com.Controller.Adapters

import android.bluetooth.BluetoothDevice
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.Interfaces.PinpadE285SelectionListener
import com.innovacion.eks.beerws.databinding.PinpadItemBinding

class AdapterPinpadE285(
    var btDevices: MutableList<BluetoothDevice>,
    private val onClickListener: PinpadE285SelectionListener
) : RecyclerView.Adapter<AdapterPinpadE285.ViewHolderPinpadE285>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPinpadE285 {
        val binding = PinpadItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolderPinpadE285(binding, onClickListener)
    }

    override fun onBindViewHolder(holder: ViewHolderPinpadE285, position: Int) {
        val device = btDevices[position]
        holder.bind(device)
    }

    override fun getItemCount(): Int = btDevices.size

    class ViewHolderPinpadE285(
        private val itemBinding: PinpadItemBinding,
        private val onClickListener: PinpadE285SelectionListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(btDevice: BluetoothDevice) {

            itemBinding.constraintLayoutPinpad.setOnClickListener {
                onClickListener.onSelectedPinpadE285(
                    btDevice
                )
            }
            itemBinding.textViewPinpadName.text = btDevice.name
            itemBinding.textViewPinpadBTAddress.text = btDevice.address

        }

    }

}