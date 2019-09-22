import java.math.BigInteger;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class BasicFunctions implements BasicDifferential {
    int skipBlank(String s,int pos) {
        int p = pos;
        while ((p < s.length()) && ((s.charAt(p) == ' ') |
                (s.charAt(p) == '\t'))) {
            p++;
        }
        return p;
    }

    ArrayList<Integer> getPower(String s, int beginpoint) {
        ArrayList<Integer> powerandpos;
        powerandpos = new ArrayList<>();
        int totalpower = 1;
        int pos = beginpoint;
        String power = "[+-]?[0-9]+";
        pos = skipBlank(s,pos);
        if (pos >= s.length()) {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        Pattern rpower;
        Matcher matcherpower;
        rpower = Pattern.compile(power);
        matcherpower = rpower.matcher(s);
        if (matcherpower.find(pos)) {
            if (matcherpower.start() != pos) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
            BigInteger pre;
            pre = new BigInteger(s.substring(pos, matcherpower.end()));
            if ((pre.compareTo(BigInteger.valueOf(10000)) > 0) ||
                (pre.compareTo(BigInteger.valueOf(-10000)) < 0)) {
                System.out.println("WRONG FORMAT!");
                System.exit(0);
            }
            totalpower = pre.intValue();
            pos = matcherpower.end();
        }
        else {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        pos = skipBlank(s,pos);
        powerandpos.add(totalpower);
        powerandpos.add(pos);
        return powerandpos;
    }

    @Override
    public int analyzeLexical(String s,int beginpoint) {
        return 0;
    }

    @Override
    public String Differential() {
        return "";
    }

    @Override
    public String getOpt() {
        return "";
    }
}
