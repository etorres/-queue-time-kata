package es.eriktorr.katas

import java.util.*

class QueueTimeCalculator {

    fun queueTime(queue: List<Int>, tillsCount: Int): Int {
        val deque = ArrayDeque<Int>(queue)
        val tills = generateSequence(0) { it + 1 }.take(tillsCount)
                .map { _ -> aSelfCheckoutTill(deque)}
                .toList()

        var queueTime = 0

        do {
            val minQueueTime = tills.minBy { it.waitingTime }!!.waitingTime
            queueTime += minQueueTime

            tills.forEach { it ->
                it.advance(minQueueTime)
                if (it.waitingTime == 0 && !deque.isEmpty()) it.waitingTime = deque.remove()
            }
        } while (!deque.isEmpty())

        queueTime += tills.maxBy { it.waitingTime }!!.waitingTime

        return queueTime
    }

    private fun aSelfCheckoutTill(deque: ArrayDeque<Int>): SelfCheckoutTill {
        return SelfCheckoutTill(if (!deque.isEmpty()) deque.remove() else 0)
    }

    private data class SelfCheckoutTill(var waitingTime: Int = 0) {
        fun advance(value: Int) {
            waitingTime -= value
        }
    }

}