package com.brainera.pseudonymizer

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class PseudonymizerTest {

    private val psuedonymizer = Pseudonymizer()

    @Test
    fun shouldHandleAnActualEmail() {
        val user = psuedonymizer.pseudonymize("joe.schmoe@realcompany.com")
        assertEquals(
            User(
                first = "Racek",
                last = "Asilova",
                email = "racek.asilova@company.com"
            ),
            user
        )
    }

    @Test
    fun shouldHandleZeroLengthEmails() = assertEquals(
        User(
            first = "Cottie",
            last = "Kabran",
            email = "cottie.kabran@company.com"
        ),
        psuedonymizer.pseudonymize("")
    )

    @Test
    fun shouldHandleLengthOneEmails() = CharRange('a', '9').forEach { validate(it.toString()) }

    @Test
    fun shouldHandleVeryLongEmails() = validate("a".repeat(10_000))

    @Test
    fun shouldHandleUnicodeGarbage() = validate(Random.Default.nextBytes(1_000).decodeToString())

    @Test
    fun shouldHandleALotOfStrings() {
        val alphabet = (0..127).map { it.toChar() }
        (1 .. 1_000).forEach { i ->
            validate((1 .. i).map { alphabet.random() }.toString())
        }
    }

    private fun validate(originalEmail: String) {
        val user = psuedonymizer.pseudonymize(originalEmail)
        val (first, last, email) = user
        assertEquals(
            "${first.lowercase()}.${last.lowercase()}@company.com",
            email
        )
        assertEquals("$first $last", user.fullName)
    }
}
