package com.android.id.peers.loans.models

class LoanFormulaConfig {
    var id: Int = 0
    lateinit var formulaName: String
    var minLoanAmount: Int = 0
    var maxLoanAmount: Int = 0
    var kelipatan: Int = 0
    var minTenure: Int = 0
    var maxTenure: Int = 0
    lateinit var tenureCycle: String
    lateinit var serviceType: String
    var serviceAmount: Long = 0
    lateinit var serviceCycle: String
}