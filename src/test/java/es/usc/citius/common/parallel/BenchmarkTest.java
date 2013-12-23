package es.usc.citius.common.parallel;


import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class BenchmarkTest {

    public float speedup(int minNumberSize, int numbersSize, int times){
        // Compute best sequential time
        List<Integer> numbers = generateNumbers(minNumberSize, numbersSize);
        long bestSeqTime = Long.MAX_VALUE;
        int primes = 0;
        for(int i=0; i<times; i++){
            long start = System.currentTimeMillis();
            primes = seqPrimeChecking(numbers);
            long time = System.currentTimeMillis() - start;
            bestSeqTime = Math.min(bestSeqTime, time);
        }

        // Compute best parallel time
        long bestParTime = Long.MAX_VALUE;
        for(int i=0; i<times; i++){
            long start = System.currentTimeMillis();
            primes = parallelPrimeChecking(numbers);
            long time = System.currentTimeMillis() - start;
            bestParTime = Math.min(bestParTime, time);
        }

        // Compute speedup
        if (bestParTime == 0) return 0;
        float speedup = (float)bestSeqTime / (float)bestParTime;
        System.out.println("Sequential vs Parallel = " + bestSeqTime + "/" + bestParTime + " ms. Speedup = " + speedup + ". Total primes: " + primes);
        return speedup;
    }

    @Test
    public void speedupTest(){
        if (Runtime.getRuntime().availableProcessors() == 1){
            return;
        }

        float sp;
        sp  = speedup(9999, 100000, 5);
        sp += speedup(99999, 10000, 5);
        sp += speedup(999999, 1000, 5);
        sp += speedup(9999999, 100, 5);
        sp += speedup(99999999, 10, 5);
        sp /= 5;
        System.out.println("Processors: " + Runtime.getRuntime().availableProcessors() + ". Average speedup in this machine: " + sp);
        assertTrue(sp >= 1);
    }

    public int seqPrimeChecking(List<Integer> numbers){
        int totalPrimes = 0;
        for(Integer i : numbers){
            if (isPrime(i)){
                totalPrimes++;
            }
        }
        return totalPrimes;
    }

    public int parallelPrimeChecking(List<Integer> numbers){
        Collection<Boolean> primes = Parallel.ForEach(numbers, new Parallel.F<Integer, Boolean>() {
            @Override
            public Boolean apply(Integer number) {
                return isPrime(number);
            }
        });
        primes.removeAll(Arrays.asList(false));
        return primes.size();
    }

    private static boolean isPrime(int n) {
        // Inefficient prime checking
        for(int i = 2; i < n; ++i) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }


    private List<Integer> generateNumbers(int minNumberSize, int size){
        List<Integer> numbers = new ArrayList<Integer>(size);
        for(int i=0; i < size; i++){
            numbers.add(minNumberSize+i);
        }
        return numbers;
    }

    private List<Integer> generateRandomNumbers(int minNumberSize, int size){
        Random r = new Random();
        List<Integer> numbers = new ArrayList<Integer>(size);
        for(int i=0; i < size; i++){
            numbers.add(r.nextInt(minNumberSize) + (9999999/size) );
        }
        return numbers;
    }
}
