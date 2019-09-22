class Calculate implements CalculateDifferential {

    int skipBlank(int beginpoint,String s) {
        int pos;
        pos = beginpoint;
        while ((pos < s.length()) && ((s.charAt(pos) == ' ') |
                (s.charAt(pos) == '\t'))) {
            pos++;
        }
        return pos;
    }

    @Override
    public String Differential() {
        return "";
    }

    @Override
    public int analyzeLexical(String s,int beginpoint) {
        return 0;
    }

    @Override
    public String getOpt() {
        return "";
    }
}
