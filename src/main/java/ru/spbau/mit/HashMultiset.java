package ru.spbau.mit;

import java.util.*;

public class HashMultiset<E> extends AbstractCollection<E> implements Multiset<E> {
    private HashMap<E, MultisetEntry<E>> multiElements = new LinkedHashMap<>();
    private Set<MultisetEntry<E>> setElements = new LinkedHashSet<>();
    private Set<E> elements = new LinkedHashSet<>();
    private int count = 0;

    @Override
    public boolean add(E e) {
        count += 1;
        if (!elements.contains(e)) {
            MultisetEntry<E> entry = new MultisetEntry<>(e);
            multiElements.put(e, entry);
            setElements.add(entry);
            elements.add(e);
        } else {
            multiElements.get(e).count += 1;
        }
        return true;
    }

    @Override
    public int count(Object element) {
        if (!multiElements.containsKey(element)) {
            return 0;
        }
        return multiElements.get(element).getCount();
    }

    @Override
    public Set<E> elementSet() {
        return Collections.unmodifiableSet(elements);
    }

    @Override
    public Set<? extends Entry<E>> entrySet() {
        return setElements;
    }

    @Override
    public Iterator<E> iterator() {
        return new MultisetIterator<>(setElements, this);
    }

    @Override
    public int size() {
        return count;
    }

//    private static final class MultiSet<E> extends AbstractSet<E>

    private static final class MultisetIterator<E> implements Iterator<E> {
        private HashMultiset<E> parent;
        private Iterator<MultisetEntry<E>> setIterator;
        private int curElement;
        private MultisetEntry<E> entry;

        private MultisetIterator(Set<MultisetEntry<E>> setElements, HashMultiset<E> parentObject) {
            parent = parentObject;
            setIterator = setElements.iterator();
        }

        @Override
        public boolean hasNext() {
            return setIterator.hasNext() || (entry != null && entry.getCount() > curElement);
        }

        @Override
        public E next() {
            if (entry == null || entry.getCount() <= curElement) {
                curElement = 1;
                entry = setIterator.next();
            } else {
                curElement += 1;
            }
            return entry.getElement();
        }

        @Override
        public void remove() {
            entry.count -= 1;
            curElement -= 1;
            if (entry.count == 0) {
                setIterator.remove();
                parent.setElements.remove(entry);
                parent.elements.remove(entry.getElement());
            }
            parent.count -= 1;
        }
    }

    private static class MultisetEntry<E> implements Entry<E> {
        private int count = 1;
        private E element;

        MultisetEntry(E o) {
            element = o;
        }

        @Override
        public E getElement() {
            return element;
        }

        @Override
        public int getCount() {
            return count;
        }
    }
}
