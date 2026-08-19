class Solution {
    public int trap(int[] height) {
        int l=0 , r = height.length - 1;
        int lmax = Integer.MIN_VALUE;
        int rmax = Integer.MIN_VALUE;
        int total = 0;
        while(l<=r){
            lmax = Math.max(lmax,height[l]);
            rmax = Math.max(rmax,height[r]);
            if(height[l]<height[r]){
                total += Math.min(lmax,rmax) - height[l];
                l++;
            }else{
                total += Math.min(lmax,rmax) - height[r];
                r--;
            }
        }
        return total;
    }
}
