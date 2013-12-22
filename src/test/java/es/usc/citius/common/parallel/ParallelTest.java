package es.usc.citius.common.parallel;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;


public class ParallelTest {
    @Test
    public void testForEach() throws Exception {
        Set<String> text = new HashSet<String>(Arrays.asList("0", "00", "000", "0000"));
        Collection<Integer> result = Parallel.ForEach(text, new Parallel.F<String, Integer>() {
            @Override
            public Integer apply(String s) {
                return s.length();
            }
        });

        int total = 0;
        for(Integer i : result) {total += i;}
        assertEquals(10, total);
    }

    @Test
    public void testFor() throws Exception {
        final AtomicInteger counter = new AtomicInteger(0);
        Parallel.For(0, 1000, new Parallel.Action<Long>() {
            @Override
            public void doAction(Long element) {
                counter.incrementAndGet();
            }
        });
        assertEquals(1000, counter.intValue());
    }

    private Iterable<Integer> iterable(final Integer size){
        return new Iterable<Integer>() {
             @Override
             public Iterator<Integer> iterator() {
                 return new Iterator<Integer>() {
                     int counter=0;
                     @Override
                     public boolean hasNext() {
                         return counter < size;
                     }

                     @Override
                     public Integer next() {
                         return counter++;
                     }

                     @Override
                     public void remove() {
                         throw new UnsupportedOperationException();
                     }
                 };
             }
        };
    }

    @Test
    public void testOneThread() throws ExecutionException, InterruptedException {
        final ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<Long, String>();
        new Parallel.ForEach(iterable(10000))
                .withFixedThreads(1)
                .apply(new Parallel.Action<Integer>() {
                    public void doAction(Integer element) {
                        map.put(Thread.currentThread().getId(), "");
                    }
                }).values();

        assertEquals(1, map.keySet().size());
    }

    @Test
    public void testFourThread() throws ExecutionException, InterruptedException {
        final ConcurrentHashMap<Long, String> map = new ConcurrentHashMap<Long, String>();
        new Parallel.ForEach(iterable(10000))
                .withFixedThreads(4)
                .apply(new Parallel.Action<Integer>() {
                    public void doAction(Integer element) {
                        map.put(Thread.currentThread().getId(), "");
                    }
                }).values();

        assertEquals(4, map.keySet().size());
    }
}
