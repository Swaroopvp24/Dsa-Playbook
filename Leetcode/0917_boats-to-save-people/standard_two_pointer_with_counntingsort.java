class Solution {
    public int numRescueBoats(int[] people, int limit) {
        // Two pointer greedy + counting sort(the range of possible weights is bounded)
        //  Arrays.sort(people);
        //  3 ways
        //  1.int[] count = new int[30001]; => fix number of elements
        // 2. int m = Arrays.stream(people).max().getAsInt();
        //    int[] count = new int[m + 1]; => finding max element fot max index length
        // 3
        int[] weightCount = new int[limit + 1]; //=> as people[i] <= limit

        for (int weight : people) {
            weightCount[weight]++;
        }

        for (int weight = 1, personIndex = 0; weight < weightCount.length; weight++) {
            while (weightCount[weight] != 0) {
                people[personIndex++] = weight;
                weightCount[weight]--;
            }
        }

        int lightest = 0;
        int heaviest = people.length - 1;
        int boatCount = 0;

        while (lightest <= heaviest) {
            if (people[lightest] + people[heaviest] <= limit) {
                lightest++;
            }

            heaviest--;
            boatCount++;
        }

        return boatCount;
    }
}
