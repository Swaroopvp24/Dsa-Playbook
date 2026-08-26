class Solution {

    public int carFleet(int target, int[] position, int[] speed) {
        int carCount = position.length;

        int[][] cars = new int[carCount][2];

        for (int i = 0; i < carCount; i++) {
            cars[i][0] = position[i];
            cars[i][1] = speed[i];
        }

        // Sort cars by position from closest to farthest from target
        Arrays.sort(cars, (a, b) -> Integer.compare(a[0], b[0]));

        Deque<Double> fleetTimes = new ArrayDeque<>();

        for (int i = carCount - 1; i >= 0; i--) {
            double timeToTarget =
                    (double) (target - cars[i][0]) / cars[i][1];

            // A slower time means this car forms a new fleet
            if (fleetTimes.isEmpty() || timeToTarget > fleetTimes.peek()) {
                fleetTimes.push(timeToTarget);
            }
            // Otherwise, it catches the fleet ahead
        }

        return fleetTimes.size();
    }
}