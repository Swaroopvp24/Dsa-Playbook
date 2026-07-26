class Solution {
    public int[][] merge(int[][] inter) {
        int n = inter.length;
        Arrays.sort(inter, (a, b) -> Integer.compare(a[0], b[0]));
        ArrayList<int[]> matrix = new ArrayList<>();
        int start = inter[0][0];
        int end = inter[0][1];
        for (int i = 0; i < n; i++) {
            if (inter[i][0] <= end) {
                // overlap
                end = Math.max(end, inter[i][1]);
            } else {
                // gap
                matrix.add(new int[] { start, end });

                start = inter[i][0];
                end = inter[i][1];
            }
        }
        matrix.add(new int[]{start, end});
        int[][] result = matrix.toArray(new int[matrix.size()][]);
        return result;
    }
}