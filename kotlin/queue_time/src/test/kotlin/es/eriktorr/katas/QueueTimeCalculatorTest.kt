package es.eriktorr.katas

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class QueueTimeCalculatorTest {

    private val queueTimeCalculator = QueueTimeCalculator()

    @TestFactory
    fun queueTimeInSelfCheckoutTills() = listOf(
            SelfCheckoutTills(listOf(5, 3, 4), 1) to 12,
            SelfCheckoutTills(listOf(10, 2, 3, 3), 2) to 10,
            SelfCheckoutTills(listOf(2, 3, 10), 2) to 12
    ).map { (selfCheckoutTill, queueTime) ->
        DynamicTest.dynamicTest("queueTime($selfCheckoutTill) => $queueTime") {
            assertThat(queueTimeCalculator.queueTime(selfCheckoutTill.queue, selfCheckoutTill.tillsCount)).isEqualTo(queueTime)
        }
    }

    private data class SelfCheckoutTills(val queue: List<Int>, val tillsCount: Int) {
        override fun toString(): String {
            return "[" + queue.joinToString(", ") + "], " + tillsCount
        }
    }

}