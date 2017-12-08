package es.eriktorr.katas;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QueueTimeCalculator {

    public int queueTime(List<Integer> queue, int tillsCount) {
        Deque<Integer> deque = new ArrayDeque<>(queue);
        List<SelfCheckoutTill> tills = IntStream.range(0, tillsCount)
                .mapToObj(i -> aSelfCheckoutTill(deque))
                .collect(Collectors.toList());

        int queueTime = 0;

        do {
            int minQueueTime = tills.stream()
                    .mapToInt(SelfCheckoutTill::waitingTime)
                    .min().orElse(0);
            queueTime += minQueueTime;

            tills.forEach(till -> {
                till.advance(minQueueTime);
                if (till.waitingTime() == 0 && !deque.isEmpty()) till.waitingTime(deque.remove());
            });
        } while (!deque.isEmpty());

        queueTime += tills.stream()
                .mapToInt(SelfCheckoutTill::waitingTime)
                .max().orElse(0);

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
