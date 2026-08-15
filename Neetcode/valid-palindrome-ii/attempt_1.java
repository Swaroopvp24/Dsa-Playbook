class Solution {
    public boolean isAlphanumeric(char ch) {
        return (ch >= 'A' && ch <= 'Z') ||
               (ch >= 'a' && ch <= 'z') ||
               (ch >= '0' && ch <= '9');
    }

    public boolean isPalindrome(String s, int i) {
        int st = 0, en = s.length() - 1;

        while (st < en) {
            while (st < en &&
                   (!isAlphanumeric(s.charAt(st)) || st == i)) {
                st++;
            }

            while (st < en &&
                   (!isAlphanumeric(s.charAt(en)) || en == i)) {
                en--;
            }

            if (Character.toUpperCase(s.charAt(st)) !=
                Character.toUpperCase(s.charAt(en))) {
                return false;
            }

            st++;
            en--;
        }

        return true;
    }

    public boolean validPalindrome(String s) {
        int st = 0, en = s.length() - 1;

        while (st < en) {
            while (st < en && !isAlphanumeric(s.charAt(st))) st++;
            while (st < en && !isAlphanumeric(s.charAt(en))) en--;

            if (Character.toUpperCase(s.charAt(st)) !=
                Character.toUpperCase(s.charAt(en))) {

                return isPalindrome(s, st) ||
                       isPalindrome(s, en);
            }

            st++;
            en--;
        }

        return true;
    }
}