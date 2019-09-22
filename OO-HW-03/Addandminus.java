import java.math.BigInteger;
import java.util.HashMap;

class Addandminus extends Calculate {
    private HashMap<Multiply, Character> terms = new HashMap<>();

    HashMap<Multiply, Character> getTerms()
    {
        return terms;
    }

    public int analyzeLexical(String s, int beginpoint) {
        int pos;
        char sig = '+';
        pos = skipBlank(beginpoint,s);
        if (pos >= s.length()) {
            System.out.print("WRONG FORMAT!");
            System.exit(0);
        }
        if (s.charAt(pos) == '(') {
            pos++;
            pos = skipBlank(pos,s);
            if (pos >= s.length()) {
                System.out.print("WRONG FORMAT!");
                System.exit(0);
            }
            if (s.charAt(pos) == '+' || s.charAt(pos) == '-') {
                sig = s.charAt(pos);
                pos++;
                pos = skipBlank(pos,s);
                if (pos >= s.length()) {
                    System.out.print("WRONG FORMAT!");
                    System.exit(0);
                }
                if (s.charAt(pos) == '+' || s.charAt(pos) == '-') {
                    if (s.charAt(pos) == sig) { sig = '+'; }
                    else { sig = '-'; }
                    pos++;
                    pos = skipBlank(pos,s);
                }
            }
            Multiply mnew = new Multiply();
            pos = mnew.analyzeLexical(s,pos);
            if (!mnew.getCoeff().equals(BigInteger.valueOf(0)))
            { terms.put(mnew, sig); }
            pos = skipBlank(pos,s);
        }
        while ((pos < s.length()) && (s.charAt(pos) == '+' ||
                s.charAt(pos) == '-')) {
            sig = s.charAt(pos);
            pos++;
            pos = skipBlank(pos,s);
            if (pos >= s.length()) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
            if ((s.charAt(pos) == '+') || s.charAt(pos) == '-') {
                if (s.charAt(pos) == sig) { sig = '+'; }
                else { sig = '-'; }
                pos++;
                pos = skipBlank(pos,s);
            }
            Multiply mnew = new Multiply();
            pos = mnew.analyzeLexical(s,pos);
            if (!mnew.getCoeff().equals(BigInteger.valueOf(0))) {
                terms.put(mnew, sig);
            }
            pos = skipBlank(pos,s);
        }
        return pos;
    }

    @Override
    public String Differential() {
        boolean first = true;
        String out;
        StringBuilder s = new StringBuilder();
        for (Multiply key : terms.keySet()) {
            if (terms.get(key) == '+') {
                if (first) {
                    first = false;
                    s.append("+(");
                }
                else {
                    s.append("+(");
                }
            }
            else {
                s.append("-(");
            }
            s.append(key.Differential());
            s.append(")");
        }
        out = s.toString();
        return out;
    }

    @Override
    public String getOpt() {
        StringBuilder sb = new StringBuilder();
        for (Multiply k : terms.keySet()) {
            if (terms.get(k) == '+') {
                sb.append(k.getOpt());
                terms.remove(k);
                break;
            }
        }
        if (terms.keySet().size() == 0) {
            return sb.toString();
        }
        for (Multiply key : terms.keySet()) {
            sb.append(terms.get(key));
            sb.append(key.getOpt());
        }
        String str;
        str = sb.toString();
        return str;
    }
}
