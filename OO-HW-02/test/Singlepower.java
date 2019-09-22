import java.math.BigInteger;

class Singlepower {
    private int type;
    private BigInteger power;

    Singlepower(int type,BigInteger power)
    {
        this.type = type;
        this.power = power;
    }

    int getType() {
        return this.type;
    }

    BigInteger getPower() {
        return this.power;
    }

    Term Derivation()
    {
        BigInteger xpower;
        BigInteger sinpower;
        BigInteger cospower;
        BigInteger coeff;
        if (this.type == Term.xtype)
        {
            xpower = this.power.subtract(BigInteger.valueOf(1));
            sinpower = BigInteger.valueOf(0);
            cospower = BigInteger.valueOf(0);
            coeff = this.power;
        }
        else if (this.type == Term.sintype)
        {
            xpower = BigInteger.valueOf(0);
            sinpower = this.power.subtract(BigInteger.valueOf(1));
            cospower = BigInteger.valueOf(1);
            coeff = this.power;

        }
        else {
            xpower = BigInteger.valueOf(0);
            sinpower = BigInteger.valueOf(1);
            cospower = this.power.subtract(BigInteger.valueOf(1));
            coeff = this.power.multiply(BigInteger.valueOf(-1));
        }
        Key k = new Key(xpower,sinpower,cospower);
        return (new Term(k,coeff));
    }
}
