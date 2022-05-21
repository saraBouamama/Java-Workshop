package fr.epita.assistant.seq;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public interface Seq<TYPE> extends ExtendedStream<TYPE> {

    static <TYPE> Seq<TYPE> of(TYPE[] values) {
        return new Seq<TYPE>() {
            @Override
            public Stream<TYPE> delegate() {
                return Arrays.stream(values);
            }
        };
    }

    static <TYPE> Seq<TYPE> of(Stream<TYPE> values) {
        return new Seq<TYPE>() {
            @Override
            public Stream<TYPE> delegate() {
                return values;
            }
        };
    }

    Stream<TYPE> delegate();

    static <TYPE> Seq<TYPE> of(List<TYPE> list) {
        return new Seq() {
            @Override
            public Stream<TYPE> delegate() {
                return list.stream();
            }

            ;
        };
    }

    @Override
    default <KEY_TYPE> Map<KEY_TYPE, TYPE>
    toMap(final Function<TYPE, KEY_TYPE> keyMapper) {
        return toMap(new HashMap<>(), keyMapper, Function.identity());
    }

    @Override
    default <KEY_TYPE, VALUE_TYPE, MAP_TYPE extends Map<KEY_TYPE, VALUE_TYPE>> MAP_TYPE
    toMap(final MAP_TYPE map, final Function<TYPE, KEY_TYPE> keyMapper, final Function<TYPE, VALUE_TYPE> valueMapper) {
        delegate().forEach(e -> map.put(keyMapper.apply(e), valueMapper.apply(e)));
        return map;
    }

    @Override
    default <KEY_TYPE, VALUE_TYPE> Map<KEY_TYPE, VALUE_TYPE> toMap(final Function<TYPE, KEY_TYPE> keyMapper, final Function<TYPE, VALUE_TYPE> valueMapper) {
        return toMap(new HashMap<>(), keyMapper, valueMapper);
    }

    @Override
    default List<TYPE> toList() {
        return delegate().toList();
    }

    @Override
    default <LIST extends List<TYPE>> LIST toList(final LIST list) {
        delegate().forEach(list :: add);
        return list;
    }

    @Override
    default Set<TYPE> toSet() {
        var set = new HashSet<TYPE>();
        delegate().forEach(set::add);
        return set;
    }

    @Override
    default <SET extends Set<TYPE>> SET toSet(final SET set) {
        delegate().forEach(set::add);
        return set;
    }

    @Override
    default <ASSOCIATED_TYPE> ExtendedStream<Pair<TYPE, ASSOCIATED_TYPE>> associate(final Supplier<ASSOCIATED_TYPE> supplier) {
        var list = new ArrayList<Pair<TYPE, ASSOCIATED_TYPE>>();
        delegate().forEach(value -> list.add(new Pair<>(value, supplier.get())));
        return of(list);
    }

    @Override
    default <ASSOCIATED_TYPE> ExtendedStream<Pair<TYPE, ASSOCIATED_TYPE>> associate(final Stream<ASSOCIATED_TYPE> supplier) {
        var list = new ArrayList<Pair<TYPE, ASSOCIATED_TYPE>>();
        var supp = supplier.iterator();
        var del = delegate().iterator();
        while (del.hasNext() && supp.hasNext()) {
            list.add(new Pair<>(del.next(), supp.next()));
        }
        return of(list);
    }

    @Override
    default ExtendedStream<TYPE> print() {
        delegate().forEach(value -> System.out.println(value.toString()));
        return this;
    }

    @Override
    default ExtendedStream<TYPE> plus(final Stream<TYPE> stream) {
        return of(Stream.concat(delegate(), stream));
    }

    @Override
    default Object join(final String delimiter) {
        StringBuilder res = new StringBuilder();
        var it = delegate().iterator();
        if (it.hasNext())
            res.append(it.next().toString());
        while (it.hasNext()) {
            res.append(delimiter);
            res.append(it.next().toString());
        }
        return res.toString();
    }

    @Override
    default String join() {
        StringBuilder res = new StringBuilder();
        var it = delegate().iterator();
        while (it.hasNext())
            res.append(it.next().toString());
        return res.toString();
    }

    @Override
    default <KEY_TYPE> ExtendedStream<Pair<KEY_TYPE, ExtendedStream<TYPE>>> partition(final Function<TYPE, KEY_TYPE> pivot) {
        Map<KEY_TYPE, ArrayList<TYPE>> map = new HashMap<>();
        var it = delegate().iterator();
        while (it.hasNext()) {
            var value = it.next();
            var key = pivot.apply(value);
            if (!map.containsKey(key))
            {
                var list = new ArrayList<TYPE>();
                list.add(value);
                map.put(key, list);
            }
            else
                map.get(key).add(value);
        }

        var stream = new ArrayList<Pair<KEY_TYPE, ExtendedStream<TYPE>>>();
        map.forEach((key, list) -> stream.add(new Pair<>(key, of(list))));

        return of(stream);
    }

    @Override
    default Stream<TYPE> filter(Predicate<? super TYPE> predicate) {
        return delegate().filter(predicate);
    }

    @Override
    default <R> Stream<R> map(Function<? super TYPE, ? extends R> mapper) {
        return delegate().map(mapper);
    }

    @Override
    default IntStream mapToInt(ToIntFunction<? super TYPE> mapper) {
        return delegate().mapToInt(mapper);
    }

    @Override
    default LongStream mapToLong(ToLongFunction<? super TYPE> mapper) {
        return delegate().mapToLong(mapper);
    }

    @Override
    default DoubleStream mapToDouble(ToDoubleFunction<? super TYPE> mapper) {
        return delegate().mapToDouble(mapper);
    }

    @Override
    default <R> Stream<R> flatMap(Function<? super TYPE, ? extends Stream<? extends R>> mapper) {
        return delegate().flatMap(mapper);
    }

    @Override
    default IntStream flatMapToInt(Function<? super TYPE, ? extends IntStream> mapper) {
        return delegate().flatMapToInt(mapper);
    }

    @Override
    default LongStream flatMapToLong(Function<? super TYPE, ? extends LongStream> mapper) {
        return delegate().flatMapToLong(mapper);
    }

    @Override
    default DoubleStream flatMapToDouble(Function<? super TYPE, ? extends DoubleStream> mapper) {
        return delegate().flatMapToDouble(mapper);
    }

    @Override
    default Stream<TYPE> distinct() {
        return delegate().distinct();
    }

    @Override
    default Stream<TYPE> sorted() {
        return delegate().sorted();
    }

    @Override
    default Stream<TYPE> sorted(Comparator<? super TYPE> comparator) {
        return delegate().sorted(comparator);
    }

    @Override
    default Stream<TYPE> peek(Consumer<? super TYPE> action) {
        return delegate().peek(action);
    }

    @Override
    default Stream<TYPE> limit(long maxSize) {
        return delegate().limit(maxSize);
    }

    @Override
    default Stream<TYPE> skip(long n) {
        return delegate().skip(n);
    }

    @Override
    default void forEach(Consumer<? super TYPE> action) {
        delegate().forEach(action);
    }

    @Override
    default void forEachOrdered(Consumer<? super TYPE> action) {
        delegate().forEachOrdered(action);
    }

    @Override
    default Object[] toArray() {
        return delegate().toArray();
    }

    @Override
    default <A> A[] toArray(IntFunction<A[]> generator) {
        return delegate().toArray(generator);
    }

    @Override
    default TYPE reduce(TYPE identity, BinaryOperator<TYPE> accumulator) {
        return delegate().reduce(identity, accumulator);
    }

    @Override
    default Optional<TYPE> reduce(BinaryOperator<TYPE> accumulator) {
        return delegate().reduce(accumulator);
    }

    @Override
    default <U> U reduce(U identity, BiFunction<U, ? super TYPE, U> accumulator, BinaryOperator<U> combiner) {
        return delegate().reduce(identity, accumulator, combiner);
    }

    @Override
    default <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super TYPE> accumulator, BiConsumer<R, R> combiner) {
        return delegate().collect(supplier, accumulator, combiner);
    }

    @Override
    default <R, A> R collect(Collector<? super TYPE, A, R> collector) {
        return delegate().collect(collector);
    }

    @Override
    default Optional<TYPE> min(Comparator<? super TYPE> comparator) {
        return delegate().min(comparator);
    }

    @Override
    default Optional<TYPE> max(Comparator<? super TYPE> comparator) {
        return delegate().max(comparator);
    }

    @Override
    default long count() {
        return delegate().count();
    }

    @Override
    default boolean anyMatch(Predicate<? super TYPE> predicate) {
        return delegate().anyMatch(predicate);
    }

    @Override
    default boolean allMatch(Predicate<? super TYPE> predicate) {
        return delegate().allMatch(predicate);
    }

    @Override
    default boolean noneMatch(Predicate<? super TYPE> predicate) {
        return delegate().noneMatch(predicate);
    }

    @Override
    default Optional<TYPE> findFirst() {
        return delegate().findFirst();
    }

    @Override
    default Optional<TYPE> findAny() {
        return delegate().findAny();
    }

    @Override
    default Iterator<TYPE> iterator() {
        return delegate().iterator();
    }

    @Override
    default Spliterator<TYPE> spliterator() {
        return delegate().spliterator();
    }

    @Override
    default boolean isParallel() {
        return delegate().isParallel();
    }

    @Override
    default Stream<TYPE> sequential() {
        return delegate().sequential();
    }

    @Override
    default Stream<TYPE> parallel() {
        return delegate().parallel();
    }

    @Override
    default Stream<TYPE> unordered() {
        return delegate().unordered();
    }

    @Override
    default Stream<TYPE> onClose(Runnable closeHandler) {
        return delegate().onClose(closeHandler);
    }

    @Override
    default void close() {
        delegate().close();
    }
}