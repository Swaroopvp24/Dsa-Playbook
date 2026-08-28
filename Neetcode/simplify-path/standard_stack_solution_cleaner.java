public class Solution {
    public String simplifyPath(String path) {
        Deque<String> stack = new ArrayDeque<>();
        String[] directories = path.split("/");

        for (String directory : directories) {
            if (directory.equals("..")) {
                // Move one directory up
                if (!stack.isEmpty()) {
                    stack.pop();
                }
            } else if (!directory.isEmpty() && !directory.equals(".")) {
                // Add a valid directory to the path
                stack.push(directory);
            }
        }

        // Reverse the stack to restore root-to-leaf order
        return "/" + String.join("/", stack.reversed());
    }
}
