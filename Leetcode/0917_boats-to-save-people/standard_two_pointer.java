class Solution {
    public int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);

        int left = 0;
        int right = people.length - 1;
        int boatCount = 0;

        while (left < right) {
            int totalWeight = people[left] + people[right];

            if (totalWeight <= limit) {
                people[left] = -1;
                people[right] = -1;

                boatCount++;
                left++;
                right--;
            } else {
                right--;
            }
        }

        for (int i = 0; i < people.length; i++) {
            if (people[i] != -1) {
                boatCount++;
            }
        }

        return boatCount;
    }
}