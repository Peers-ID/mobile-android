package com.android.id.peers.loans.models

import android.content.SharedPreferences

class LoanFormulaConfig {
    var id: Int = 0
    var formulaName: String = ""
    var minLoanAmount: Int = 0
    var maxLoanAmount: Int = 0
    var kelipatan: Int = 0
    var minTenure: Int = 1
    var maxTenure: Int = 1
    var tenureCycle: String = "tahun"
    var serviceType: String = "fixed"
    var serviceAmount: Long = 0
    var serviceCycle: String = "tahun"

    companion object {
        fun saveLoanFormula(configPreferences: SharedPreferences, result: LoanFormulaConfig) {
            configPreferences.edit()
                .putInt("id", result.id)
                .putString("formula_name", result.formulaName)
                .putInt("min_loan_amount", result.minLoanAmount)
                .putInt("max_loan_amount", result.maxLoanAmount)
                .putInt("kelipatan", result.kelipatan)
                .putInt("min_tenure", result.minTenure)
                .putInt("max_tenure", result.maxTenure)
                .putString("tenure_cycle", result.tenureCycle)
                .putString("service_type", result.serviceType)
                .putLong("service_amount", result.serviceAmount)
                .putString("service_cycle", result.serviceCycle)
                .apply()
        }
    }
}