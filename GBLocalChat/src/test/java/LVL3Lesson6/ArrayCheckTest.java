package LVL3Lesson6;

import com.example.gblocalchat.LVL3Lesson6.ArrayCheck;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ArrayCheckTest {
    public ArrayCheck test = new ArrayCheck();

    @Test
    public void convertTest() {
        int[] ints = {1, 2, 3, 4, 5, 6};
        int[] ints1 = {5, 6};
        Assertions.assertArrayEquals(ints1, test.convert(ints));
    }

    @Test
    public void convertTest1() {
        int[] ints = {4, 5, 4, 6, 4, 7, 8, 9, 10};
        int[] ints1 = {7, 8, 9, 10};
        Assertions.assertArrayEquals(ints1, test.convert(ints));
    }

    @Test
    public void convertTest2() {
        int[] ints = {4, 5, 4, 6, 4, 7, 8, 9, 4, 10};
        int[] ints1 = {10};
        Assertions.assertArrayEquals(ints1, test.convert(ints));
    }

    //Будет наше исключение так как нету ни одной 4.
    @Test
    public void convertTest3() {
        int[] ints = {2, 5, 1, 6, 3, 7, 8, 9, 5, 10};
        int[] ints1 = {10};
        Assertions.assertArrayEquals(ints1, test.convert(ints));
    }

    @Test
    public void checkArrayTest() {
        int[] ints = {2, 5, 1, 6, 3, 7, 8, 9, 5, 10};
        final boolean bool = test.checkArray(ints);
        Assertions.assertTrue(bool);
    }

    @Test
    public void checkArrayTest1() {
        int[] ints = {2, 5, 6, 3, 7, 8, 9, 5, 10};
        final boolean bool = test.checkArray(ints);
        Assertions.assertFalse(bool);
    }

    @Test
    public void checkArrayTest2() {
        int[] ints = {10};
        final boolean bool = test.checkArray(ints);
        Assertions.assertFalse(bool);
    }

    @Test
    public void checkArrayTest3() {
        int[] ints = {1};
        final boolean bool = test.checkArray(ints);
        Assertions.assertTrue(bool);
    }
}