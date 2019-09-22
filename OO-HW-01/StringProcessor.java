import java.math.BigInteger;

class StringProcessor {
    private Polynomial poly;
    private String input;
    private int position;
    private int positive;

    StringProcessor(Polynomial p,String in)
    {
        position = 0;
        positive = 1;
        poly = p;
        input = in;
    }

    private void skipBlank()
    {
        while ((position < input.length()) &&
                (input.charAt(position) == ' ' |
                        input.charAt(position) == '\t'))
        {
            position++;
        }
    }

    private void processSign()
    {
        skipBlank();
        if (position < input.length())
        {
            if (((input.charAt(position) == '+') && (positive == 1)) ||
                    ((input.charAt(position) == '-') && (positive == -1)))
            {
                positive = 1;
                position++;
            }
            else if (((input.charAt(position) == '+') && (positive == -1)) ||
                    ((input.charAt(position) == '-') && (positive == 1)))
            {
                positive = -1;
                position++;
            }
        }
        skipBlank();
    }

    private BigInteger processInteger()
    {
        boolean notone = false;
        skipBlank();
        BigInteger result = BigInteger.valueOf(0);
        try
        {
            while (position < input.length() &&
                    input.charAt(position) >= '0' &&
                    input.charAt(position) <= '9')
            {
                result = result.multiply(
                        BigInteger.valueOf(10)).add(BigInteger.valueOf(
                                input.charAt(position) - '0'));
                position++;
                notone = true;
            }
            if (!notone)
            {
                result = BigInteger.valueOf(1);
            }
        }
        catch (Exception e) {
            skipBlank();
            return result;
        }
        skipBlank();
        return result;
    }

    private BigInteger processPower()
    {
        BigInteger pow = BigInteger.valueOf(0);
        int neg = 1;
        skipBlank();
        try {
            if ((position < input.length()) &&
                (input.charAt(position) == '*')) {
                position++;
                skipBlank();
            }
            if ((position < input.length()) &&
                    (input.charAt(position) == 'x')) {
                pow = BigInteger.valueOf(1);
                position++;
                skipBlank();
            }
            if ((position < input.length()) &&
                    (input.charAt(position) == '^')) {
                position++;
                skipBlank();
                if ((position < input.length()) &&
                        (input.charAt(position) == '-'))
                {
                    neg = -1;
                    position++;
                    skipBlank();
                }
                if ((position < input.length()) &&
                        (input.charAt(position) == '+'))
                {
                    neg = 1;
                    position++;
                    skipBlank();
                }
                if (position < input.length() &&
                        input.charAt(position) >= '0' &&
                        input.charAt(position) <= '9')
                {
                    pow = processInteger().multiply(BigInteger.valueOf(neg));
                }
            }
            skipBlank();
            return pow;
        }
        catch (Exception e)
        {
            return pow;
        }
    }

    void controlProcess()
    {
        BigInteger power;
        BigInteger coefficient;
        while (position < input.length())
        {
            processSign();
            processSign();
            coefficient = processInteger().
                    multiply(BigInteger.valueOf(positive));
            positive = 1;
            power = processPower();
            poly.insertTerm(power,coefficient);
        }
    }
}
