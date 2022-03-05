package com.example.gblocalchat.LVL3Lesson6;

import java.util.Arrays;

public class ArrayCheck {


    public static void main(String[] args) {
    }


    public int[] convert(int[] arr) {
        int[] nums = null;
        for (int i = arr.length - 1; i > 0; i--) {
            if (arr[i] == 4) {
                nums = new int[arr.length - i - 1];
                System.arraycopy(arr, i + 1, nums, 0, arr.length - i - 1);
                break;
            }
        }
        if (nums == null) {
            throw new MyExeption("Массив не содержит ни одной 4!");
        }
        return nums;
    }

    public boolean checkArray(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == 1 || arr[i] == 4) {
                return true;
            }
        }
        return false;
    }
}
