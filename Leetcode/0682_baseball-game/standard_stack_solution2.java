class Solution {
    public int calPoints(String[] operations) {
        Deque<Integer> stack = new ArrayDeque<>();
        int res = 0;
        for (String s : operations) {
            if (s.equals("+")) {
                int n1 = stack.pop();
                int n2 = stack.peek();

                stack.push(n1);
                stack.push(n1 + n2);
                res += (n1 + n2);
            } else if (s.equals("D")) {
                int n = stack.peek();
                int prod = 2 * n;
                stack.push(prod);
                res += prod;
            } else if (s.equals("C")) {
                res -= stack.pop();

            } else {
                stack.push(Integer.parseInt(s));
                res += Integer.parseInt(s);
            }
        }
        return res;
    }
}