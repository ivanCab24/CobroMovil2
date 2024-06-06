package com.View.Dialogs

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.Constants.ConstantsAComer
import com.Constants.ConstantsMarcas
import com.Constants.ConstantsPreferences
import com.DI.BaseApplication
import com.Interfaces.DialogRedencionCuponListener
import com.Interfaces.RedencionCuponMVP
import com.Utilities.Files
import com.Utilities.PreferenceHelper
import com.Utilities.PreferenceHelperLogs
import com.View.Fragments.ContentFragment
import com.View.UserInteraction
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.innovacion.eks.beerws.R
import com.innovacion.eks.beerws.databinding.CustomDialogRedencionCuponBinding
import javax.inject.Inject
import javax.inject.Named

/**
 * Type: Class.
 * Access: Public.
 * Name: DialogRedencionCupon.
 *
 * @Description.
 * @EndDescription.
 */
class DialogRedencionCupon : DialogFragment, RedencionCuponMVP.View {
    /**
     * The Binding.
     */
    var binding: CustomDialogRedencionCuponBinding? = null
    private val doubleBounce = DoubleBounce()
    private var dialogRedencionCuponListener: DialogRedencionCuponListener? = null
    private var title = ""
    private var mensaje = ""
    private var fragmentManager2: FragmentManager? = null

    /**
     * The Shared preferences.
     */
    @JvmField
    @Named("Preferencias")
    @Inject
    var sharedPreferences: SharedPreferences? = null

    /**
     * The Preference helper.
     */
    @JvmField
    @Inject
    var preferenceHelper: PreferenceHelper? = null

    /**
     * The Preference helper logs.
     */
    @JvmField
    @Inject
    var preferenceHelperLogs: PreferenceHelperLogs? = null

    /**
     * The Files.
     */
    @JvmField
    @Inject
    var files: Files? = null

    /**
     * The Redencion cupon presenter.
     */
    @JvmField
    @Inject
    var presenter: RedencionCuponMVP.Presenter? = null

    /**
     * Type: Method.
     * Parent: DialogRedencionCupon.
     * Name: DialogRedencionCupon.
     *
     * @Description.
     * @EndDescription.
     */
    constructor() {
        //Required empty constructor
    }

    /**
     * Type: Method.
     * Parent: DialogRedencionCupon.
     * Name: DialogRedencionCupon.
     *
     * @param dialogRedencionCuponListener @PsiType:DialogRedencionCuponListener.
     * @param title                        @PsiType:String.
     * @param mensaje                      @PsiType:String.
     * @Description.
     * @EndDescription.
     */
    constructor(
        dialogRedencionCuponListener: DialogRedencionCuponListener?,
        title: String,
        mensaje: String
    ) {
        this.dialogRedencionCuponListener = dialogRedencionCuponListener
        this.title = title
        this.mensaje = mensaje
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CustomDialogRedencionCuponBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as BaseApplication).plusAcomerClubSubComponent().inject(this)
        if (preferenceHelper!!.getString(ConstantsPreferences.PREF_MARCA_SELECCIONADA, null) == ConstantsMarcas.MARCA_BEER_FACTORY) {
            doubleBounce.color = context.resources.getColor(R.color.doradoBeer)
        } else {
            doubleBounce.color = context.resources.getColor(R.color.white)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDI()
        fragmentManager2 = getFragmentManager()
        binding!!.progressBarAfiliado.indeterminateDrawable = doubleBounce
        binding!!.dialogTitle.text = title
        binding!!.textViewMessage.text = mensaje
        binding!!.btnRedimirPuntos.setOnClickListener {
            if (ContentFragment.cuponMiembro != null) {
                if (ContentFragment.copiaMiembro!!.response.membresia!!.numeroMembresia == ConstantsAComer.MEMBRESIA_SUBTIPO_INAPAM) {
                    binding!!.progressBarAfiliado.visibility = View.VISIBLE
                    binding!!.btnRedimirPuntos.isEnabled = false
                    presenter!!.executeRedimeCupon(null)
                } else {
                    UserInteraction.showNipAComerDialog(
                        fragmentManager2,
                        ContentFragment.copiaMiembro!!.response.membresia!!.numeroMembresia,
                        object: DialogInputNipAComer.DialogButtonClickListener {
                            override fun onPositiveButton(nip: String?) {
                                binding!!.progressBarAfiliado.visibility = View.VISIBLE
                                binding!!.btnRedimirPuntos.isEnabled = false
                                presenter!!.executeRedimeCupon(nip)
                            }
                        })
                }
            }
        }
        binding!!.buttonCancelar.setOnClickListener {
            dismiss()
            dialogRedencionCuponListener!!.onCancelSelected()
        }
    }

    private fun initDI() {
        presenter!!.setView(this)
        presenter!!.setPreferences(sharedPreferences, preferenceHelper)
        presenter!!.setLogsInfo(preferenceHelperLogs, files)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.window!!.attributes.windowAnimations = R.style.DialogTheme
        dialog!!.setCancelable(false)
    }

    //================================================================================
    // Inicia Presenter RedencionCupon
    //================================================================================
    override fun onExceptionRedencionCuponResult(onExceptionResult: String?) {
        binding!!.progressBarAfiliado.visibility = View.INVISIBLE
        UserInteraction.snackyException(activity, null, onExceptionResult)
        binding!!.btnRedimirPuntos.isEnabled = true
    }

    override fun onFailRedencionCuponResult(onFailResult: String?) {
        binding!!.progressBarAfiliado.visibility = View.INVISIBLE
        UserInteraction.snackyFail(activity, null, onFailResult)
        binding!!.btnRedimirPuntos.isEnabled = true
    }

    override fun onSuccessRedencionCuponResult(onSuccessResult: String?) {
        ContentFragment.cuponMiembro = null
        ContentFragment.copiaMiembro = null
        UserInteraction.snackySuccess(activity, null, onSuccessResult)
    }

    override fun onSuccessRedencionCuponDescuentoResult() {
        dismiss()
        ContentFragment.contentFragment!!.cuenta
    } //================================================================================
    // Termina Presenter RedencionCupon
    //================================================================================
}