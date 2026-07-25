class Solution {
    public void rotate(int[] nums, int k) {
        int n = nums.length;
        k = k % n;              // Important! Whenever you're moving around a circle (arrays, clocks, circular queues, etc.), use modulo because the pattern repeats after a fixed number of steps.

        int[] temp = new int[n];
        int i = 0, pt = n - k;

        while (pt < n) {
            temp[i++] = nums[pt++];
        }

        pt = 0;
        while (i < n) {
            temp[i++] = nums[pt++];
        }

        for (int j = 0; j < n; j++) {
            nums[j] = temp[j];
        }
    }
}