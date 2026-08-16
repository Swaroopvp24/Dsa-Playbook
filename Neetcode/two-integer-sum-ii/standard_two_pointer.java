class Solution {
    public int[] twoSum(int[] nums, int target) {
        int st = 0, en = nums.length - 1;
        while (st < en) {
            int sum = nums[st] + nums[en];
            if (sum == target)
                return new int[] {st + 1, en + 1};
            if (sum > target)
                en--;
            else
                st++;
        }
        return new int[] {-1, -1};
    }
}
