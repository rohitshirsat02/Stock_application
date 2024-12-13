package com.example.stock_quotes_app.controller;

/**
 * Recursion
 */
public class Recursion {

    public static int binarysearch(int arr[] , int left, int right, int target){

        if(left>right){
            return -1;

        }
        int mid = left+(right - left) / 2;
        if(arr[mid]==target){
            return mid;
        }

        if(target<arr[mid]){
            return binarysearch(arr, left, right, target);
        }
        return binarysearch(arr, left, right, target);
    
    }
    public static void main(String[] args) {
        int arr [] = {1,2,3,4,5,7,6,8};
        int target = 6;
        int result = binarysearch(arr,0, arr.length-1, target);
        System.out.println(result);
        if(result==-1){
            System.out.println("the element was not found");
        }
    }
}