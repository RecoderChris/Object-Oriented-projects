import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Constant extends BasicFunctions {
    private BigInteger value;

    BigInteger getValue() {
        return value;
    }

    @Override
    public int analyzeLexical(String s, int beginpoint) {
        int pos;
        pos = beginpoint;
        int power = 1;
        value = BigInteger.valueOf(1);
        String coeff = "[+-]?[0-9]+";
        Pattern coef;
        Matcher matchercoef;
        coef = Pattern.compile(coeff);
        matchercoef = coef.matcher(s);
        if (matchercoef.find(pos)) {
            if (pos != matchercoef.start()) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
            value = new BigInteger(s.
                    substring(pos,matchercoef.end()));
            pos = matchercoef.end();
        }
        else {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        pos = skipBlank(s,pos);
        //power
        if ((pos < s.length()) && (s.charAt(pos) == '^')) {
            ArrayList<Integer> a;
            a = getPower(s,pos);
            power = a.get(0);
            pos = a.get(1);
        }
        value = value.pow(power);
        //System.out.println(value);
        return pos;
    }

    @Override
    public String Differential() {
        return "0";
    }

    @Override
    public String getOpt() {
        return value.toString();
    }
}
