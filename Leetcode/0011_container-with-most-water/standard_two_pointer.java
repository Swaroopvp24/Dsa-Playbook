class Solution {
    public int maxArea(int[] heights) {
        int left = 0;
        int right = heights.length - 1;
        int maxArea = 0;

        while (left < right) {
            int currentArea =
                Math.min(heights[left], heights[right]) * (right - left);

            maxArea = Math.max(maxArea, currentArea);

            if (heights[left] < heights[right])
                left++;
            else
                right--;
        }

        return maxArea;
    }
}