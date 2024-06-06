package com.View.Dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.Verifone.VerifoneTaskManager
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.QuestionDialogLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
/**
 * Type: Class.
 * Access: Public.
 * Name: DialogQuestionTip.
 *
 * @Description.
 * @EndDescription.
 */
class DialogQuestionTip : DialogFragment(), CoroutineScope {

    private lateinit var questionDialogLayoutBinding: QuestionDialogLayoutBinding
    private var mFragmentManager: FragmentManager? = null

    private val job = Job()

    private var message = ""

    companion object {

        private const val ARG_MESSAGE = "message"

        fun newInstance(message: String): DialogQuestionTip {
            val args = Bundle()
            args.putString(ARG_MESSAGE, message)

            val fragment = DialogQuestionTip()
            fragment.arguments = args

            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { arguments ->

            this.message = arguments.getString(ARG_MESSAGE, "")

        } ?: run {
            Toast.makeText(context, "Error instantiating DialogQuestionTip", Toast.LENGTH_SHORT)
                .show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        questionDialogLayoutBinding =
            QuestionDialogLayoutBinding.inflate(inflater, container, false)
        return questionDialogLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFragmentManager = fragmentManager

        questionDialogLayoutBinding.dialogTitle.text = "Aviso"
        questionDialogLayoutBinding.dialogMessage.text = message

        questionDialogLayoutBinding.dialogButton.setOnClickListener {
            dismiss()
            UserInteraction.showInputDialog(fragmentManager, "6", "Estafeta")
        }

        questionDialogLayoutBinding.cancelarButton.setOnClickListener {
            ContentFragment.contentFragment!!.binding!!.buttonCobrar.isEnabled = true
            launch { VerifoneTaskManager.limpiarTerminal() }
            dismiss()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.let { dialog ->

            dialog.setCancelable(false)

            dialog.window?.let { window ->
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.attributes.windowAnimations = R.style.DialogTheme
            }

        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO
}