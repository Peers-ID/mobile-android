package id.peers

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import id.peers.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*
import java.util.*
import java.util.Calendar.MONTH
import java.util.Calendar.SHORT

class ForgotPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        val signIn = findViewById<TextView>(R.id.sign_in_button)
        val birthDate = findViewById<EditText>(R.id.birth_date)
        val submit = findViewById<Button>(R.id.submit)

        signIn.setOnClickListener { signIn(it) }
        birthDate.setOnFocusChangeListener { v, hasFocus ->  if(hasFocus) showDatePickerDialog(v) }
//        findViewById<EditText>(R.id.birth_date).setOnClickListener { showDatePickerDialog(it) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            birthDate.showSoftInputOnFocus = false
        } else {
            birthDate.setRawInputType(InputType.TYPE_CLASS_TEXT)
            birthDate.setTextIsSelectable(true)
        }
    }

    private fun signIn(view: View) {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePickerDialog(view: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            birth_date.setText("" + dayOfMonth + " " + c.getDisplayName(MONTH, SHORT, Locale.US) + ", " + year)
        }, year, month, day)

        dpd.show()
    }
}
