package com.pthw.mymovieapp.utils

import android.content.Context
import android.content.DialogInterface
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.pthw.mymovieapp.R
import com.pthw.mymovieapp.base.BaseFragment
import com.pthw.mymovieapp.utils.fastadapter.SmartScrollListener
import com.pthw.mymovieapp.vos.GenericDomainVO
import com.pthw.mymovieapp.vos.MetaVO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt

fun String.isOneChar(): Boolean = this.length == 1

var i: Int = 2

fun BaseFragment.finish() {
    this.findNavController().navigateUp()
}

fun NavDirections.navigate(fragment: BaseFragment) {
    fragment.findNavController().navigate(this)
}

fun String.toMMNumber() = this.replace("([0-9])".toRegex()) {
    (it.groupValues[1][0].toInt() + 4112).toChar().toString()
}

fun Int.toMMNumber() = "$this".toMMNumber()

fun <T> List<T>.toMetaVO(currentPage: Int?, totalPage: Int?): MetaVO<T> {
    return MetaVO(
        this, currentPage ?: 1, totalPage ?: 0
    )
}

fun DataEmptyResponse.toDomain(): GenericDomainVO {
    return GenericDomainVO(
        this.errorMessage.orEmpty()
    )
}

fun Int.isFirstPage(): Boolean = this == 1

fun RecyclerView.applySmartScrollListener(listener: SmartScrollListener.OnSmartScrollListener) {
    this.addOnScrollListener(SmartScrollListener(listener))
}


fun ImageView.show(imageUrl: String) {
    Glide.with(this)

        .load(imageUrl)
        .apply(
            RequestOptions.fitCenterTransform()
                .error(R.drawable.placeholder)
        )
        .into(this)
}

fun ImageView.load(
    resource: Any,
    error: Int = R.drawable.placeholder,
    placeholder: Int = R.drawable.placeholder,
    centerCrop: Boolean = false
) {

    val options = if (centerCrop) RequestOptions().placeholder(placeholder).error(error).centerCrop()
    else RequestOptions().placeholder(placeholder).error(error)

    Glide.with(this.context)
        .load(resource)
        .apply(options)
        .into(this)
}

fun View.toGone() {
    this.visibility = View.GONE
}

fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toInVisible() {
    this.visibility = View.INVISIBLE
}

fun AppCompatActivity.toast(message: String) {
    Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
}

fun Fragment.toast(message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.preventDoubleClick() {
    this.isEnabled = false
    this.postDelayed({ this.isEnabled = true }, 1000)
}

//fun Context.autoConvert(str: String): String {
//    return if (!MMFontUtils.isSupportUnicode()) {
//        MMFontUtils.uni2zg(str)
//    } else str
//}

fun Int.thousandSeparator(): String {
    return String.format("%,d", this)
}

fun Double.thousandSeparator(): String {
    return if (this == 0.0) "0"
    else String.format("%,d", this.roundToInt())
}

fun View.showDialogWithOk(
    title: String,
    message: String,
    okButtonText: String,
    okButton: (DialogInterface) -> Unit
) {

    val dialogBuilder = AlertDialog.Builder(context)

    dialogBuilder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton(okButtonText, DialogInterface.OnClickListener { dialog, id ->
            okButton(dialog)
        })

    val alert = dialogBuilder.create()
    alert.setTitle(title)
    alert.show()

}



fun Fragment.showDialogWithOk(
    title: String,
    message: String,
    okButtonText: String,
    okButton: (DialogInterface) -> Unit
) {

    val dialogBuilder = AlertDialog.Builder(context!!)

    dialogBuilder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton(okButtonText, DialogInterface.OnClickListener { dialog, id ->
            okButton(dialog)
        })

    val alert = dialogBuilder.create()
    alert.setTitle(title)
    alert.show()

}


fun AppCompatActivity.showDialogWithOkAndCancel(
    title: String,
    message: String,
    okButtonText: String,
    cancelButtonText: String,
    okButton: (DialogInterface) -> Unit,
    cancel: (DialogInterface) -> Unit
) {

    val dialogBuilder = AlertDialog.Builder(this)

    val okBtnText = okButtonText
    val cancelBtnText = cancelButtonText
    val okBtn = okBtnText
    val cancel = cancelBtnText


    dialogBuilder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton(okBtn, DialogInterface.OnClickListener { dialog, id ->
            okButton(dialog)
        })
        .setNegativeButton(cancel, DialogInterface.OnClickListener { dialog, id ->
            cancel(dialog)
        })

    val alert = dialogBuilder.create()
    alert.setTitle(title)
    alert.show()

}

fun Fragment.showDialogWithOkAndCancel(
    title: String,
    message: String,
    okButtonText: String,
    cancelButtonText: String,
    okButton: (DialogInterface) -> Unit,
    cancel: (DialogInterface) -> Unit
) {

    val dialogBuilder = AlertDialog.Builder(requireContext())

    val okBtnText = okButtonText
    val cancelBtnText = cancelButtonText
    val okBtn = okBtnText
    val cancel = cancelBtnText


    dialogBuilder.setMessage(message)
        .setCancelable(false)
        .setPositiveButton(okBtn, DialogInterface.OnClickListener { dialog, id ->
            okButton(dialog)
        })
        .setNegativeButton(cancel, DialogInterface.OnClickListener { dialog, id ->
            cancel(dialog)
        })

    val alert = dialogBuilder.create()
    alert.setTitle(title)
    alert.show()

}


fun View.preventDoubleClick(delay: Long = 1000) {
    this.isEnabled = false
    this.postDelayed({ this.isEnabled = true }, delay)
}

fun EditText.afterTextChangedDelayed(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        var timer: CountDownTimer? = null

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(editable: Editable?) {
            timer?.cancel()
            timer = object : CountDownTimer(700, 1000) {
                override fun onTick(millisUntilFinished: Long) {}
                override fun onFinish() {
                    afterTextChanged.invoke(editable.toString())
                }
            }.start()
        }
    })
}

fun <T1, T2, T3, R> zip(
    first: Flow<T1>,
    second: Flow<T2>,
    third: Flow<T3>,
    transform: suspend (T1, T2, T3) -> R
): Flow<R> =
    first.zip(second) { a, b -> a to b }
        .zip(third) { (a, b), c ->
            transform(a, b, c)
        }