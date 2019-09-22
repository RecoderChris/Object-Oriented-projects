import java.util.ArrayList;

class SinCosFunc extends BasicFunctions {
    private Nestfication nf;
    private int power;
    private int kind = 2;
    private String str;

    @Override
    public int analyzeLexical(String s, int beginpoint) {
        str = s;
        power = 1;
        int pos = beginpoint;
        if (pos + 2 >= s.length()) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        if (s.substring(pos,pos + 3).equals("sin") |
                s.substring(pos,pos + 3).equals("cos")) {
            if (s.substring(pos,pos + 3).equals("sin")) {
                kind = 1;
            }
            else {
                kind = 0;
            }
            pos = pos + 3;
            pos = skipBlank(s,pos);
            if ((pos < s.length()) && (s.charAt(pos) == '(')) {
                pos++;
                pos = skipBlank(s,pos);
                nf = new Nestfication();
                pos = nf.analyzeLexical(s,pos);
                if ((pos < s.length()) && (s.charAt(pos) != ')'))
                {
                    System.out.println("WRONG FORMAT!");
                    System.exit(0);
                }
                pos++;
                pos = skipBlank(s,pos);
                if ((pos < s.length()) && (s.charAt(pos) == '^')) {
                    pos++;
                    ArrayList<Integer> a;
                    a = getPower(s,pos);
                    power = a.get(0);
                    pos = a.get(1);
                }
            }
            else {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
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
        String out = "";
        if (power == 0) {
            out = out + "0";
            return out;
        }
        if (kind == 1) {
            if (power != 1) {
                out = out + power + "*sin(" + str.substring(nf.getNestbegin(),
                        nf.getNestend()) + ")^" + (power - 1) + "*";
            }
            out = out + "cos(" + str.substring(nf.getNestbegin(),
                    nf.getNestend()) + ")*";
            out = out + nf.Differential();
        }
        else if (kind == 0) {
            if (power != 1) {
                out = out + power + "*cos(" + str.substring(nf.getNestbegin(),
                        nf.getNestend()) + ")^" + (power - 1) + "*";
            }
            out = out + "(-1 * sin(" + str.substring(nf.getNestbegin(),
                    nf.getNestend()) + "))*";
            out = out + nf.Differential();
        }
        else {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        return out;
    }

    @Override
    public String getOpt() {
        String str = "";
        if (power == 0) {
            str = str + "1";
        }
        else if (kind == 1) {
            str = str + "sin(";
        }
        else {
            str = str + "cos(";
        }
        boolean a = true;
        if (nf.isExpflag()) {
            nf.setSinflag(a);
        }
        str = str + nf.getOpt();
        str = str + ")";
        if (power != 1) {
            str = str + "^" + power;
        }
        return str;
    }
}
