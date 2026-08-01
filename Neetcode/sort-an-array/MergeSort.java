class Solution {
    public void merge(int[] nums, int l, int m, int h) {
        int i = l, j = m + 1, k = l;
        int[] copy = new int[nums.length];
        while (i <= m && j <= h) {
            if (nums[i] <= nums[j]) {
                copy[k++] = nums[i++];
            } else {
                copy[k++] = nums[j++];
            }
        }
        while (i <= m) {
            copy[k++] = nums[i++];
        }
        while (j <= h) {
            copy[k++] = nums[j++];
        }
        for (int a = l; a <= h; a++) {
            nums[a] = copy[a];
        }
    }
    public void mergeSort(int[] nums, int l, int h) {
        if (l < h) {
            int m = l + (h - l) / 2;
            mergeSort(nums, l, m);
            mergeSort(nums, m + 1, h);
            merge(nums, l, m, h);
        }
    }
    public int[] sortArray(int[] nums) {
        mergeSort(nums, 0, nums.length - 1);
        return nums;
    }
}