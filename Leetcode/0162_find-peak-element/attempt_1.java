class Solution {
    public int findPeakElement(int[] nums) {
        int l = 0, r = nums.length - 1;

        while (l <= r) {
            int m = l + (r - l) / 2;

            if (nums.length == 1) {
                return 0;
            }

            if (m == 0) {
                if (nums[m] > nums[m + 1])
                    return m;
                l = m + 1;
                continue;
            }

            if (m == nums.length - 1) {
                if (nums[m] > nums[m - 1])
                    return m;
                r = m - 1;
                continue;
            }

            if (nums[m] > nums[m + 1] && nums[m] > nums[m - 1]) {
                return m;
            } else if (nums[m + 1] > nums[m]) {
                l = m + 1;
            } else {
                r = m - 1;
            }
        }

        return -1;
    }
}
