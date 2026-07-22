class Solution {
    List<List<Integer>> result;
    int n;
    int k;

    public List<List<Integer>> combine(int n, int k) {
        result = new ArrayList<>();
        this.n = n;
        this.k = k;
        generateCombinations(1, new ArrayList<Integer>());
        return result;
    }

    private void generateCombinations(int index, List<Integer> ds) {
        if (ds.size() == k) {
            result.add(new ArrayList<>(ds));
            return;
        }
        int remaining  =  k - ds.size();
        for (int i = index; i <= n - remaining + 1; i++) {
            ds.add(i);
            generateCombinations(i + 1, ds);
            ds.remove(ds.size() - 1);
        }
    }
}