package top.bfylu.expired_object_reference;

import java.util.Arrays;
import java.util.EmptyStackException;

/**
 * Can you spot the "memory leak"?
 * @author bfy
 * @date 2018.3.15
 */
public class Stack {
    private Object[] elements;
    private int size = 0;
    private static final int DEFAULT_INITIAL_CAPACITY = 16;

    public Stack() {
        elements = new Object[DEFAULT_INITIAL_CAPACITY];
    }

    public void push(Object e) {
        ensureCapacity();
        elements[size++] = e;
        for (Object element : elements) {
            System.out.println(element);
        }
        pop();
    }

    public Object pop() {
        if (size == 0)
            throw new EmptyStackException();
        Object result = elements[--size];
        elements[size] = null; //Eliminate obsolete reference 消除过期引用
        return result;
    }

    /**
     * Ensure space for at least one more element, roughly
     * doubling the capacity each time the array needs to grow.
     */
    private void ensureCapacity() {
        if (elements.length == size)
            elements = Arrays.copyOf( elements, 2 * size + 1);
    }

    public static void main(String[] args) {
        Stack stack = new Stack();
        stack.push(20);
    }

}
