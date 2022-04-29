package SchoolTimeTable;

public class RuleBestSolution {
    private RuleId ruleName;
    private Rule.RuleType ruleType;
    private String configuration;
    private float ruleGrade;

    public RuleBestSolution(RuleId ruleName, Rule.RuleType ruleType, String configuration, float ruleGrade) {
        this.ruleName = ruleName;
        this.ruleType = ruleType;
        this.configuration = configuration;
        this.ruleGrade = ruleGrade;
    }

    public RuleId getRuleName() {
        return ruleName;
    }

    public Rule.RuleType getRuleType() {
        return ruleType;
    }

    public String getConfiguration() {
        return configuration;
    }

    public float getRuleGrade() {
        return ruleGrade;
    }
}
