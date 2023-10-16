package Auditoriski.a2t4;

import java.math.BigDecimal;

public class BigComplex {
    private BigDecimal realPart;
    private BigDecimal imaginaryPart;

    public BigComplex(BigDecimal realPart, BigDecimal imaginaryPart) {
        this.realPart = realPart;
        this.imaginaryPart = imaginaryPart;
    }

    public BigComplex() {
    }

    public BigComplex add(BigComplex compl){
        return new BigComplex(this.realPart.add(compl.realPart),
                this.imaginaryPart.add(compl.imaginaryPart));
    }

    @Override
    public String toString() {
        return "BigComplex{" +
                "realPart=" + realPart +
                ", imaginaryPart=" + imaginaryPart +
                '}';
    }
}
