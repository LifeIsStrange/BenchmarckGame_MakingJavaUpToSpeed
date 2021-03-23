import jdk.incubator.vector.*;

import java.io.Console;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static jdk.incubator.vector.VectorOperators.*;
class SpectralNorm
{
    static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_128;

    public static void main(String[] args)
    {
        int n = 10;
        if (args.length > 0) n = Integer.parseInt(args[0]);

        var u = new double[n];
        Arrays.fill(u, 1);
        var v = new double[n];

        // print all initial vals
        // simulate no memset ( just the first)
        // remove para
        for (var i = 0; i < 10; i++)
        {
            mult_AtAv(u, v, n);
            mult_AtAv(v, u, n);
        }

        // WHY NO CLONING despite span value type
        var result = Math.sqrt(dot(u, v, n) / dot(v, v, n));
        System.out.println(BigDecimal.valueOf(result).setScale(9, RoundingMode.HALF_UP));
    }

    private static double A(int i, int j)
    {
        return (i + j) * (i + j + 1) / 2 + i + 1;
    }

    private static double dot(double[] v, double[] u, int n)
    {
        double sum = 0;
        for (var i = 0; i < n; i++)
            sum += v[i] * u[i];
        return sum;
    }

    private static void mult_Av(double[] v, double[] outv, int n)
    {
        var list = new int[n];

        // make the array ascending
        for (int i = 0; i < n ; i++) {
            list[i] = i;
        }

        // bench without and with a threadpool
        Arrays.stream(list).parallel().forEach( i -> {
            var sum = DoubleVector.zero(SPECIES);

            for (var j = 0; j < n; j += 2)
            {
                var b = DoubleVector.fromArray(SPECIES, v, j);
                // needless array
                var tmp = new double[2];
                tmp[0] = A(i, j);
                tmp[1] = A(i, j + 1);
                var a = DoubleVector.fromArray(SPECIES, tmp, 0);

                // is the operation in place
                sum = sum.add(b.div(a));
            }

            // should be horizontal
            sum = sum.add(sum);
            System.err.println("CHICHON");
            System.out.println(sum);
            System.err.println("JEANMI");
            System.err.println(Arrays.toString(outv));

            // to double should only be done on the first element
            outv[i] = sum.toDoubleArray()[0];
        });
    }

    private static void mult_Atv(double[] v, double[] outv, int n)
    {
        var list = new int[n];

        // make the array ascending
        for (int i = 0; i < n ; i++) {
            list[i] = i;
        }

        Arrays.stream(list).parallel().forEach( i -> {
            var sum = DoubleVector.zero(SPECIES);

            for (var j = 0; j < n; j += 2)
            {
                var b = DoubleVector.fromArray(SPECIES, v, j);
                // needless array
                var tmp = new double[2];
                tmp[0] = A(j, i);
                tmp[1] = A(j + 1, i);
                var a = DoubleVector.fromArray(SPECIES, tmp, 0);

                // is the operation in place
                sum = sum.add(b.div(a));
            }

            // should be horizontal
            sum = sum.add(sum);


            // to double should only be done on the first element
            outv[i] = sum.toDoubleArray()[0];
        });
    }

    private static void mult_AtAv(double[] v, double[] outv, int n)
    {
        var tmp = new double[n];
        mult_Av(v, tmp, n);
        mult_Atv(tmp, outv, n);
    }
}

/*
public class Main {
    public static void main(String... args) {
        System.out.print(System.getProperty("java.version"));

        int count = 1000_000;
        float a[] = new float[count];
        float b[] = new float[count];
        float c[] = new float[count];
        Random r = new Random();
        for (int i = 0; i < count; ++i) {
            a[i] = r.nextFloat();
            b[i] = r.nextFloat();
        }

        for (int i = 0; i < 10; ++i) {
            sqrtsum(a, b,c);
            sqrtsumVector(a, b,c);
        }

        long s = System.nanoTime();
        for (int i = 0; i < 100; ++i) {
            sqrtsum(a, b,c);
        }
        long m = System.nanoTime();
        System.out.println(m - s);
        for (int i = 0; i < 100; ++i) {
            sqrtsumVector(a, b,c);
        }
        System.out.println(System.nanoTime() - m);
    }

    static void sqrtsum(float[] a, float[] b, float[] c) {
        for (int i = 0; i < a.length; ++i) {
            c[i] = -(a[i] * a[i] + b[i] * b[i]);
        }
    }

    static VectorSpecies<Float> SPECIES = FloatVector.SPECIES_256;

    static void sqrtsumVector(float[] a, float[] b, float[] c) {
        int i = 0;
        int len = a.length & ~(SPECIES.length() - 1);
        for (; i < len; i+= SPECIES.length()) {
            //var m = SPECIES.indexInRange(i, a.length);

            var va = FloatVector.fromArray(SPECIES, a, i);
            var vb = FloatVector.fromArray(SPECIES, b, i);
            var vc = va.mul(va)
                    .add(vb.mul(vb))
                    .neg();
            vc.intoArray(c, i);
        }
        for (; i < a.length; ++i) {
            c[i] = (a[i] * a[i] + b[i] * b[i]) * -1f;
        }
    }
}*/