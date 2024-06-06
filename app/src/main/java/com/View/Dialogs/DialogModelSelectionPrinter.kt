package com.View.Dialogs

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.Constants.ConstantsIntents
import com.Utilities.PreferenceHelper
import com.View.Activities.BixolonPrinterActivity
import com.View.UserInteraction
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.DialogSelectPrinterBinding
import javax.inject.Inject
/**
 * Type: Class.
 * Access: Public.
 * Name: DialogModelSelectionPrinter.
 *
 * @Description.
 * @EndDescription.
 */
class DialogModelSelectionPrinter : DialogFragment() {

    lateinit var binding: DialogSelectPrinterBinding

    private var banderaActivity = ""

    @Inject
    lateinit var preferenceHelper: PreferenceHelper

    companion object {
        const val ARG_BANDERA_ACTIVITY = "banderaActivity"


        fun newInstance(
            banderaActivity: String,
        ): DialogModelSelectionPrinter {
            val args = Bundle()
            args.putString(ARG_BANDERA_ACTIVITY, banderaActivity)

            val fragment = DialogModelSelectionPrinter()
            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogSelectPrinterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            rdEpson.setOnClickListener {
                UserInteraction.showDialogBTPrinter(
                    fragmentManager
                )
            }

            rdBixolon.setOnClickListener {
                val intent = Intent(activity, BixolonPrinterActivity::class.java).apply {
                    putExtra(ConstantsIntents.INTENT_PINPAD, banderaActivity)
                }

                startActivity(intent)
            }

        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.let { it ->
            it.window?.let {
                it.attributes.windowAnimations = R.style.DialogTheme
                it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //dialog?.window?.setLayout(550, 130)
    }

}