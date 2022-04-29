package SchoolTimeTable;

import java.util.Objects;

public class Rule {
public enum RuleType
        {
            HARD,SOFT;
        }
        private RuleType type;
        private RuleId ruleId;

    public Rule(RuleType type, RuleId ruleId) {
        this.type = type;
        this.ruleId = ruleId;
    }

    public RuleType getType() {
        return type;
    }

    @Override
    public String toString() {
        return " Rule name: "+ruleId+", Rule type: "+type;
    }

    public void setType(RuleType type) {
        this.type = type;
    }
public String getRuleConfiguration(){
        String configuration;
        if(ruleId.configuration==null) {
            configuration="None";
        }
        else{
            configuration=ruleId.configuration;
        }
        return configuration;
}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rule)) return false;
        Rule rule = (Rule) o;
        return type == rule.type && ruleId == rule.ruleId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, ruleId);
    }

    public RuleId getRuleId() {
        return ruleId;
    }
}
