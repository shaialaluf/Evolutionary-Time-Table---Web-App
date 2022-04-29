package SchoolTimeTable;

import java.util.ArrayList;
import java.util.List;

public class RulesCollection {
    private List<Rule> rules;
    private int softRuleCounter;
    private int hardRuleCounter;
    private int hardRulesWeight;

    public int getHardRulesWeight() {
        return hardRulesWeight;
    }

    public void setHardRulesWeight(int hardRulesWeight) {
        this.hardRulesWeight = hardRulesWeight;
    }

    public RulesCollection() {
        this.rules = new ArrayList<>();
        hardRuleCounter = 0;
        softRuleCounter = 0;
    }

    public void addToRuleCollection(Rule rule) {
        rules.add(rule);
        if(rule.getType()== Rule.RuleType.SOFT){
            this.softRuleCounter++;
        }
        else{
            this.hardRuleCounter++;
        }
    }
    @Override
    public String toString() {
        String rulesStr="List of all rules: ";
        for(Rule currRule: rules){
            String currMsg=System.lineSeparator()+currRule.toString();
            rulesStr+=currMsg;
        }
        return rulesStr;
    }


    public List<Rule> getRules() {
        return rules;
    }

    public int getSoftRuleCounter() {
        return softRuleCounter;
    }

    public int getHardRuleCounter() {
        return hardRuleCounter;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }
}
