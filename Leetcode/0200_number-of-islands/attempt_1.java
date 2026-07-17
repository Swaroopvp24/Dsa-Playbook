class Solution {
    public void dfs(int row, int col, boolean[][] visited, char[][] grid) {
        visited[row][col] = true;
        if (row > 0 && grid[row - 1][col] == '1' && !visited[row - 1][col]) {
            dfs(row - 1, col, visited, grid);
        }
        if (col > 0 && grid[row][col - 1] == '1' && !visited[row][col - 1]) {
            dfs(row, col - 1, visited, grid);
        }
        if (row < grid.length - 1 && grid[row + 1][col] == '1' && !visited[row + 1][col]) {
            dfs(row + 1, col, visited, grid);
        }
        if (col < grid[row].length - 1 && grid[row][col + 1] == '1' && !visited[row][col + 1]) {
            dfs(row, col + 1, visited, grid);
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