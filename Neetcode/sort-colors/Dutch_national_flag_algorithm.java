class Solution {
    public void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }
    public void sortColors(int[] nums) {
        int l = 0, m = 0, h = nums.length - 1;
        while (m <= h) {
            if (nums[m] == 0) {
                swap(nums, l, m);
                l++; // move low
                m++; // move mid
            } else if (nums[m] == 2) {
                swap(nums, m, h);
                h--; // move high in
                // the element swapped is unkown so dont move mid
            }else{
                m++; //if its already 1 , just move mid
            }
        }
    }
}