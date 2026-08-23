class Solution {
    public int calPoints(String[] operations) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (String s : operations) {
            if (s.equals("+")) {
                int n1 = stack.pop();
                int n2 = stack.peek();

                stack.push(n1);
                stack.push(n1 + n2);
            } else if (s.equals("D")) {
                int n = stack.peek();
                int prod = 2 * n;
                stack.push(prod);
            } else if (s.equals("C")) {
                stack.pop();
            } else {
                stack.push(Integer.parseInt(s));
            }
        }
        int res = 0;
        while (!stack.isEmpty()) {
            res += stack.pop();
        }
        return res;
    }
}