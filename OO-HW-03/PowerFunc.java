import java.util.ArrayList;

class PowerFunc extends BasicFunctions {
    private int power = 1;

    @Override
    public int analyzeLexical(String s, int beginpoint) {
        //outer = power func
        int pos = beginpoint;
        if ((pos < s.length()) && (s.charAt(pos) == 'x')) {
            pos = pos + 1;
            pos = skipBlank(s,pos);
            if ((pos < s.length()) && (s.charAt(pos) == '^')) {
                ArrayList<Integer> a;
                pos++;
                a = getPower(s,pos);
                power = a.get(0);
                pos = a.get(1);
            }
        }
        else {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        return pos;
    }

    @Override
    public String Differential() {
        String str = "";
        if (power == 0) {
            str = str + "0";
        }
        else if (power != 1) {
            if (power == 2) {
                str = str + "2*x";
            }
            else {
                str = str + power + "*x^" + (power - 1);
            }
        }
        else {
            str = str + "1";
        }
        return str;
    }

    @Override
    public String getOpt() {
        String str = "";
        if (power == 0) {
            str = str + "1";
        }
        else if (power == 1) {
            str = str + "x";
        }
        else {
            str = str + "x^" + power;
        }
        return str;
    }
}
