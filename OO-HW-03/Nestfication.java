import java.math.BigInteger;

public class Nestfication extends Calculate {
    private int nestbegin;
    private int nestend;
    private BasicFunctions outerfunc;
    private Addandminus poly;
    private boolean expflag = false;
    private BigInteger coeff;
    private boolean constflag = false;
    private boolean sinflag = false;

    boolean isExpflag() {
        return expflag;
    }

    void setSinflag(boolean sinflag) {
        this.sinflag = sinflag;
    }

    boolean isConstflag() {
        return constflag;
    }

    BigInteger getCoeff() {
        return coeff;
    }

    int getNestbegin() {
        return this.nestbegin;
    }

    int getNestend() {
        return this.nestend;
    }

    Nestfication another()
    {
        Nestfication nt = new Nestfication();
        nt.nestbegin = this.nestbegin;
        nt.nestend = this.nestend;
        nt.expflag = this.expflag;
        nt.outerfunc = this.outerfunc;
        nt.poly = this.poly;

        return nt;
    }

    @Override
    public int analyzeLexical(String s, int beginpoint) {
        coeff = BigInteger.valueOf(1);
        int pos;
        nestbegin = beginpoint;
        pos = beginpoint;
        if (pos > s.length()) {
            System.out.print("WRONG FORMAT!");
            System.exit(0);
        }
        if ((s.charAt(pos) == 's') |
                (s.charAt(pos) == 'c')) {
            outerfunc = new SinCosFunc();
            pos = outerfunc.analyzeLexical(s,pos);
            nestend = pos;
        }
        else if (((s.charAt(pos) >= '0') &&
                (s.charAt(pos) <= '9')) || (s.charAt(pos) == '+')
                || (s.charAt(pos) == '-'))
        {
            outerfunc = new Constant();
            constflag = true;
            pos = outerfunc.analyzeLexical(s,pos);
            coeff = coeff.multiply(((Constant)outerfunc).getValue());
            nestend = pos;

        }
        else if (s.charAt(pos) == 'x') {
            outerfunc = new PowerFunc();
            pos = outerfunc.analyzeLexical(s,pos);
            nestend = pos;

        }
        else if (s.charAt(pos) == '(') {
            expflag = true;
            poly = new Addandminus();
            //get a poly factor
            pos = poly.analyzeLexical(s,pos);
            if ((pos >= s.length()) || (s.charAt(pos) != ')')) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
            if (poly.getTerms().size() == 0) {
                this.coeff = BigInteger.valueOf(0);
            }
            pos++;
            nestend = pos;
        }
        else {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        pos =  skipBlank(pos,s);
        return pos;
    }

    @Override
    public String Differential() {
        String out = "";
        if (expflag) {
            out = out + "(";
            out = out + poly.Differential();
            out = out + ")";
        }
        else {
            out = out + "(";
            out = out + outerfunc.Differential();
            out = out + ")";
        }
        return out;
    }

    @Override
    public String getOpt() {
        String str = "";
        if ((!expflag) && (!constflag)) {
            if (this.coeff.compareTo(BigInteger.valueOf(0)) < 0) {
                str = str + "(" + outerfunc.getOpt() + ")";
            }
            else {
                str = str + outerfunc.getOpt();
            }
        }
        else if (!constflag) {
            //got this poly in crack.
            if (poly.getTerms().size() == 1) {
                boolean fg = false;
                for (Multiply k : poly.getTerms().keySet()) {
                    if (k.getCoeff().equals(BigInteger.valueOf(1)) &&
                        poly.getTerms().get(k) == '-') {
                        fg = true;
                    }
                }
                if (fg || sinflag) {
                    str = str + "(";
                    str = str + poly.getOpt();
                    str = str + ")";
                }
                else {
                    str = str + poly.getOpt();
                }
            }
            else {
                str = str + "(";
                str = str + poly.getOpt();
                str = str + ")";
            }
        }
        return str;
    }
}
