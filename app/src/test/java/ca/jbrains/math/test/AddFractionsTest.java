package ca.jbrains.math.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AddFractionsTest {
    @Test
    void zeroPlusZero() {
        Assertions.assertEquals(new Fraction(0), new Fraction(0).plus(new Fraction(0)));
    }

    @Test
    void notZeroPlusZero() {
        Assertions.assertEquals(new Fraction(7), new Fraction(7).plus(new Fraction(0)));
    }

    @Test
    void zeroPlusNotZero() {
        Assertions.assertEquals(new Fraction(3), new Fraction(0).plus(new Fraction(3)));
    }

    @Test
    void notZeroPlusNotZero() {
        Assertions.assertEquals(new Fraction(12), new Fraction(8).plus(new Fraction(4)));
    }

    @Test
    void sameDenominators() {
        Assertions.assertEquals(
                new Fraction(3, 5),
                new Fraction(1, 5).plus(new Fraction(2, 5)));
    }

    public static class Fraction {
        private int numerator;
        private int denominator;

        public Fraction(int integerValue) {
            this(integerValue, 1);
        }

        public Fraction(int numerator, int denominator) {
            this.numerator = numerator;
            this.denominator = denominator;
        }

        public Fraction plus(Fraction that) {
            if (this.denominator == 1 || that.denominator == 1)
                return new Fraction(this.numerator + that.numerator);
            else
                return new Fraction(this.numerator + that.numerator, this.denominator);
        }

        public int intValue() {
            return numerator;
        }

        public int getNumerator() {
            return numerator;
        }

        public int getDenominator() {
            return denominator;
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof Fraction) {
                Fraction that = (Fraction) other;
                return this.numerator * that.denominator == that.numerator * this.denominator;
            }
            else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return -762;
        }
    }
}
