class Solution {
    List<List<Integer>> res;
    public void comb(int idx, int target, List<Integer> arr, int[] c) {
        if (target == 0) {
            res.add(new ArrayList<>(arr));
            return;
        }
        else if (target < 0 || idx >= c.length)
            return;
        arr.add(c[idx]);
        comb(idx, target - c[idx], arr, c);
        // comb(idx + 1, target - c[idx], arr, c);
        arr.remove(arr.size() - 1);
        comb(idx + 1, target, arr, c);
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        res = new ArrayList<>();
        List<Integer> arr = new ArrayList<>();
        comb(0,target,arr,candidates);
        return res;
    }
}