class Solution {
    public String simplifyPath(String path) {
        Deque<String> stack = new ArrayDeque<>();
        StringBuilder currentDirectory = new StringBuilder();

        // Start from index 1 since the path always begins with '/'
        for (int i = 1; i < path.length(); i++) {
            char currentChar = path.charAt(i);

            if (currentChar == '/') {
                processDirectory(stack, currentDirectory);
                currentDirectory.setLength(0); // Reset for the next directory
            } else {
                currentDirectory.append(currentChar);
            }
        }

        // Process the last directory, if any
        processDirectory(stack, currentDirectory);

        // Stack contains directories from root to current path
        return "/" + String.join("/", stack.reversed());
    }

    private void processDirectory(Deque<String> stack, StringBuilder directory) {
        String directoryName = directory.toString();

        if (directoryName.equals("..")) {
            // Go one level up, if possible
            if (!stack.isEmpty()) {
                stack.pop();
            }
        } else if (!directoryName.isEmpty() && !directoryName.equals(".")) {
            // Ignore "." and empty directories
            stack.push(directoryName);
        }
    }
}
