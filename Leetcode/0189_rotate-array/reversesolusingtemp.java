class Solution {
    public void rev(int[] arr, int st, int en) {
        while (st < en) {
            int temp = arr[st];
            arr[st] = arr[en];
            arr[en] = temp;

            st++;
            en--;
        }
    }

    public void rotate(int[] nums, int k) {
        k = k % nums.length; //VERY VERY IMPORTANT
        rev(nums, 0, nums.length - 1); // rverses the whole array
        rev(nums, 0, k - 1); // reverses the first k ele
        rev(nums, k, nums.length - 1); //reverse the remaining
    }
}

/* We need to swap two contiguous blocks (A and B).
Reversing the entire array automatically swaps their positions.
The only problem is that both blocks become reversed.
Reversing each block again restores their original order.
*/