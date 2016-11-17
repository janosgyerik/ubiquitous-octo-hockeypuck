package com.janosgyerik.tools.util;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public final class IterTools {

    private IterTools() {
        throw new AssertionError("utility class, forbidden constructor");
    }

    public static <T> Iterable<List<T>> permutations(List<T> list) {
        return () -> new PermutationIterator<>(list);
    }

    public static Iterable<List<Integer>> permutations(int n) {
        List<Integer> nums = IntStream.rangeClosed(1, n).boxed().collect(Collectors.toList());
        return permutations(nums);
    }

    private static class PermutationIterator<T> implements Iterator<List<T>> {
        private final List<T> list;
        private final int size;
        private final int maxCount;
        private final int[] indexes;

        int count = 0;

        PermutationIterator(List<T> list) {
            this.list = list;
            this.size = list.size();
            int factorial = factorial(size);
            maxCount = factorial >= size ? factorial : Integer.MAX_VALUE;
            indexes = createInitialIndexes();
        }

        private int[] createInitialIndexes() {
            return IntStream.range(0, size).toArray();
        }

        @Override
        public boolean hasNext() {
            return count < maxCount;
        }

        @Override
        public List<T> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            List<T> current = new ArrayList<>(size);
            for (int index : indexes) {
                current.add(list.get(index));
            }

            if (++count < maxCount) {
                updateIndexes();
            }

            return current;
        }

        private void updateIndexes() {
            int i = indexes.length - 2;
            for (; i >= 0; --i) {
                if (indexes[i] < indexes[i + 1]) {
                    break;
                }
            }
            int j = indexes.length - 1;
            for (; ; j--) {
                if (indexes[j] > indexes[i]) {
                    break;
                }
            }

            swap(i, j);

            int half = (indexes.length - i) / 2;
            for (int k = 1; k <= half; ++k) {
                swap(i + k, indexes.length - k);
            }
        }

        private void swap(int i, int j) {
            int tmp = indexes[i];
            indexes[i] = indexes[j];
            indexes[j] = tmp;
        }
    }

    private static int factorial(int n) {
        if (n < 2) {
            return 1;
        }
        return n * factorial(n - 1);
    }

    public static <T> List<T> toList(Iterator<T> iterator) {
        List<T> list = new ArrayList<>();
        toList(iterator, list);
        return list;
    }

    public static <T> void toList(Iterator<T> iterator, List<T> list) {
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
    }

    public static <T> Set<List<T>> toSet(Iterable<List<T>> permutations) {
        return StreamSupport.stream(permutations.spliterator(), false).collect(Collectors.toSet());
    }
}
