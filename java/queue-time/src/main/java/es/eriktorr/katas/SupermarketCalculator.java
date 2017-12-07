package es.eriktorr.katas;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SupermarketCalculator {

    public int queueTime(int[] clients, int checkoutTillsCount) throws InterruptedException {
        BlockingQueue<Integer> queue = createQueue(clients);
        CheckoutTill[] checkoutTills = new CheckoutTill[checkoutTillsCount];
        for (int i = 0; i < checkoutTills.length; i++) {
            checkoutTills[i] = new CheckoutTill();
            if (!queue.isEmpty()) checkoutTills[i].nextClient(queue.take());
        }

        int waitingTime = 0;
        do {
            int minWaitingTime = Arrays.stream(checkoutTills)
                    .mapToInt(CheckoutTill::waitingTime)
                    .min().orElse(0);

            Arrays.stream(checkoutTills).forEach(checkoutTill -> checkoutTill.advance(minWaitingTime));

            Arrays.stream(checkoutTills).filter(checkoutTill -> checkoutTill.waitingTime() == 0)
                    .forEach(checkoutTill -> {
                        if (!queue.isEmpty()) {
                            try {
                                checkoutTill.nextClient(queue.take());
                            } catch (InterruptedException ignored) { }
                        }
                    });

            waitingTime += minWaitingTime;
        } while (!queue.isEmpty());

        waitingTime += Arrays.stream(checkoutTills)
                .mapToInt(CheckoutTill::waitingTime)
                .max().orElse(0);

        return waitingTime;
    }

    private BlockingQueue<Integer> createQueue(int[] clients) {
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(clients.length);
        Arrays.stream(clients).forEachOrdered(client -> {
            try {
                queue.put(client);
            } catch (InterruptedException ignored) { }
        });
        return queue;
    }

    private static class CheckoutTill {
        private int waitingTime = 0;

        void nextClient(int waitingTime) {
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
