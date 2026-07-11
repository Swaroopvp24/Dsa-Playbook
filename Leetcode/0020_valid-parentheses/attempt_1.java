//Trying Attempt 1 filename again without reload 
class Solution {
    public boolean isValid(String s) {
        // Top index of our custom stack (-1 means empty)
        int top = -1;

        // Stack to store the expected closing brackets
        char[] stack = new char[s.length()];

        // Odd-length strings can never have valid matching brackets
        if (s.length() % 2 != 0) return false;

        // Traverse each character
        for (char c : s.toCharArray()) {

            // For an opening bracket, push its expected closing bracket
            if (c == '(') stack[++top] = ')';
            else if (c == '[') stack[++top] = ']';
            else if (c == '{') stack[++top] = '}';

            // Current character is a closing bracket
            else {
                // Invalid if:
                // 1. Stack is empty (no matching opening bracket), or
                // 2. Closing bracket doesn't match the expected one
                if (top == -1 || stack[top--] != c)
                    return false;
            }
        }

        // Valid only if all opening brackets were matched
        return top == -1;
    }
}