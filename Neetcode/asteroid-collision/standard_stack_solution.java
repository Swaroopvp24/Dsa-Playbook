class Solution {

    public void reverseArray(int[] arr) {
        int left = 0, right = arr.length - 1;

        while (left < right) {
            int temp = arr[left];
            arr[left++] = arr[right];
            arr[right--] = temp;
        }
    }

    public int[] asteroidCollision(int[] asteroids) {
        Deque<Integer> stack = new ArrayDeque<>();

        for (int asteroid : asteroids) {

            // Right-moving asteroid
            if (asteroid > 0) {
                stack.push(asteroid);
                continue;
            }

            // Resolve collisions with positive asteroids
            while (!stack.isEmpty()
                    && stack.peek() > 0
                    && stack.peek() < Math.abs(asteroid)) {
                stack.pop();
            }

            // Equal sizes -> both explode
            if (!stack.isEmpty() && stack.peek() == Math.abs(asteroid)) {
                stack.pop();
            }
            // Negative asteroid survives
            else if (stack.isEmpty() || stack.peek() < 0) {
                stack.push(asteroid);
            }
        }

        // Stack is in reverse order
        int[] result = new int[stack.size()];
        int index = 0;

        while (!stack.isEmpty()) {
            result[index++] = stack.pop();
        }

        reverseArray(result);
        return result;
    }
}
