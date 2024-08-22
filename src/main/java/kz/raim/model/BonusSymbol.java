package kz.raim.model;

public class BonusSymbol extends Symbol {
    public String impact;
    public int extra;

    public BonusSymbol() {
        this.type = "bonus";
    }
}
