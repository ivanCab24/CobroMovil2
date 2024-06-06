package com.View.Dialogs

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.DI.BaseApplication
import com.Interfaces.DialogWaitingFilesMVP
import com.View.UserInteraction
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.WaitDialogFilesBinding
import javax.inject.Inject
/**
 * Type: Class.
 * Access: Public.
 * Name: DialogWaitingFiles.
 *
 * @Description.
 * @EndDescription.
 */
class DialogWaitingFiles : DialogFragment(), DialogWaitingFilesMVP.View {

    private lateinit var binding: WaitDialogFilesBinding

    @Inject
    lateinit var presenter: DialogWaitingFilesMVP.Presenter

    companion object {

        fun newInstance(): DialogWaitingFiles {

            val fragment = DialogWaitingFiles()

            return fragment

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WaitDialogFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(700, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).plusUploadFilesSubComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.setView(this)

        with(binding.progressBarFiles) {

            isIndeterminate = false
            progress = 0
            max = 100

        }

        presenter.doUploadFiles()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog?.let { dialog ->
            isCancelable = false
            dialog.window?.let { window ->
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.attributes.windowAnimations = R.style.DialogTheme
            }
        }

    }

    //================================================================================
    // Inicia Presenter DialogWaitingFiles
    //================================================================================
    override fun updateProgressUploadFiles(text: String, progress: Int) {

        with(binding) {

            dialogMessage.text = text
            progressBarFiles.progress = progress

        }

    }

    override fun onExceptionUploadFiles(onException: String) {
        UserInteraction.snackyException(activity, null, onException)
        dismiss()
    }

    override fun onSuccesUploadFiles(onSuccess: String) {
        UserInteraction.snackySuccess(activity, null, onSuccess)
        dismiss()
    }

    //================================================================================
    // Termina Presenter DialogWaitingFiles
    //================================================================================

}