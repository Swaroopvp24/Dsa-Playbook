class Solution {
    public void swap(int[] nums, int a, int b) {
        int temp = nums[a];
        nums[a] = nums[b];
        nums[b] = temp;
    }
    public void printArr(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            System.out.print("" + arr[i] + " ");
        }
        System.out.println();
    }
    public int firstMissingPositive(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] <= 0 || nums[i] > nums.length) {
                continue;
            }
            while(nums[i] > 0 && nums[i] <= nums.length && nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
            }
        }

        for(int i=0; i<nums.length; i++){
            if(nums[i] != (i+1)) return i+1;
        }
        return nums.length+1;
    }
}