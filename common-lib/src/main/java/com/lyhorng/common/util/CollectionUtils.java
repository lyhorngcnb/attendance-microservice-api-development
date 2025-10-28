package com.lyhorng.common.util;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class for collection operations
 */
public class CollectionUtils {

    private CollectionUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Check if collection is null or empty
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * Check if collection is not empty
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    /**
     * Check if map is null or empty
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * Check if map is not empty
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        return !isEmpty(map);
    }

    /**
     * Get first element from collection
     */
    public static <T> Optional<T> first(Collection<T> collection) {
        return isEmpty(collection) ? Optional.empty() : Optional.ofNullable(collection.iterator().next());
    }

    /**
     * Get last element from list
     */
    public static <T> Optional<T> last(List<T> list) {
        return isEmpty(list) ? Optional.empty() : Optional.ofNullable(list.get(list.size() - 1));
    }

    /**
     * Convert collection to list
     */
    public static <T> List<T> toList(Collection<T> collection) {
        return isEmpty(collection) ? new ArrayList<>() : new ArrayList<>(collection);
    }

    /**
     * Convert collection to set
     */
    public static <T> Set<T> toSet(Collection<T> collection) {
        return isEmpty(collection) ? new HashSet<>() : new HashSet<>(collection);
    }

    /**
     * Filter collection
     */
    public static <T> List<T> filter(Collection<T> collection, Predicate<T> predicate) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Map collection to another type
     */
    public static <T, R> List<R> map(Collection<T> collection, Function<T, R> mapper) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    /**
     * Group by key
     */
    public static <T, K> Map<K, List<T>> groupBy(Collection<T> collection, Function<T, K> keyExtractor) {
        if (isEmpty(collection)) {
            return new HashMap<>();
        }
        return collection.stream()
                .collect(Collectors.groupingBy(keyExtractor));
    }

    /**
     * Partition collection
     */
    public static <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        if (isEmpty(list)) {
            return partitions;
        }
        
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        
        return partitions;
    }

    /**
     * Get distinct elements
     */
    public static <T> List<T> distinct(Collection<T> collection) {
        if (isEmpty(collection)) {
            return new ArrayList<>();
        }
        return collection.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Flatten collection of collections
     */
    public static <T> List<T> flatten(Collection<? extends Collection<T>> collections) {
        if (isEmpty(collections)) {
            return new ArrayList<>();
        }
        return collections.stream()
                .filter(Objects::nonNull)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }
}

