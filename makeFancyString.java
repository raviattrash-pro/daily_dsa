class Solution {
    public String makeFancyString(String s) {
        StringBuilder sb = new StringBuilder();
        int count = 0;

        for (int i = 0; i < s.length(); i++) {
            // If first character or current != previous, reset count
            if (i == 0 || s.charAt(i) != s.charAt(i - 1)) {
                count = 1;
            } else {
                count++;
            }

            // Only append if count <= 2
            if (count <= 2) {
                sb.append(s.charAt(i));
            }
        }

        return sb.toString();
    }
}
