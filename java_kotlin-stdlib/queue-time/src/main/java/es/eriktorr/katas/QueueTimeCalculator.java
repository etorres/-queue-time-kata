package es.eriktorr.katas;

import kotlin.Unit;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static kotlin.collections.CollectionsKt.*;
import static kotlin.sequences.SequencesKt.asIterable;
import static kotlin.sequences.SequencesKt.generateSequence;

class QueueTimeCalculator {

    int queueTime(List<Integer> queue, int tillsCount) {
        Deque<Integer> deque = new ArrayDeque<>(queue);
        List<SelfCheckoutTill> tills = mapTo(
                take(asIterable(generateSequence(0, i -> i + 1)), tillsCount),
                new ArrayList<>(),
                i -> aSelfCheckoutTill(deque)
        );

        int queueTime = 0;

        do {
            int minQueueTime = requireNonNull(minBy(tills, SelfCheckoutTill::waitingTime)).waitingTime;
            queueTime += minQueueTime;

            forEach(tills, till -> {
                till.advance(minQueueTime);
                if (till.waitingTime() == 0 && !deque.isEmpty()) till.waitingTime(deque.remove());
                return Unit.INSTANCE;
            });
        } while (!deque.isEmpty());

        queueTime += requireNonNull(maxBy(tills, SelfCheckoutTill::waitingTime)).waitingTime;

        return queueTime;
    }

    private SelfCheckoutTill aSelfCheckoutTill(Deque<Integer> deque) {
        SelfCheckoutTill till = new SelfCheckoutTill();
        if (!deque.isEmpty()) till.waitingTime(deque.remove());
        return till;
    }

    private static class SelfCheckoutTill {
        private int waitingTime = 0;

        void waitingTime(int waitingTime) {
            this.waitingTime = waitingTime;
        }

        int waitingTime() {
            return waitingTime;
        }

        void advance(int time) {
            waitingTime -= time;
        }
    }

}