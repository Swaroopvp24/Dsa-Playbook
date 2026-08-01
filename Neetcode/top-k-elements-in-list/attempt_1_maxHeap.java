class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        PriorityQueue<int[]> maxHeap = new PriorityQueue<>((a, b) -> Integer.compare(b[1], a[1]));

        HashMap<Integer, Integer> count = new HashMap<>();
        for (int n : nums) {
            count.put(n, count.getOrDefault(n, 0) + 1);
        }

        for(Map.Entry<Integer, Integer> entry : count.entrySet()){
            maxHeap.offer(new int[]{entry.getKey() , entry.getValue()});
            // if(maxHeap.size() > k){
            //     maxHeap.poll();
            // }
        }
        int[] res =  new int[k];
        for(int i=0; i<k; i++){
            res[i] = maxHeap.poll()[0];
        }
        return res;
    }
}
