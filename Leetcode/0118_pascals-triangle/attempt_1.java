class Solution {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 1; i <= numRows; i++) {
            List<Integer> row = new ArrayList<>();
            for (int j = 1; j <= i; j++) {
                if (j == 1 || j == i) {
                    row.add(1);
                } else {
                    int value = result.get(i - 2).get(j - 2) + result.get(i - 2).get(j - 1);

                    row.add(value);
                }
            }
            result.add(row);
        }
        return result;
    }
}