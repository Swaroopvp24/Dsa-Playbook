class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);

        List<List<Integer>> result = new ArrayList<>();

        for (int fixedIndex = 0; fixedIndex < nums.length; fixedIndex++) {

            // Skip duplicate fixed values
            if (fixedIndex > 0 && nums[fixedIndex] == nums[fixedIndex - 1]) {
                continue;
            }

            int left = fixedIndex + 1;
            int right = nums.length - 1;

            while (left < right) {
                int sum = nums[fixedIndex] + nums[left] + nums[right];

                if (sum == 0) {
                    result.add(Arrays.asList(
                        nums[fixedIndex],
                        nums[left],
                        nums[right]
                    ));

                    left++;
                    right--;

                    // Skip duplicate left values
                    while (left < right && nums[left] == nums[left - 1]) {
                        left++;
                    }

                    // Skip duplicate right values
                    while (left < right && nums[right] == nums[right + 1]) {
                        right--;
                    }

                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }

        return result;
    }
}