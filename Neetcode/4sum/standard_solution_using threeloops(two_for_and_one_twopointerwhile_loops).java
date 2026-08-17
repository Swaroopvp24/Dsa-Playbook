class Solution {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        int n = nums.length;
        List<List<Integer>> ans = new ArrayList<>();

        for (int firstInd = 0; firstInd < n; firstInd++) {
            if (firstInd > 0 && nums[firstInd] == nums[firstInd - 1]) {
                continue;
            }

            for (int secondInd = firstInd + 1; secondInd < n; secondInd++) {
                if (secondInd > firstInd + 1 && nums[secondInd] == nums[secondInd - 1]) {
                    continue;
                }

                int left = secondInd + 1;
                int right = n - 1;

                while (left < right) {
                    // Cast to long before addition to prevent integer overflow
                    long sum = (long) nums[firstInd] + nums[secondInd] + nums[left] + nums[right];

                    if (sum == target) {
                        ans.add(Arrays.asList(
                            nums[firstInd], nums[secondInd], nums[left], nums[right]));

                        left++;
                        right--;

                        // Skip duplicate values after finding a valid quadruplet
                        while (left < right && nums[left] == nums[left - 1]) left++;

                        while (left < right && nums[right] == nums[right + 1]) right--;

                    } else if (sum > target) {
                        right--;
                    } else {
                        left++;
                    }
                }
            }
        }

        return ans;
    }
}