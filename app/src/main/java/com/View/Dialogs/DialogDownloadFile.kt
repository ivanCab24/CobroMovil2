package com.View.Dialogs

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.DI.BaseApplication
import com.Interfaces.DownloadMVP
import com.View.UserInteraction
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.WaitDialogFilesBinding
import javax.inject.Inject
/**
 * Type: Class.
 * Access: Public.
 * Name: DialogDownloadFile.
 *
 * @Description.
 * @EndDescription.
 */
class DialogDownloadFile : DialogFragment(), DownloadMVP.View {

    private val TAG = "DialogDownloadFile"
    private lateinit var binding: WaitDialogFilesBinding
    private var downloadCompleteReceiver: BroadcastReceiver? = null
    private var downloadManager: DownloadManager? = null
    private var idDowload: Long? = null

    @Inject
    lateinit var presenter: DownloadMVP.Presenter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WaitDialogFilesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).plusDownloadFileSubComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            dialogTitle.text = "Actualizando"
            dialogMessage.text = "Descargando: 0%"
            buttonReintentar.setOnClickListener {
                updateUI(true)
                tryDownload()
            }
        }
        initDI()
        tryDownload()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.let { dialog ->
            dialog.window?.let { window ->
                window.attributes.windowAnimations = R.style.DialogTheme
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }

    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(700, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    //================================================================================
    // General methods
    //================================================================================
    private fun tryDownload() {

        if (dialog != null) {
            Log.i(TAG, "tryDownload: NO null")
        }
        isCancelable = false
        binding.dialogMessage.text = "Descargando: 0%"
        presenter.executeDonwload(activity as FragmentActivity)

    }

    private fun initDI() {
        presenter.setView(this)
    }

    private fun borrarReceiver(downloadCompleteReceiver: BroadcastReceiver) {

        try {
            context?.unregisterReceiver(downloadCompleteReceiver)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }

    }

    private fun cancelDownloadManagerWork(downloadManager: DownloadManager, idDownload: Long) {
        downloadManager.remove(idDownload)
    }

    private fun updateUI(isUpdating: Boolean) {

        with(binding) {
            progressBarFiles.visibility = if (isUpdating) View.VISIBLE else View.INVISIBLE
            buttonReintentar.visibility = if (!isUpdating) View.VISIBLE else View.GONE
        }

        dialog?.setCancelable(!isUpdating)

    }

    //================================================================================
    // Inicia Presenter DownloadFile
    //================================================================================

    override fun onExceptionDownload(exception: String) {
        downloadCompleteReceiver?.let {
            borrarReceiver(it)
        }
        downloadManager?.let { downloadManager ->
            idDowload?.let { id ->
                cancelDownloadManagerWork(downloadManager, id)
            }
        }
        updateUI(false)
        binding.dialogMessage.text = "Presione el botón de reintentar"
        UserInteraction.snackyException(activity!!, null, exception)

    }

    override fun updateProgress(progress: Int) {
        binding.progressBarFiles.progress = progress
    }

    override fun updateText(text: String) {
        binding.dialogMessage.text = "Descargando: $text"
    }

    override fun setReceiver(downloadCompleteReceiver: BroadcastReceiver) {
        this.downloadCompleteReceiver = downloadCompleteReceiver
    }

    override fun setDownloadManager(downloadManager: DownloadManager, idDownload: Long) {
        this.downloadManager = downloadManager
        this.idDowload = idDownload
    }

    override fun onSuccessDonwload() {
        UserInteraction.snackySuccess(activity, null, "Descarga completada")
        dismiss()
    }
}