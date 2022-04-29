package exception;

import SchoolTimeTable.RuleId;

public class RuleAppearsTwiceException extends RuntimeException{
    private RuleId ruleId;

    public RuleAppearsTwiceException(RuleId ruleId) {
        super("The Rule " + ruleId + " appears at least twice in the file");
        this.ruleId = ruleId;
    }

//    @Override
//    public String toString() {
//       return ;
//    }
}
