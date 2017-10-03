package org.walleth.tests

import android.os.Parcel
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.kethereum.model.Address
import org.kethereum.model.Transaction
import org.kethereum.model.createTransactionWithDefaults
import org.walleth.kethereum.android.TransactionParcel
import java.math.BigInteger

class TheTransactionParcel {

    val DEFAULT_NONCE = BigInteger("42")
    @Test
    fun normalTransactionSurvives() {

        val transactionBefore = createTransactionWithDefaults(value = BigInteger("10"), from = Address("0xab"), to = Address("0xcd"), nonce = DEFAULT_NONCE)
        testTransactionParcel(transactionBefore)
    }

    @Test
    fun customTransactionSurvives() {

        val transactionBefore = createTransactionWithDefaults(value = BigInteger("1000"), from = Address("0xab"), to = Address("0xcd"),
                creationEpochSecond = 10L,
                gasLimit = BigInteger("123"),
                gasPrice = BigInteger("123542"),
                input = listOf(1, 2, 3, 4, 2),
                txHash = "0xfoo")
        testTransactionParcel(transactionBefore)
    }

    @Test
    fun transactionWithNullFieldsSurvives() {

        val transactionBefore = createTransactionWithDefaults(value = BigInteger("0"), from = Address("0xab"), to = null)
        testTransactionParcel(transactionBefore)
    }

    private fun testTransactionParcel(transactionBefore: Transaction) {
        val transactionParcel = TransactionParcel(transactionBefore)

        val obtain = Parcel.obtain()
        transactionParcel.writeToParcel(obtain, 0)

        obtain.setDataPosition(0)

        val transactionAfter = TransactionParcel.createFromParcel(obtain).transaction

        assertThat(transactionAfter).isEqualTo(transactionBefore)
    }
}
