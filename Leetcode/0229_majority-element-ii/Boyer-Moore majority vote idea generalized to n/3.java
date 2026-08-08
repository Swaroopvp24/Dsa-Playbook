class Solution {
    public List<Integer> majorityElement(int[] nums) {
        int ct1 = 0, cand1 = 0, ct2 = 0, cand2 = 0;
        for (int n : nums) {
            if (n == cand1)
                ct1++;
            else if (n == cand2)
                ct2++;
            else if (ct1 == 0) {
                cand1 = n;
                ct1++;
            } else if (ct2 == 0) {
                cand2 = n;
                ct2++;
            } else {
                ct1--;
                ct2--;
            }
        }

        ct1 = 0;
        ct2 = 0;
        for (int n : nums) {
            if (cand1 == n)
                ct1++;
            else if (cand2 == n)
                ct2++;
        }

        List<Integer> res = new ArrayList<>();

        if (ct1 > nums.length / 3)
            res.add(cand1);
        if (ct2 > nums.length / 3)
            res.add(cand2);

        return res;
    }
}