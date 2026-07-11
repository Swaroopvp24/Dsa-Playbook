// Try 1 to atempt1 file
class Solution {
    public int[] twoSum(int[] nums, int target) {
         HashMap<Integer, Integer> a = new HashMap<Integer, Integer>();
        for(int i=0; i<nums.length; i++){
            int compl = target - nums[i];
            if(a.containsKey(compl)){
                return new int[]{a.get(compl),i};
            }
            a.put(nums[i],i);
        }
        return new int[]{-1};
    }
}