class Solution {

    public int carFleet(int target, int[] position, int[] speed) {
        int carCount = position.length;

        int[][] cars = new int[carCount][2];

        for (int i = 0; i < carCount; i++) {
            cars[i][0] = position[i];
            cars[i][1] = speed[i];
        }

        // Sort cars by position
        Arrays.sort(cars, (a, b) -> Integer.compare(a[0], b[0]));

        double lastFleetTime = -1;
        int fleetCount = 0;

        // Start with the car closest to the target
        for (int i = carCount - 1; i >= 0; i--) {
            double timeToTarget =
                    (double) (target - cars[i][0]) / cars[i][1];

            // Takes longer -> forms a new fleet
            if (timeToTarget > lastFleetTime) {
                fleetCount++;
                lastFleetTime = timeToTarget;
            }
            // Otherwise, it catches the fleet ahead
        }

        return fleetCount;
    }
}