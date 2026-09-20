class Solution {
    public int countSeniors(String[] details) {
        int ct = 0;
        for(String s : details){
            if(Integer.parseInt(s.substring(11, 13)) > 60) ct++;
        }
        return ct;
    }
}