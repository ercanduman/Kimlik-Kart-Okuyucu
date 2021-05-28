package ercanduman.cardreader.ui.fragments


import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import com.mobsandgeeks.saripaar.ValidationError
import com.mobsandgeeks.saripaar.Validator
import ercanduman.cardreader.R
import ercanduman.cardreader.ui.validators.DateRule
import ercanduman.cardreader.ui.validators.DocumentNumberRule
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_selection.*
import net.sf.scuba.data.Gender
import org.jmrtd.lds.icao.MRZInfo
import java.security.Security

class SelectionFragment : Fragment(R.layout.fragment_selection), Validator.ValidationListener {

    private var linearLayoutManual: LinearLayout? = null
    private var linearLayoutAutomatic: LinearLayout? = null
    private var appCompatEditTextDocumentNumber: AppCompatEditText? = null
    private var appCompatEditTextDocumentExpiration: AppCompatEditText? = null
    private var appCompatEditTextDateOfBirth: AppCompatEditText? = null
    private var buttonReadNFC: Button? = null

    private var mValidator: Validator? = null
    private var selectionFragmentListener: SelectionFragmentListener? = null
    var disposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        linearLayoutManual = view.findViewById(R.id.layoutManual)
        linearLayoutAutomatic = view.findViewById(R.id.layoutAutomatic)
        appCompatEditTextDocumentNumber = view.findViewById(R.id.documentNumber)
        appCompatEditTextDocumentExpiration = view.findViewById(R.id.documentExpiration)
        appCompatEditTextDateOfBirth = view.findViewById(R.id.documentDateOfBirth)
        buttonReadNFC = view.findViewById(R.id.buttonReadNfc)

        buttonReadNFC!!.setOnClickListener { validateFields() }

        mValidator = Validator(this)
        mValidator!!.setValidationListener(this)

        mValidator!!.put(appCompatEditTextDocumentNumber!!, DocumentNumberRule())
        mValidator!!.put(appCompatEditTextDocumentExpiration!!, DateRule())
        mValidator!!.put(appCompatEditTextDateOfBirth!!, DateRule())

        buttonDeleteCSCA?.setOnClickListener {
            val subscribe = cleanCSCAFolder()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { _ ->
                        Toast.makeText(requireContext(), "CSCA Folder deleted", Toast.LENGTH_SHORT).show()
                    }
            disposable.add(subscribe)
        }
    }

    private fun validateFields() {
        linearLayoutManual!!.visibility = View.VISIBLE
        linearLayoutAutomatic!!.visibility = View.GONE
        try {
            mValidator!!.removeRules(appCompatEditTextDocumentNumber!!)
            mValidator!!.removeRules(appCompatEditTextDocumentExpiration!!)
            mValidator!!.removeRules(appCompatEditTextDateOfBirth!!)

            mValidator!!.put(appCompatEditTextDocumentNumber!!, DocumentNumberRule())
            mValidator!!.put(appCompatEditTextDocumentExpiration!!, DateRule())
            mValidator!!.put(appCompatEditTextDateOfBirth!!, DateRule())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        mValidator!!.validate()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = activity
        if (activity is SelectionFragmentListener) {
            selectionFragmentListener = activity
        }
    }

    override fun onDetach() {
        selectionFragmentListener = null
        super.onDetach()

    }

    override fun onDestroyView() {

        if (!disposable.isDisposed) {
            disposable.dispose()
        }
        super.onDestroyView()
    }


    override fun onValidationSucceeded() {

        val documentNumber = appCompatEditTextDocumentNumber!!.text!!.toString()
        val dateOfBirth = appCompatEditTextDateOfBirth!!.text!!.toString()
        var documentExpiration = appCompatEditTextDocumentExpiration!!.text!!.toString()

        documentExpiration = documentExpiration.dropLast(1)
        val mrzInfo = MRZInfo("P",
                "ESP",
                "DUMMY",
                "DUMMY",
                documentNumber,
                "ESP",
                dateOfBirth,
                Gender.MALE,
                documentExpiration,
                "DUMMY"
        )
        if (selectionFragmentListener != null) {
            selectionFragmentListener!!.onPassportRead(mrzInfo)
        }
    }

    override fun onValidationFailed(errors: List<ValidationError>) {
        for (error in errors) {
            val view = error.view
            val message = error.getCollatedErrorMessage(context)

            // Display error messages ;)
            if (view is EditText) {
                view.error = message
            } else {
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////
    //
    //        Listener
    //
    ////////////////////////////////////////////////////////////////////////////////////////

    interface SelectionFragmentListener {
        fun onPassportRead(mrzInfo: MRZInfo)
    }


    ////////////////////////////////////////////////////////////////////////////////////////
    //
    //        Download Master List Spanish Certificates
    //
    ////////////////////////////////////////////////////////////////////////////////////////

    private fun cleanCSCAFolder(): Single<Boolean> {
        return Single.fromCallable {
            try {
                val downloadsFolder = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!
                val listFiles = downloadsFolder.listFiles()
                for (tempFile in listFiles) {
                    tempFile.delete()
                }
                downloadsFolder.listFiles()
                true
            } catch (e: java.lang.Exception) {
                false
            }
        }
    }

    companion object {
        private const val TAG = "SelectionFragment"

        init {
            Security.insertProviderAt(org.spongycastle.jce.provider.BouncyCastleProvider(), 1)
        }
    }
}
