class Solution {

    public int trap(int[] height) {

        int left = 0;
        int right = height.length - 1;

        int leftMaxHeight = 0;
        int rightMaxHeight = 0;

        int trappedWater = 0;

        while (left <= right) {

            leftMaxHeight = Math.max(leftMaxHeight, height[left]);
            rightMaxHeight = Math.max(rightMaxHeight, height[right]);

            if (height[left] <= height[right]) {

                trappedWater += leftMaxHeight - height[left];
                left++;

            } else {

                trappedWater += rightMaxHeight - height[right];
                right--;
            }
        }

        return trappedWater;
    }
}

//At every step:
//water at position = maxHeight on the limiting side - current height

