package com.brainera.pseudonymizer

import com.ionspin.kotlin.bignum.integer.BigInteger
import com.ionspin.kotlin.bignum.integer.Sign
import org.kotlincrypto.hash.md.MD5
import kotlin.js.JsExport
import kotlin.js.JsName

@JsExport
class Pseudonymizer {
    private val md5 = MD5()

    @JsName("pseudonymize")
    fun pseudonymize(email: String): User {
        val hash = BigInteger.fromByteArray(
            source = md5.digest(email.encodeToByteArray()),
            sign = Sign.POSITIVE
        )
        val firstName = FirstNames[(hash % FirstNames.size).intValue()]
        val lastName = LastNames[(hash % LastNames.size).intValue()]

        return User(
            first = firstName,
            last = lastName,
            email = "${firstName.lowercase()}.${lastName.lowercase()}@company.com"
        )
    }
}

@JsExport
data class User(
    val first: String,
    val last: String,
    val email: String,
) {
    val fullName = "$first $last"
}
