public class Solution {

    public int[] asteroidCollision(int[] asteroids) {
        int stackTop = -1; // Top of the in-place stack

        for (int asteroid : asteroids) {

            // Collision only happens: positive stack top vs negative asteroid
            while (stackTop >= 0
                    && asteroids[stackTop] > 0
                    && asteroid < 0) {

                // Stack asteroid wins
                if (asteroids[stackTop] > Math.abs(asteroid)) {
                    asteroid = 0;
                    break;
                }

                // Both have the same size
                if (asteroids[stackTop] == Math.abs(asteroid)) {
                    stackTop--;
                    asteroid = 0;
                    break;
                }

                // Incoming asteroid wins, keep checking
                stackTop--;
            }

            // Incoming asteroid survived
            if (asteroid != 0) {
                asteroids[++stackTop] = asteroid;
            }
        }

        // Return the valid portion of the stack
        return Arrays.copyOfRange(asteroids, 0, stackTop + 1);
    }
}
