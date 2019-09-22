import java.math.BigInteger;

class Term {
    static final int xtype = 0;
    static final int sintype = 1;
    private static final int costype = 2;
    private Key key;
    private BigInteger coeff;

    Term(Key k, BigInteger coeff)
    {
        this.key = k;
        this.coeff = coeff;
    }

    private Polynomial multiTerm(Term t)
    {
        BigInteger xpower;
        BigInteger sinpower;
        BigInteger cospower;
        xpower = this.key.getXpower().add(t.key.getXpower());
        sinpower = this.key.getSinxpower().add(t.key.getSinxpower());
        cospower = this.key.getCosxpower().add(t.key.getCosxpower());
        BigInteger coeff;
        coeff = this.coeff.multiply(t.coeff);
        Key k = new Key(xpower,sinpower,cospower);
        Polynomial p = new Polynomial();
        p.insertTerm(k,coeff);
        return p;
    }

    Polynomial derivation()
    {
        Polynomial dp = new Polynomial();
        if (!this.key.getXpower().equals(BigInteger.valueOf(0)))
        {
            Singlepower sp = new Singlepower(xtype,key.getXpower());
            Term nt = sp.Derivation();
            Key newk = new Key(BigInteger.valueOf(0),
                    key.getSinxpower(),key.getCosxpower());
            Term left = new Term(newk,coeff);
            dp = nt.multiTerm(left).addPoly(left.derivation().multiSp(sp));
            return dp;
        }
        else if (!this.key.getSinxpower().equals(BigInteger.valueOf(0)))
        {
            Singlepower sp = new Singlepower(sintype,key.getSinxpower());
            Term nt = sp.Derivation();
            Key newk = new Key(BigInteger.valueOf(0),
                    BigInteger.valueOf(0),key.getCosxpower());
            Term left = new Term(newk,coeff);
            dp = nt.multiTerm(left).addPoly(left.derivation().multiSp(sp));
            return dp;
        }
        else if (!this.key.getCosxpower().equals(BigInteger.valueOf(0)))
        {
            Singlepower sp = new Singlepower(costype,key.getCosxpower());
            Term nt = sp.Derivation();
            Key newk = new Key(BigInteger.valueOf(0),
                    BigInteger.valueOf(0),BigInteger.valueOf(0));
            Term left = new Term(newk,coeff);
            dp = nt.multiTerm(left).addPoly(left.derivation().multiSp(sp));
            return dp;
        }
        else {
            dp.insertTerm(new Key(BigInteger.valueOf(0),
                    BigInteger.valueOf(0),
                    BigInteger.valueOf(0)),
                    BigInteger.valueOf(0));
            return dp;
        }

    }
}
