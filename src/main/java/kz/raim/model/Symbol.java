package kz.raim.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({@JsonSubTypes.Type(
        value = StandardSymbol.class,
        name = "standard"
), @JsonSubTypes.Type(
        value = BonusSymbol.class,
        name = "bonus"
)})
public abstract class Symbol {
    public String type;
    public double reward_multiplier;

    public Symbol() {
    }
}
