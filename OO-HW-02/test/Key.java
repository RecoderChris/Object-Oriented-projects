import java.math.BigInteger;

public final class Key {
    private BigInteger xpower;
    private BigInteger sinxpower;
    private BigInteger cosxpower;

    Key(BigInteger x,BigInteger sinx,BigInteger cosx)
    {
        this.xpower = x;
        this.sinxpower = sinx;
        this.cosxpower = cosx;
    }

    BigInteger getXpower()
    {
        return this.xpower;
    }

    BigInteger getSinxpower() {
        return this.sinxpower;
    }

    BigInteger getCosxpower()
    {
        return this.cosxpower;
    }

    @Override
    public int hashCode()
    {
        int result = 17;
        result = result * 31 + xpower.intValue() +
                cosxpower.intValue() + sinxpower.intValue();
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        Key k = (Key)obj;
        return (this.xpower.equals(k.xpower) &&
                this.sinxpower.equals(k.sinxpower) &&
                this.cosxpower.equals(k.cosxpower));
    }
}
