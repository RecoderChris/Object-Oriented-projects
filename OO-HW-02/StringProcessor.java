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
        boolean processed = false;
        int neg = 1;
        skipBlank();
        BigInteger result = BigInteger.valueOf(0);
        try
        {
            if (position < input.length())
            {
                if (input.charAt(position) == '-')
                {
                    neg = -1;
                    position++;
                }
                else if (input.charAt(position) == '+')
                {
                    position++;
                }
            }
            while (position < input.length() &&
                    input.charAt(position) >= '0' &&
                    input.charAt(position) <= '9')
            {
                processed = true;
                result = result.multiply(
                        BigInteger.valueOf(10)).add(BigInteger.valueOf(
                        input.charAt(position) - '0'));
                position++;
            }
        }
        catch (Exception e) {
            skipBlank();
            return (result.multiply(BigInteger.valueOf(neg)));
        }
        skipBlank();
        if (processed) {
            return (result.multiply(BigInteger.valueOf(neg)));
        }
        else { return (BigInteger.valueOf(1)); }
    }

    private BigInteger processX()
    {
        BigInteger pow = BigInteger.valueOf(0);
        skipBlank();
        try {
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
                pow = processInteger();
            }
            return pow;
        }
        catch (Exception e)
        {
            return pow;
        }
    }

    private BigInteger processSincosx()
    {
        BigInteger pow = BigInteger.valueOf(0);
        skipBlank();
        try {
            if ((position < input.length())) {
                pow = BigInteger.valueOf(1);
                position += 3;
                skipBlank();
                while ((position < input.length()) &&
                        ((input.charAt(position) == '(') ||
                                (input.charAt(position) == ')') ||
                                (input.charAt(position) == 'x')))
                {
                    position++;
                    skipBlank();
                }
            }
            if ((position < input.length()) &&
                    (input.charAt(position) == '^')) {
                position++;
                skipBlank();
                if (position < input.length())
                {
                    pow = processInteger();
                }
            }
            return pow;
        }
        catch (Exception e)
        {
            return pow;
        }
    }

    void controlProcess()
    {
        BigInteger xpower;
        BigInteger sinxpower;
        BigInteger cosxpower;
        BigInteger coefficient;
        while (position < input.length())
        {
            xpower = BigInteger.valueOf(0);
            sinxpower = BigInteger.valueOf(0);
            cosxpower = BigInteger.valueOf(0);
            coefficient = BigInteger.valueOf(1);
            processSign();
            processSign();
            do {
                if (position >= input.length())
                {
                    break;
                }
                if (input.charAt(position) == '*') {
                    position++;
                    skipBlank();
                }
                coefficient = coefficient.multiply(processInteger());
                if (position < input.length() &&
                        input.charAt(position) == 'x')
                {
                    xpower = xpower.add(processX());
                }
                else if (position < input.length()
                        && input.charAt(position) == 's') {
                    sinxpower = sinxpower.add(processSincosx());
                }
                else if (position < input.length() &&
                        input.charAt(position) == 'c') {
                    cosxpower = cosxpower.add(processSincosx());
                }
                skipBlank();
            } while (position < input.length()
                    && input.charAt(position) == '*');
            coefficient = coefficient.multiply(
                    BigInteger.valueOf(positive));
            positive = 1;
            Key newk = new Key(xpower,sinxpower,cosxpower);
            poly.insertTerm(newk,coefficient);
        }
    }
}
