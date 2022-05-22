package com.pthw.mymovieapp.base

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.pthw.mymovieapp.R
import com.pthw.mymovieapp.utils.AppConstants

abstract class BaseFragment : Fragment {
    protected val DEFAULT_POST_PONE_DURATION = 500L

    constructor() : super()

    constructor(contentLayoutId: Int) : super(contentLayoutId)

    private var loadingDialog: AlertDialog? = null
    private lateinit var errorToastView: View

    /*  @Inject
      lateinit var analytics: AnalyticsHelper*/

    /*@Inject
    lateinit var dialogManager: DialogManager*/


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }


    open val disabledScreenshot: Boolean = false

    override fun onStart() {
        super.onStart()
        if (disabledScreenshot) {
            requireActivity().window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
    }

    override fun onStop() {
        if (disabledScreenshot) {
            requireActivity().window.clearFlags(
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
        super.onStop()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.filterTouchesWhenObscured = true
    }


    /* fun buildDialog(
         title: String = "",
         message: String,
         iconRes: Int? = null,
         isCancelable: Boolean = true,
         positiveAction: Pair<String, () -> Unit>,
         negativeAction: (Pair<String, () -> Unit>)? = null
     ): DialogFragment {
         return dialogManager.buildDialog(
             title, message, iconRes, isCancelable, positiveAction, negativeAction
         )
     }

     fun hideDialog() {
         try {
             dialogManager.hideDialog()
         } catch (e: Exception) {
             Timber.e(e, "Error hidingDialog at $className")
         }
     }*/

    private val loginDialogWidth by lazy { getScreenWidth(requireContext(), 0.25) }

    fun getScreenWidth(context: Context, percentage: Double): Int {
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        return (displayMetrics.widthPixels * percentage).toInt()
    }

    private val loadingView: View by lazy {
        LayoutInflater.from(requireContext()).inflate(R.layout.loading, null)
    }

    fun showLoadingDialog() {
        if (loadingDialog == null) {
            loadingDialog =
                createCustomDialog(requireContext(), loadingView, Gravity.CENTER, false)
        }
        modifyWindowsParamsAndShow(
            loadingDialog!!,
            loginDialogWidth,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    fun hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog!!.isShowing)
            loadingDialog?.dismiss()
    }

    /**Method for creating dialogs with custom views
     * @param view -> view inflated with desired layout
     * @param gravity -> gravity of the dialog to be displayed
     * @param cancelable -> whether the dialog is cancelable or not by touching
     * outside of the dialog
     */
    fun createCustomDialog(
        context: Context?,
        view: View?,
        gravity: Int,
        cancelable: Boolean
    ): AlertDialog {
        val dialog = AlertDialog.Builder(context).create()
        dialog.setView(view)
        val window = dialog.window
        window?.setGravity(gravity)
        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val layoutParams = window.attributes
        layoutParams.y = 40 // bottom margin
        window.attributes = layoutParams
        dialog.setCancelable(cancelable)
        return dialog
    }

    /**Method for modifying and overriding alertDialogs width and height as default width and height
     *  is almost 95% */
    fun modifyWindowsParamsAndShow(dialog: AlertDialog, width: Int, height: Int) {
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()
//        dialog.window?.attributes = lWindowParams
        dialog.window!!.setLayout(width, height)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loadingDialog?.cancel()
    }

    override fun onDestroy() {
        // hideDialog()
        super.onDestroy()
        toastErrorView = null
    }

    var toastErrorView: View? = null

    fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }

    fun showErrorToast() {
        Toast(requireContext()).apply {
            setGravity(Gravity.TOP, 0, 40)
            view = toastErrorView
            duration = Toast.LENGTH_SHORT
        }.show()
    }

    val imageUrl = AppConstants.IMAGE_URL

    fun popBackStack() {
        findNavController().popBackStack()
    }


    val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1000
    val GET_IMAGE = 10000
    protected fun selectUserImageFromGallery() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
            )

            return
        }

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(
            Intent.createChooser(intent, getString(R.string.lbl_select_user_profile_image)),
            GET_IMAGE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty()) {

            when (requestCode) {
                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE -> {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        selectUserImageFromGallery()
                    }
                }
            }

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GET_IMAGE) run {
                if (data != null && data.data != null) {
                    val selectedImageUri = data.data
                    onImageSelected(selectedImageUri)
                } else {
                    Toast.makeText(
                        requireContext(),
                        resources.getString(R.string.error_photo_uri_null),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

    }

    open fun onImageSelected(selectedImageUri: Uri?) {

    }


}