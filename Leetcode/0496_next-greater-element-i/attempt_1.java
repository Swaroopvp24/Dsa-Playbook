class Solution {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
       Stack<Integer> s = new Stack<>();
       Map<Integer , Integer> m = new HashMap<>();
       int[] res = new int[nums1.length];

       for(int i=0; i<nums2.length; i++){
        int num =nums2[i];
            while (!s.isEmpty() && s.peek() < num) {
                m.put(s.pop(), num);
            }
            s.push(num);
       }
       for(int i=0; i<nums1.length; i++){
        res[i] = m.getOrDefault(nums1[i],-1);
       }
       return res;
    }
}