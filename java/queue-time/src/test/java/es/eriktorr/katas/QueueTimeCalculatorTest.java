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
public class QueueTimeCalculatorTest {

    private QueueTimeCalculator queueTimeCalculator = new QueueTimeCalculator();

    @TestFactory
    Stream<DynamicTest> queueWaitingTimes() {
        List<SelfSelfCheckoutTills> inputList = Arrays.asList(
                new SelfSelfCheckoutTills(Arrays.asList(5, 3, 4), 1),
                new SelfSelfCheckoutTills(Arrays.asList(10, 2, 3, 3), 2),
                new SelfSelfCheckoutTills(Arrays.asList(2, 3, 10), 2)
        );
        List<Integer> outputList = Arrays.asList(12, 10, 12);

        return inputList.stream()
                .map(selfCheckoutTill -> DynamicTest.dynamicTest("queueTime(" + selfCheckoutTill + ")", () -> {
                    int id = inputList.indexOf(selfCheckoutTill);
                    assertThat(queueTimeCalculator.queueTime(selfCheckoutTill.queue, selfCheckoutTill.tillsCount))
                            .isEqualTo(outputList.get(id));
                }));
    }

    private static class SelfSelfCheckoutTills {
        private final List<Integer> queue;
        private final int tillsCount;

        private SelfSelfCheckoutTills(List<Integer> queue, int tillsCount) {
            this.queue = queue;
            this.tillsCount = tillsCount;
        }

        @Override
        public String toString() {
            return "[" + toString(queue) + "], " + tillsCount;
        }

        private String toString(List<Integer> queue) {
            return queue.stream().map(Object::toString).collect(Collectors.joining(", "));
        }
    }

}
