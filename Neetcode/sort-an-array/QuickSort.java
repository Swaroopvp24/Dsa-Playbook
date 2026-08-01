class Solution {
    public void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    public int partition(int[] nums, int l, int h) {
        int piv = nums[l];
        int i = l, j = h;
        while (i <= j) {
            while (i <= h && nums[i] <= piv) i++;
            while (j >= l && nums[j] > piv) j--;
            if (i < j)
                swap(nums, i, j);
        }
        swap(nums, l, j);
        return j;
    }
    public void quickSort(int[] nums, int l, int h) {
        if (l < h) {
            int part = partition(nums, l, h);
            quickSort(nums, l, part - 1);
            quickSort(nums, part + 1, h);
        }
    }
    public int[] sortArray(int[] nums) {
        quickSort(nums,0,nums.length - 1);
        return nums;
    }
}