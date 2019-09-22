import java.math.BigInteger;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Polynomial {
    private HashMap<Key, BigInteger> factorandpower = new HashMap<>();

    private void simplifyPoly()
    {
        //In simplify we will do three main things:
        //sin^2 + cos^2 = 1;
        BigInteger too = BigInteger.valueOf(2);
        boolean found = true;
        while (found) {
            found = false;
            for (Key k : factorandpower.keySet()) {
                Key k1 = new Key(k.getXpower(),k.getSinxpower().
                        subtract(too),k.getCosxpower().add(too));
                if (factorandpower.containsKey(k1) && factorandpower.get(k1).
                        equals(factorandpower.get(k))) {
                    found = true;
                    Key k2 = new Key(k.getXpower(),k1.getSinxpower(),
                            k.getCosxpower());
                    BigInteger coeff = factorandpower.get(k);
                    this.insertTerm(k2,coeff);
                    factorandpower.remove(k);
                    factorandpower.remove(k1);
                    break;
                }
            }
            //1 - sin^2 = cos^2
            for (Key k3 :factorandpower.keySet()) {
                Key k4 = new Key(k3.getXpower(),k3.getSinxpower().
                        subtract(too),k3.getCosxpower());
                if (factorandpower.containsKey(k4) && factorandpower.get(k3).
                        equals(factorandpower.get(k4).
                                multiply(BigInteger.valueOf(-1)))) {
                    found = true;
                    Key k5 = new Key(k4.getXpower(),k4.getSinxpower(),
                            k4.getCosxpower().add(too));
                    BigInteger coeff = factorandpower.get(k4);
                    this.insertTerm(k5,coeff);
                    factorandpower.remove(k3);
                    factorandpower.remove(k4);
                    break;
                }
            }
            //1 = sin^2 = cos^2
            for (Key k6 :factorandpower.keySet()) {
                Key k7 = new Key(k6.getXpower(),k6.getSinxpower(),
                        k6.getCosxpower().subtract(too));
                if (factorandpower.containsKey(k7) && factorandpower.get(k6).
                        equals(factorandpower.get(k7).
                                multiply(BigInteger.valueOf(-1)))) {
                    found = true;
                    Key k8 = new Key(k6.getXpower(),k7.getSinxpower().add(too),
                            k7.getCosxpower());
                    BigInteger coeff = factorandpower.get(k7);
                    this.insertTerm(k8,coeff);
                    factorandpower.remove(k6);
                    factorandpower.remove(k7);
                    break;
                }
            }
        }

    }

    private void derivation()
    {
        Set<Key> set = this.factorandpower.keySet();
        Polynomial poly = new Polynomial();
        for (Key k:set)
        {
            Term t = new Term(k,this.factorandpower.get(k));
            poly.addPoly(t.derivation());
        }
        poly.simplifyPoly();
        poly.showPoly();
    }

    Polynomial multiSp(Singlepower sp)
    {
        Set<Key> set = this.factorandpower.keySet();
        Polynomial poly = new Polynomial();
        BigInteger xpower;
        BigInteger sinpower;
        BigInteger cospower;
        Key knew;
        for (Key k:set)
        {
            if (sp.getType() == Term.xtype)
            {
                xpower = k.getXpower().add(sp.getPower());
                knew = new Key(xpower,k.getSinxpower(),k.getCosxpower());
            }
            else if (sp.getType() == Term.sintype)
            {
                sinpower = k.getSinxpower().add(sp.getPower());
                knew = new Key(k.getXpower(),sinpower,k.getCosxpower());
            }
            else {
                cospower = k.getCosxpower().add(sp.getPower());
                knew = new Key(k.getXpower(),k.getSinxpower(),cospower);
            }
            poly.insertTerm(knew,this.factorandpower.get(k));
        }
        return poly;
    }

    Polynomial addPoly(Polynomial pl)
    {
        Set<Key> set = pl.factorandpower.keySet();
        for (Key k:set)
        {
            this.insertTerm(k,pl.factorandpower.get(k));
        }
        return this;
    }

    void insertTerm(Key k,BigInteger coeff)
    {
        BigInteger finalcoeff;
        if (coeff.equals(BigInteger.valueOf(0))) {
            return;
        }
        if (factorandpower.containsKey(k))
        {
            finalcoeff = coeff.add(factorandpower.get(k));
        }
        else {
            finalcoeff = coeff;
        }
        this.factorandpower.put(k,finalcoeff);
    }

    private void constructPoly(String s)
    {
        Polynomial poly = this;
        StringProcessor sp = new StringProcessor(poly,s);
        sp.controlProcess();
    }

    private boolean judgePoly(String s)
    {
        String str;
        int pos;
        str = s;
        if (str.length() == 0)
        {
            return false;
        }
        for (pos = 0; (str.charAt(pos) == '\t' || str.charAt(pos) == ' ');pos++)
        {
            if (pos == str.length() - 1)
            {
                break;
            }
        }
        if (str.charAt(pos) != '+' && str.charAt(pos) != '-')
        {
            str = "+" + str;
        }
        String numwithsym = "([+-]?[0-9]+)";
        String powerfunction = "((x|sin[\t ]*\\([\t ]*x[\t ]*\\)|" +
                "cos[\t ]*\\([\t ]*x[\t ]*\\))([\t ]*\\^[\t ]*" +
                numwithsym + ")?)";
        String factor = "(" + powerfunction + "|" + numwithsym + ")";
        String term = "[\t ]*[+-][\t ]*[+-]?[\t ]*" + factor +
                "([\t ]*\\*[\t ]*" + factor + "[\t ]*)*";
        int beginpoint = 0;
        boolean matched = false;
        Pattern r;
        Matcher matcher0;
        r = Pattern.compile(term);
        matcher0 = r.matcher(str);
        while (matcher0.find())
        {
            if (matcher0.start() != beginpoint)
            {
                return false;
            }
            matched = true;
            beginpoint = matcher0.end();
        }
        return ((matched && beginpoint == str.length()));
    }

    private void printPower(Key power)
    {
        boolean out = false;
        if (power.getXpower().equals(BigInteger.valueOf(1)))
        {
            out = true;
            System.out.print("x");
        }
        else if (!power.getXpower().equals(BigInteger.valueOf(0)))
        {
            out = true;
            System.out.print("x^");
            System.out.print(power.getXpower());
        }
        if (power.getSinxpower().equals(BigInteger.valueOf(1)))
        {
            if (out) {
                System.out.print("*sin(x)");
            }
            else {
                out = true;
                System.out.print("sin(x)");
            }
        }
        else if (!power.getSinxpower().equals(BigInteger.valueOf(0)))
        {
            if (out) {
                System.out.print("*sin(x)^");
            }
            else {
                out = true;
                System.out.print("sin(x)^");
            }
            System.out.print(power.getSinxpower());
        }
        if (power.getCosxpower().equals(BigInteger.valueOf(1)))
        {
            if (out) {
                System.out.print("*cos(x)");
            }
            else {
                System.out.print("cos(x)");
            }
        }
        else if (!power.getCosxpower().equals(BigInteger.valueOf(0)))
        {
            if (out) {
                System.out.print("*cos(x)^");
            }
            else {
                System.out.print("cos(x)^");
            }
            System.out.print(power.getCosxpower());
        }
    }

    private void showPoly()
    {
        BigInteger one = BigInteger.valueOf(1);
        BigInteger zero = BigInteger.valueOf(0);
        BigInteger negone = BigInteger.valueOf(-1);
        BigInteger coeff;
        BigInteger foundcoeff;
        boolean out = false;
        boolean first = true;
        for (Key found:this.factorandpower.keySet())
        {
            foundcoeff = factorandpower.get(found);
            if (foundcoeff.compareTo(zero) > 0)
            {
                first = false;
                if (!foundcoeff.equals(one) || (found.getXpower().equals(zero)
                        && found.getSinxpower().equals(zero) &&
                        found.getCosxpower().equals(zero)))
                {
                    System.out.print(foundcoeff);
                    if (!found.getXpower().equals(zero)
                            || !found.getSinxpower().equals(zero) ||
                            !found.getCosxpower().equals(zero))
                    { System.out.print("*"); }
                }
                printPower(found);
                out = true;
                factorandpower.remove(found);
                break;
            }
        }
        for (Key k:this.factorandpower.keySet())
        {
            coeff = this.factorandpower.get(k);
            if (coeff.equals(BigInteger.valueOf(0)))
            { continue; }
            if (first)
            { first = false; }
            else {
                if (coeff.compareTo(BigInteger.valueOf(0)) > 0)
                { System.out.print("+"); }
            }
            if ((!coeff.equals(one) && !coeff.equals(negone)) || (k.getXpower().
                    equals(zero) && k.getSinxpower().equals(zero) && k.
                    getCosxpower().equals(zero)))
            {
                System.out.print(coeff);
                if (!k.getXpower().equals(zero)
                        || !k.getSinxpower().equals(zero) ||
                        !k.getCosxpower().equals(zero))
                { System.out.print("*"); }
            }
            if (coeff.equals(negone)) { System.out.print("-"); }
            out = true;
            printPower(k);
        }
        if (!out) { System.out.println(0); }
    }

    public static void main(String[] args) {
        String p = "";
        Scanner in = new Scanner(System.in);
        if (in.hasNextLine()) {
            p = in.nextLine();
        }
        else {
            System.out.println("WRONG FORMAT!");
            System.exit(0);
        }
        Polynomial poly = new Polynomial();
        if (!poly.judgePoly(p))
        {
            System.out.println("WRONG FORMAT!");
        }
        else
        {
            poly.constructPoly(p);
            poly.derivation();
        }
    }

}

