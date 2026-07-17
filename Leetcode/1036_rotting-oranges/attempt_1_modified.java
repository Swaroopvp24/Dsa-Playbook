class Solution {
    public int orangesRotting(int[][] grid) {
        Queue<int[]> queue = new ArrayDeque<>();
        int[] dr = { -1, 1, 0, 0 };
        int[] dc = { 0, 0, -1, 1 };
        int min = 0;
        int fresh = 0;

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == 1)
                    fresh++;
                else if (grid[r][c] == 2) {
                    queue.offer(new int[] { r, c });
                }
            }
        }

        if (fresh == 0)
            return 0;
        if (queue.isEmpty())
            return -1;

        while (!queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cur = queue.poll();
                int cur_r = cur[0];
                int cur_c = cur[1];

                for (int j = 0; j < 4; j++) {
                    int nr = cur_r + dr[j];
                    int nc = cur_c + dc[j];

                    if (nr >= 0 && nr < grid.length &&
                            nc >= 0 && nc < grid[0].length &&
                            grid[nr][nc] == 1) {
                        grid[nr][nc] = 2;
                        fresh--;
                        queue.offer(new int[] { nr, nc });
                    }
                }
            }
            if (!queue.isEmpty())
                min++;
        }
        return (fresh == 0) ? min : -1;
    }
}