import java.math.BigInteger;
import java.util.ArrayList;

public class Multiply extends Calculate {
    private ArrayList<Nestfication> factors = new ArrayList<>();
    private String str;
    private BigInteger coeff;

    BigInteger getCoeff() {
        return coeff;
    }

    @Override
    public int analyzeLexical(String s, int beginpoint) {
        boolean first = true;
        str = s;
        int pos = beginpoint;
        coeff = BigInteger.valueOf(1);
        do {
            if (first) {
                first = false;
            }
            else {
                pos++;
            }
            pos = skipBlank(pos,s);
            Nestfication nest = new Nestfication();
            pos = nest.analyzeLexical(s,pos);
            coeff = coeff.multiply(nest.getCoeff());
            factors.add(nest);
            pos = skipBlank(pos,s);
        } while (pos < s.length() && s.charAt(pos) == '*');
        return pos;
    }

    @Override
    public String Differential() {
        String sub;
        String out;
        StringBuilder s = new StringBuilder();
        for (int i = 0;i < factors.size();i++) {
            s.append("(");
            s.append(factors.get(i).Differential());
            s.append(")");
            if (factors.size() > 1) {
                s.append("*");
                ArrayList<Nestfication> newfactor = new ArrayList<>();
                for (Nestfication t : factors) {
                    newfactor.add(t.another());
                }
                //* all the factor that not Differential
                newfactor.remove(newfactor.get(i));
                for (int j = 0;j < newfactor.size() - 1;j++) {
                    sub = "(" + str.substring(newfactor.get(j).
                            getNestbegin(), newfactor.get(j).
                            getNestend()) + ")*";
                    s.append(sub);
                }
                sub = "(" + str.substring(newfactor.
                        get(newfactor.size() - 1).
                        getNestbegin(),newfactor.get(newfactor.
                        size() - 1).getNestend()) + ")";
                s.append(sub);
                if (i != factors.size() - 1) {
                    s.append("+");
                }
            }
        }
        out = s.toString();
        return out;
    }

    @Override
    public String getOpt() {
        String str = "";
        boolean constflag = true;
        boolean out = false;
        for (Nestfication factor : factors) {
            if (!factor.isConstflag()) {
                constflag = false;
                break;
            }
        }
        if (coeff.equals(BigInteger.valueOf(1))) {
            if (constflag) {
                str = str + "1";
                out = true;
            }
        }
        else if (coeff.equals(BigInteger.valueOf(-1))) {
            str = str + "-1";
            out = true;
        }
        else {
            out = true;
            str = str + coeff.toString();
        }
        if ((!factors.get(0).isConstflag())) {
            if (out) {
                str = str + "*";
            }
            if (factors.get(0).getCoeff().compareTo(BigInteger.
                    valueOf(0)) < 0) {
                str = str + "(";
                str = str + factors.get(0).getOpt();
                str = str + ")";
            }
            else {
                str = str + factors.get(0).getOpt();
            }
            out = true;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 1;i < factors.size();i++) {
            if (!factors.get(i).isConstflag()) {
                if (out) {
                    sb.append("*");
                }
                if (factors.get(i).getCoeff().
                        compareTo(BigInteger.valueOf(0)) < 0) {
                    sb.append("(");
                    sb.append(factors.get(i).getOpt());
                    sb.append(")");
                }
                else {
                    sb.append(factors.get(i).getOpt());
                }
                out = true;
            }
        }
        str = str + sb.toString();
        return str;
    }
}
