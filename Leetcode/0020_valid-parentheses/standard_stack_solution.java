class Solution {
    public char validOpen(char c) {
        if (c == '}')
            return '{';
        if (c == ')')
            return '(';
        return '[';
    }
    public boolean isValid(String s) {
        Deque<Character> st = new ArrayDeque<>();
        for (char c : s.toCharArray()) {
            if (c == '}' || c == ')' || c == ']') {
                if (st.isEmpty() || (st.pop() != validOpen(c)))
                    return false;
            } else {
                st.push(c);
            }
        }
        return st.isEmpty();
    }
}
