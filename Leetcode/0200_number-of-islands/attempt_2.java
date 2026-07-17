class Solution {
    public void dfs(int row, int col, boolean[][] visited, char[][] grid) {
        visited[row][col] = true;

        //up , down , left , right
        int[] dr = { -1, 1, 0, 0 };
        int[] dc = { 0, 0, -1, 1 };


        for (int i = 0; i < 4; i++) {
            int nr = row + dr[i];
            int nc = col + dc[i];

            if (nr >= 0 && nr < grid.length &&
                    nc >= 0 && nc < grid[0].length &&
                    grid[nr][nc] == '1' &&
                    !visited[nr][nc]) {

                dfs(nr, nc, visited, grid);
            }
        }
    }

    public int numIslands(char[][] grid) {
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        int ct = 0;

        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (grid[row][col] == '1' && !visited[row][col]) {
                    ct++;
                    dfs(row, col, visited, grid);
                }
            }
        }
        return ct;
    }
}