import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.math.BigInteger;

public class Polynomial {
    // Basic properties setting
    private HashMap<BigInteger,BigInteger> factorandpower = new HashMap<>();
    private static String constantreg = "([+-]?([0-9]+))";
    private static String powerfuncreg = "(" + "x([\t ]*\\^[\t ]*" +
            constantreg + ")?" + ")";
    private static String termreg = "(" +
            "(([\t ]*)([+-])([\t ]*))((" + constantreg +
            "([\t ]*)(\\*)([\t ]*)" + powerfuncreg + ")" + "|" +
            constantreg + "|" +
            "(" + "([+-]?)([\t ]*)" + powerfuncreg + "))([\t ]*)" +
            ")";

    void insertTerm(BigInteger pow,BigInteger coeff)
    {
        BigInteger finalcoeff;
        if (this.factorandpower.containsKey(pow))
        {
            finalcoeff = coeff.add(factorandpower.get(pow));
        }
        else {
            finalcoeff = coeff;
        }
        this.factorandpower.put(pow,finalcoeff);
    }

    private void constructPoly(String s)
    {
        StringProcessor sp = new StringProcessor(this,s);
        sp.controlProcess();
    }

    private Polynomial takeDerivation()
    {
        Polynomial derivation = new Polynomial();
        for (BigInteger k:this.factorandpower.keySet())
        {
            if (!k.equals(BigInteger.valueOf(0)))
            {
                derivation.insertTerm(k.subtract(BigInteger.valueOf(1)),
                        k.multiply(factorandpower.get(k)));
            }
        }
        return derivation;
    }

    private void printPower(BigInteger power)
    {
        if (power.equals(BigInteger.valueOf(1)))
        {
            System.out.print("*x");
        }
        else if (!power.equals(BigInteger.valueOf(0)))
        {
            System.out.print("*x^");
            System.out.print(power);
        }
    }
    
    private void showPoly()
    {
        BigInteger coeff;
        BigInteger foundcoeff;
        boolean out = false;
        boolean first = true;
        for (BigInteger found:this.factorandpower.keySet())
        {
            foundcoeff = factorandpower.get(found);
            if (foundcoeff.compareTo(BigInteger.valueOf(0)) > 0)
            {
                first = false;
                if (!foundcoeff.equals(BigInteger.valueOf(1)) ||
                        found.equals(BigInteger.valueOf(0)))
                {
                    System.out.print(foundcoeff);
                }
                printPower(found);
                out = true;
                factorandpower.remove(found);
                break;
            }
        }
        for (BigInteger k:this.factorandpower.keySet())
        {
            coeff = this.factorandpower.get(k);
            if (coeff.equals(BigInteger.valueOf(0)))
            {
                continue;
            }
            if (first)
            {
                first = false;
            }
            else {
                if (coeff.compareTo(BigInteger.valueOf(0)) > 0)
                {
                    System.out.print("+");
                }
            }
            if (!coeff.equals(BigInteger.valueOf(1)) ||
                k.equals(BigInteger.valueOf(0)))
            {
                System.out.print(coeff);
            }
            out = true;
            printPower(k);
        }
        if (!out)
        {
            System.out.println(0);
        }
    }
    /*
    * Judge a string whether a legal poly.If it is,return true.Else return false
    */

    private boolean judgePoly(String s)
    {

        /*
        Pre-process of dumb empty space and tab.
        Turn the first term to normal one.
        */
        String str;
        int pos;  // position parameter
        str = s;  // copy of s
        if (str.length() == 0) {
            return false;// this is an empty string, return false.
        }
        for (pos = 0; (str.charAt(pos) == '\t' || str.charAt(pos) == ' ');pos++)
        {
            //filtering
            if (pos == str.length() - 1)
            {
                break;
            }
        }
        if (str.charAt(pos) != '+' && str.charAt(pos) != '-')
        {
            //normalization
            str = "+" + str;
        }
        /*
        Begin a match judging.
        */
        int beginpoint = 0; //current match begin point.
        boolean ifmatch = false; // whether have at least one match.
        Matcher matcher0;
        Pattern r;
        // Define a matcher
        r = Pattern.compile(termreg);
        matcher0 = r.matcher(str);
        while (matcher0.find())
        {
            // Match term by term.
            if (matcher0.start() != beginpoint)
            {
                return false;
            }
            ifmatch = true;// At least one match
            //Every matching time: the end point is next begin.
            beginpoint = matcher0.end();
        }
        return ((ifmatch && beginpoint == str.length()));
    }
    /*
    * Entrance of the program.
    */

    public static void main(String[] args)
    {
        // Standard put in
        Scanner in = new Scanner(System.in);
        String input = in.nextLine();
        //Construct a poly object.
        Polynomial poly = new Polynomial();
        if (!poly.judgePoly(input))
        {
            System.out.println("WRONG FORMAT!");
        }
        else {
            poly.constructPoly(input);
            Polynomial derivation;
            derivation = poly.takeDerivation();
            derivation.showPoly();
        }
    }
}
