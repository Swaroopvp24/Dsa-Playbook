class Solution {
    public boolean isPalindrome(int x) {
        if(x<0) return false;
        if(reversee(x)==x){
            return true;
        }
        return false;
    }
    public int reversee(int n){
        int rev=0;
        while(n!=0){
            rev = (rev * 10) + (n%10);
            n=(int)n/10;
        }
        return rev;
    }
}