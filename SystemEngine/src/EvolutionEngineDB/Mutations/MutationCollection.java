package EvolutionEngineDB.Mutations;

import java.util.ArrayList;
import java.util.List;

public class MutationCollection {
    private List<Mutation> mutations;

    public MutationCollection() {
        mutations=new ArrayList<>();
    }

    public List<Mutation> getMutations() {
        return mutations;
    }
    @Override
    public String toString() {
        String mutationsStr="";
        for(Mutation currMutations: mutations){
            String currMsg=System.lineSeparator()+currMutations.toString();
            mutationsStr+=currMsg;
        }
        return mutationsStr;
    }

    public void addMutation(Mutation mutationToAdd){
        mutations.add(mutationToAdd);
    }

}
