class Solution {
    public void dfs(int node, int[][] adj, boolean[] visited) {
        visited[node] = true;
        int n = adj[node].length;
        for (int j = 0; j < n; j++) {
            if (adj[node][j] == 1 && !visited[j]) {
                dfs(j, adj, visited);
            }
        }
    }

    public int findCircleNum(int[][] isConnected) {
        int ct = 0;
        boolean[] visited = new boolean[isConnected.length];
        for (int i = 0; i < isConnected.length; i++) {
            if (!visited[i]) {
                ct++;
                dfs(i, isConnected, visited);
            }
        }
        return ct;
    }
}