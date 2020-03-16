package com.android.id.peers.loans

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.SeekBar
import com.android.id.peers.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoanApplicationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loan_application)
        title = "Loan Application"

        var numberOfLoan = findViewById<SeekBar>(R.id.number_of_loan_seek_bar)
        numberOfLoan.max = 10
        numberOfLoan.incrementProgressBy(100)
//        numberOfLoan.setOnSeekBarChangeListener()
    }

//    var seek = seekBarChangeListener(){

//    }
}
