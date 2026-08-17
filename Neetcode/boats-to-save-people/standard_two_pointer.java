class Solution {
    public int numRescueBoats(int[] people, int limit) {
        Arrays.sort(people);
        int l = 0, r = people.length - 1, ct = 0;
        while (l < r) {
            int weight = people[l] + people[r];
            if (weight <= limit) {
                people[l] = -1;
                people[r] = -1;
                ct++;
                l++;
                r--;
            } else {
                r--;
            }
        }
        for (int i = 0; i < people.length; i++) {
            if (people[i] != -1 && people[i] <= limit)
                ct++;
        }
        return ct;
    }
}