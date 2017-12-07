package es.eriktorr.katas;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(JUnitPlatform.class)
public class SupermarketQueueTest {

    private SupermarketCalculator supermarketCalculator = new SupermarketCalculator();

    @TestFactory
    Stream<DynamicTest> queueWaitingTimes() {
        List<SupermarketQueue> inputList = Arrays.asList(
                new SupermarketQueue(new int[]{ 5, 3, 4 }, 1),
                new SupermarketQueue(new int[]{ 10, 2, 3, 3 }, 2),
                new SupermarketQueue(new int[]{ 2, 3, 10 }, 2)
        );
        List<Integer> outputList = Arrays.asList(12, 10, 12);

        return inputList.stream()
                .map(queue -> DynamicTest.dynamicTest("queueTime(" + queue + ")", () -> {
                    int id = inputList.indexOf(queue);
                    assertThat(supermarketCalculator.queueTime(queue.clients, queue.checkoutTills))
                            .isEqualTo(outputList.get(id));
                }));
    }

    private static class SupermarketQueue {
        private final int[] clients;
        private final int checkoutTills;

        private SupermarketQueue(int[] clients, int checkoutTills) {
            this.clients = clients;
            this.checkoutTills = checkoutTills;
        }

        @Override
        public String toString() {
            return "[" + toString(clients) + "], " + checkoutTills;
        }

        private String toString(int[] clients) {
            return Arrays.stream(clients).mapToObj(Integer::toString).collect(Collectors.joining(", "));
        }
    }

}
