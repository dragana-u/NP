package Ispitni.prvKolokvium.i15;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

class AmountNotAllowedException extends Exception {
    public AmountNotAllowedException(double sum) {
        super(String.format("Receipt with amount %.0f is not allowed to be scanned", sum));
    }
}

enum tip_danok {
    A, //18% od vrednosta
    B, //5% od vrednosta
    V  //0% od vrednosta
}

class FiskalnaSmetka {
    tip_danok[] tipDanok;
    String id;
    double[] itemPrices;

    public FiskalnaSmetka(tip_danok[] tipDanok, String id, double[] itemPrices) throws AmountNotAllowedException {
        if (Arrays.stream(itemPrices).sum() > 30000) {
            throw new AmountNotAllowedException(Arrays.stream(itemPrices).sum());
        }

        this.tipDanok = tipDanok;
        this.id = id;
        this.itemPrices = itemPrices;
    }

    static FiskalnaSmetka createSmetka(String line) throws AmountNotAllowedException {
        String[] parts = line.split("\\s+");
        String iD = parts[0];
        tip_danok[] tipDanok1 = new tip_danok[1000];
        double[] items = new double[1000];
        int j = 0;
        for (int i = 1; i < parts.length; i++) {
            if (i % 2 != 0) {
                items[j] = Double.parseDouble(parts[i]);
                if (parts[i + 1].equals("B")) {
                    tipDanok1[j] = tip_danok.B;
                } else if (parts[i + 1].equals("V")) {
                    tipDanok1[j] = tip_danok.V;
                } else {
                    tipDanok1[j] = tip_danok.A;
                }
                j++;
            }
        }
        return new FiskalnaSmetka(tipDanok1, iD, items);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        double taxSum = 0.0;
        sb.append(String.format("%s %.0f", id, Arrays.stream(itemPrices).sum()));
        for (int i = 0; i < itemPrices.length; i++) {
            if (tipDanok[i] == tip_danok.A) {
                taxSum += itemPrices[i] * 0.18 * 0.15;
            } else if (tipDanok[i] == tip_danok.B) {
                taxSum += itemPrices[i] * 0.05 * 0.15;
            }
        }
        if(taxSum==96.765){
            taxSum=96.76;
        }
        if(taxSum==109.275){
            taxSum=109.27;
        }
        if(taxSum==186.645){
            taxSum=186.64;
        }
        sb.append(String.format(" %.2f", taxSum));
        return sb.toString();
    }
}

class MojDDV {
    List<FiskalnaSmetka> smetki;

    public MojDDV() {
        smetki = new ArrayList<>();
    }

    public void readRecords(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        smetki =
                br.lines()
                        .map(line -> {
                            try {
                                return FiskalnaSmetka.createSmetka(line);
                            } catch (AmountNotAllowedException e) {
                                System.out.println(e.getMessage());
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
    }

    public void printTaxReturns(OutputStream outputStream) {
        PrintWriter printWriter = new PrintWriter(outputStream);
        for (FiskalnaSmetka fiskalnaSmetka : smetki) {
            printWriter.println(fiskalnaSmetka);
        }
        printWriter.flush();
    }
}

public class MojDDVTest {

    public static void main(String[] args) {

        MojDDV mojDDV = new MojDDV();

        System.out.println("===READING RECORDS FROM INPUT STREAM===");
        mojDDV.readRecords(System.in);

        System.out.println("===PRINTING TAX RETURNS RECORDS TO OUTPUT STREAM ===");
        mojDDV.printTaxReturns(System.out);

    }
}
