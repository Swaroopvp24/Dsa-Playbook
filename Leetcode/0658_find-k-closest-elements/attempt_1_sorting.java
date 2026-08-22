class Solution {
    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        List<Integer> list = new ArrayList<>();

        for (int num : arr) {
            list.add(num);
        }

        list.sort((a, b) -> {
            int da = Math.abs(a - x);
            int db = Math.abs(b - x);

            if (da != db) {
                return da - db; // smaller distance first
            }

            return a - b; // smaller value first if tie
        });

        List<Integer> result = new ArrayList<>(list.subList(0, k));
        Collections.sort(result);
        return result;
    }
}