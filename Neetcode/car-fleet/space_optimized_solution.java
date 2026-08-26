class Solution {
    public int carFleet(int target, int[] position, int[] speed) {
        int n = position.length;

        int[][] cars = new int[n][2];

        for (int i = 0; i < n; i++) {
            cars[i][0] = position[i];
            cars[i][1] = speed[i];
        }

        Arrays.sort(cars, (a, b) -> Integer.compare(a[0], b[0]));

        // Deque<Double> stack = new ArrayDeque<>();
        /*
        1. currentTime < stack.top() → current car is faster → it catches the fleet ahead → joins
        that fleet → don't push.
        2. currentTime == stack.top() → catches it exactly at the destination → also joins → don't
        push.
        3. currentTime > stack.top() → current car is slower → it cannot catch the fleet ahead →
        forms a new fleet → push.
        */
        double lastTime = -1;
        int fleets = 0;

        for (int i = n - 1; i >= 0; i--) {
            double time = (double) (target - cars[i][0]) / cars[i][1];

            if (time > lastTime) {
                fleets++;
                lastTime = time;
            }
        }

        return fleets;
    }
}
