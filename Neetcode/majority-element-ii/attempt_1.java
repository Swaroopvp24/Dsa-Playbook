class Solution {
    public List<Integer> majorityElement(int[] nums) {
        Map<Integer, Integer> count = new HashMap<>();

        for (int no : nums) {
            count.put(no, count.getOrDefault(no, 0) + 1);
        }

        List<Integer> res = new ArrayList<>();

        // System.out.println(nums.length / 3);
        // System.out.println(Math.floor(nums.length / 3));
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (entry.getValue() > Math.floor(nums.length / 3)) {
                res.add(entry.getKey());
            }
        }
        return res;
    }
}