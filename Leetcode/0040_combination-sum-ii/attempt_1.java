class Solution {
    List<List<Integer>> res;

    public void findComb(int[] c, int target, int index, List<Integer> ds) {
        if (target == 0) {
            res.add(new ArrayList<>(ds));
            return;
        }

        for (int i = index; i < c.length; i++) {
            if (i > index && c[i] == c[i - 1])
                continue;
            if (c[i] > target)
                break;

            ds.add(c[i]);
            findComb(c, target - c[i], i + 1, ds);
            ds.remove(ds.size() - 1);
        }
    }

    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        res = new ArrayList<>();
        List<Integer> ds = new ArrayList<>();
        Arrays.sort(candidates);
        findComb(candidates,target,0,ds);
        return res;
    }
}