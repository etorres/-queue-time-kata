package es.eriktorr.katas;

import kotlin.Unit;
import kotlin.ranges.IntRange;

import java.util.*;

import static kotlin.collections.CollectionsKt.*;

class QueueTimeCalculator {

    int queueTime(List<Integer> queue, int tillsCount) {
        Deque<Integer> deque = new ArrayDeque<>(queue);
        List<SelfCheckoutTill> tills = mapTo(new IntRange(0, tillsCount - 1), new ArrayList<>(), i -> aSelfCheckoutTill(deque));

        int queueTime = 0;

        do {
            List<Integer> waitingTimes = mapTo(tills, new ArrayList<>(), SelfCheckoutTill::waitingTime);
            int minQueueTime = Optional.ofNullable(min(waitingTimes)).orElse(0);
            queueTime += minQueueTime;

            forEach(tills, till -> {
                till.advance(minQueueTime);
                if (till.waitingTime() == 0 && !deque.isEmpty()) till.waitingTime(deque.remove());
                return Unit.INSTANCE;
            });
        } while (!deque.isEmpty());

        List<Integer> remainingWaitingTimes = mapTo(tills, new ArrayList<>(), SelfCheckoutTill::waitingTime);
        queueTime += Optional.ofNullable(max(remainingWaitingTimes)).orElse(0);

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