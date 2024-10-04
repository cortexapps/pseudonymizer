package com.brainera.pseudonymizer

import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

class PseudonymizerTest {

    private val psuedonymizer = Pseudonymizer()

    @Test
    fun shouldHandleZeroLengthEmails() = validate("")

    @Test
    fun shouldHandleLengthOneEmails() = CharRange('a', '9').forEach { validate(it.toString()) }

    @Test
    fun shouldHandleVeryLongEmails() = validate("a".repeat(10_000))

    @Test
    fun shouldHandleUnicodeGarbage() = validate(Random.Default.nextBytes(1_000).decodeToString())

    @Test
    fun shouldHandleALotOfStrings() {
        val alphabet = (0..127).map { it.toChar() }
        (1 .. 10_000).forEach { i ->
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
