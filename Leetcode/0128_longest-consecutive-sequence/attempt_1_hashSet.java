class Solution {
    public int longestConsecutive(int[] nums) {
        Set<Integer> un = new HashSet<>();

        for (int num : nums) {
            un.add(num);
        }
        int ans = 0;
        for (int num : un) {
            if (un.contains(num - 1))
                continue;
            else {
                int cur = num;
                int curC = 0;
                while (un.contains(cur++)) curC++;
                ans = Math.max(ans, curC);
            }
        }
        return ans;
    }
}
