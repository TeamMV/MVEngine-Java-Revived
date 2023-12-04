package dev.mv.engine.utils;

import dev.mv.engine.utils.collection.FlatHashMap;
import dev.mv.engine.utils.collection.Vec;
import dev.mv.engine.utils.generic.triplet.Triplet;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static dev.mv.engine.utils.generic.Option.None;
import static dev.mv.engine.utils.generic.Option.Some;
import static dev.mv.engine.utils.generic.pair.Pair.Duo;

public class Test {

    public static final String[] ADJACENTS = {"0,8", "1,2,4", "1,2,3,5", "2,3,6", "1,4,5,7", "2,4,5,6,8", "3,5,6,9", "4,7,8", "0,5,7,8,9", "6,8,9"};

    private static final int ITERATIONS = 1000000;
    private static final int VALUE_ITERATIONS = 500;

    public static void main(String[] args) {
        HashMap<Integer, Integer> hashMap = new HashMap<>();
        FlatHashMap<Integer, Integer> flatHashMap = new FlatHashMap<>();

        // Prepare data for testing
        Random random = new Random();
        Integer[] keys = new Integer[ITERATIONS];
        Integer[] values = new Integer[ITERATIONS];
        for (int i = 0; i < ITERATIONS; i++) {
            keys[i] = random.nextInt();
            values[i] = random.nextInt();
        }

        // Benchmark add operation
        benchmarkOperation("Add", hashMap, flatHashMap, keys, values, Map::put);

        // Benchmark get operation
        benchmarkOperation("Get", hashMap, flatHashMap, keys, null, (map, key, value) -> map.get(key));

        // Benchmark containsKey operation
        benchmarkOperation("ContainsKey", hashMap, flatHashMap, keys, null, (map, key, value) -> map.containsKey(key));

        // Benchmark remove operation
        benchmarkOperation("Remove", hashMap, flatHashMap, keys, null, (map, key, value) -> map.remove(key));

        // Prepare data for containsValue test
        //Integer[] valueKeys = new Integer[VALUE_ITERATIONS];
        //for (int i = 0; i < VALUE_ITERATIONS; i++) {
        //    valueKeys[i] = random.nextInt();
        //}

        // Benchmark containsValue operation
        //benchmarkOperation("ContainsValue", hashMap, flatHashMap, valueKeys, null, (map, key, value) -> map.containsValue(key));
    }

    private static <K, V, M extends Map<K, V>> void benchmarkOperation(
        String operationName, M hashMap, M flatHashMap, K[] keys, V[] values,
        MapOperation<K, V, M> operation) {

        long startTime, elapsedTime;

        // Benchmark for HashMap
        startTime = System.nanoTime();
        for (int i = 0; i < keys.length; i++) {
            operation.perform(hashMap, keys[i], values == null ? null : values[i]);
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.println(operationName + " HashMap: " + elapsedTime + "ns");

        // Benchmark for FlatHashMap
        startTime = System.nanoTime();
        for (int i = 0; i < keys.length; i++) {
            operation.perform(flatHashMap, keys[i], values == null ? null : values[i]);
        }
        elapsedTime = System.nanoTime() - startTime;
        System.out.println(operationName + " FlatHashMap: " + elapsedTime + "ns");
    }

    @FunctionalInterface
    private interface MapOperation<K, V, M extends Map<K, V>> {
        void perform(M map, K key, V value);
    }

    public static Vec<String> getPins(String observed) {
        final Vec<String> pins = new Vec<>(ADJACENTS[observed.charAt(0) - '0'].split(","));
        new Vec<>(Utils.toObject(observed.toCharArray())).fastIter(1).map(c -> (int) c - '0').forEach(i -> {
            pins.resetData(pins.fastIter().flatMap(pin -> new Vec<>(ADJACENTS[i].split(",")).fastIter().map(digit -> pin + digit).collect()).collect());
        });
        return pins;
    }

    public static Vec<String> getPinsBetter(String observed) {
        return new Vec<>(Utils.toObject(observed.toCharArray()))
            .fastIter()
            .map(c -> ADJACENTS[c - '0'].split(","))
            .fold(new Vec<>(""), (acc, digits) ->
                acc
                    .fastIter()
                    .flatMap(pin ->
                        new Vec<>(digits).fastIter().map(digit -> pin + digit).collect()
                    ).collect()
            );
    }


    public static String toCamelCase(String s) {
        return new Vec<>(s.split("[_-]"))
            .fastIter()
            .map(s1 -> s1.substring(0, 1).toUpperCase() + s1.substring(1).toLowerCase())
            .collect()
            .splitFirst()
            .map((str, vec) -> str.toLowerCase() + vec.join((a, b) -> a + b));
    }

    public static String format(int seconds) {
        Vec<String> result = new Vec<>(
            Triplet.Trio("year", 31536000, 100000),
            Triplet.Trio("day", 86400, 365),
            Triplet.Trio("hour", 3600, 24),
            Triplet.Trio("minute", 60, 60),
            Triplet.Trio("second", 1, 60)
        ).fastIter()
            .map(trio -> Duo(seconds / trio.b % trio.c, trio.a))
            .filterMap(pair -> {
                if (pair.a == 1) return Some(pair.a + " " + pair.b);
                if (pair.a != 0) return Some(pair.a + " " + pair.b + "s");
                return None();
            })
            .collect(Vec::collect);

        return switch (result.size()) {
            case 0:
                yield "now";
            case 1:
                yield result.get(0);
            default:
                yield result.splitLast().map(pair -> pair.b.join((a, b) -> a + b, ", ") + " and " + pair.a);
        };
    }

}
