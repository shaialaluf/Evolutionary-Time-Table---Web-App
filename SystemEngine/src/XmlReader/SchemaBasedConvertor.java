package XmlReader;

import Algorithm.EvolutionaryAlgorithm;
import CoreEvolution.SystemManager;
import ETTgenerated.*;
import EvolutionEngineDB.Crossovers.AspectOriented;
import EvolutionEngineDB.Crossovers.Crossover;
import EvolutionEngineDB.Crossovers.DayTimeOriented;
import EvolutionEngineDB.Mutations.Flipping;
import EvolutionEngineDB.Mutations.Mutation;
import EvolutionEngineDB.Mutations.MutationCollection;
import EvolutionEngineDB.Mutations.Sizer;
import EvolutionEngineDB.Selections.RouletteWheel;
import EvolutionEngineDB.Selections.Selection;
import EvolutionEngineDB.Selections.Tournament;
import EvolutionEngineDB.Selections.Truncation;
import exception.*;
import SchoolTimeTable.*;


import java.util.HashMap;
import java.util.Map;

public class SchemaBasedConvertor {
    private SchoolDB schoolDB;
    private EvolutionaryAlgorithm algorithmDB;
    private ETTDescriptor descriptor;

    public SchemaBasedConvertor(ETTDescriptor descriptor) {
        this.descriptor = descriptor;
        schoolDB=new SchoolDB();
        //algorithmDB=new EvolutionaryAlgorithm();
    }

    public void createDaysAndHoursForSchool(){
        schoolDB.setNumberOfDays(descriptor.getETTTimeTable().getDays());
        schoolDB.setNumberOfHours(descriptor.getETTTimeTable().getHours());
    }

    //////////////////////////////////////////// first init days/hours.
    //////////////////////////////////////////// second init Subjects.
    /////////////////////////////////////////// third init teachers.
    public SystemManager createDB(){
        createDaysAndHoursForSchool();
        createSubjectsCollection();
        createTeachersCollection();
        createClassRoomsCollection();
        createRulesCollection();//may not work-reflection :(
         // schoolDB is ready
//        createInitialPopulation();
//        createSelection();
//        createCrossOver();
//        createMutations();
        // algorithmDB is ready

        SystemManager system=new SystemManager();
        system.setSchoolSettings(schoolDB);
        //system.setEvolutionaryAlgorithmSettings(algorithmDB);
        return system;
    }

//    private void createMutations(){
//        MutationCollection mutations;
//        mutations=new MutationCollection();
//        Mutation mutationToAdd=null;
//        for(ETTMutation currentMutation:descriptor.getETTEvolutionEngine().getETTMutations().getETTMutation()){
//            String type=currentMutation.getName();
//            switch (type){
//                case "Flipping":
//                    mutationToAdd=new Flipping(currentMutation.getConfiguration());
//                    break;
//                case "Sizer":
//                    mutationToAdd=new Sizer(currentMutation.getConfiguration());
//                    break;
//            }
//
//            mutationToAdd.setProbability(currentMutation.getProbability());
//            mutations.addMutation(mutationToAdd);
//        }
//
//        algorithmDB.setMutations(mutations);
//    }
//
//    private void createCrossOver() {
//        Crossover crossoverTechnique=null;
//        String type=descriptor.getETTEvolutionEngine().getETTCrossover().getName();
//
//        switch (type){
//            case "DayTimeOriented":
//                crossoverTechnique=new DayTimeOriented();
//                break;
//
//            case "AspectOriented":
//                crossoverTechnique=new AspectOriented(descriptor.getETTEvolutionEngine().getETTCrossover().getConfiguration());
//                break;
//        }
//        crossoverTechnique.setCuttingPointsNumber(descriptor.getETTEvolutionEngine().getETTCrossover().getCuttingPoints());
//        algorithmDB.setCrossover(crossoverTechnique);
//    }
//
//    private void createSelection(){
//        Selection selectionTechnique=null;
//        String type=descriptor.getETTEvolutionEngine().getETTSelection().getType();
//        int elitism=0;
//        if(descriptor.getETTEvolutionEngine().getETTSelection().getETTElitism()!=null){
//            if(descriptor.getETTEvolutionEngine().getETTSelection().getETTElitism()>=descriptor.getETTEvolutionEngine().getETTInitialPopulation().getSize()){
//                throw new ElitismBiggerThanPopulationException(descriptor.getETTEvolutionEngine().getETTSelection().getETTElitism());
//            }
//
//            elitism=descriptor.getETTEvolutionEngine().getETTSelection().getETTElitism();
//        }
//        switch (type){
//            case "Truncation":
//                selectionTechnique=new Truncation(descriptor.getETTEvolutionEngine().getETTSelection().getConfiguration(),elitism);
//                // selectionTechnique= (How to convert to something specific) Class.forName(type).getConstructor().newInstance();
//                break;
//
//            case "RouletteWheel":
//                selectionTechnique=new RouletteWheel(elitism);
//                break;
//
//            case "Tournament":
//                selectionTechnique=new Tournament(descriptor.getETTEvolutionEngine().getETTSelection().getConfiguration(),elitism);
//                break;
//        }
//        algorithmDB.setSelection(selectionTechnique);
//
////        Class clazz;
////        try {
////             clazz=Class.forName(type);
////            Constructor c=clazz.getConstructor();
////            selectionTechnique= (clazz)c.newInstance();
////        }
////        catch (ClassNotFoundException | NoSuchMethodException e) {
////
////        } catch (InvocationTargetException e) {
////            e.printStackTrace();
////        } catch (InstantiationException e) {
////            e.printStackTrace();
////        } catch (IllegalAccessException e) {
////            e.printStackTrace();
////        }

 //   }

//    private void createInitialPopulation() {
//        algorithmDB.setInitialPopulation(descriptor.getETTEvolutionEngine().getETTInitialPopulation().getSize());
//    }

    public void createSubjectsCollection () {
        int id;
        String name;

        for(ETTSubject subject:descriptor.getETTTimeTable().getETTSubjects().getETTSubject()){
            id=subject.getId();
            name=subject.getName();
            schoolDB.getSubjectsCollection().addToSubjectCollection(new Subject(id,name));
        }
        schoolDB.getSubjectsCollection().sortSubjectsById();
        areSubjectsArrangedInSequentialOrder();
    }

    public void areSubjectsArrangedInSequentialOrder()  {
        for(int i=0; i<schoolDB.getSubjectsList().size(); i++){
            if(schoolDB.getSubjectsList().get(i).getId()!=i+1){
                throw new SubjectsIdNotSequentialException();
            }
        }
    }

    public void createTeachersCollection () {
        int id;
        String name;
        int workingHours;

        for(ETTTeacher teacher: descriptor.getETTTimeTable().getETTTeachers().getETTTeacher()){
            id=teacher.getId();
            name=teacher.getETTName();
            workingHours=teacher.getETTWorkingHours();
            if(workingHours > schoolDB.getNumberOfDays()*schoolDB.getNumberOfHours()){
                throw new TeacherWorkingHoursException(id,workingHours,schoolDB.getNumberOfDays()*schoolDB.getNumberOfHours());
            }
            Teacher currentTeacher=new Teacher(id,name,workingHours);
            for(ETTTeaches teaches:teacher.getETTTeaching().getETTTeaches()){
                if(teaches.getSubjectId()>schoolDB.getSubjectsList().size() || teaches.getSubjectId()<1){
                    throw new TeacherWithIllegalSubjectException(id, name,teaches.getSubjectId(),schoolDB.getSubjectsList().size());
                }
                currentTeacher.addSubjectToTeacher(schoolDB.getSubjectsCollection().getSubjectById(teaches.getSubjectId()));
            }

            schoolDB.getTeacherCollection().addToTeacherCollection(currentTeacher);
        }
        schoolDB.getTeacherCollection().sortTeachersById();
        areTeachersArrangedInSequentialOrder();
    }

    public void areTeachersArrangedInSequentialOrder()  {
        for(int i=0; i<schoolDB.getTeacherList().size(); i++){
            if(schoolDB.getTeacherList().get(i).getId()!=i+1){
                throw new TeachersIdNotSequentialException();
            }
        }
    }

    public void createClassRoomsCollection () {
        int id;
        String name;
        int sumOfHours;

        for(ETTClass ettclass: descriptor.getETTTimeTable().getETTClasses().getETTClass()){
            id=ettclass.getId();
            name=ettclass.getETTName();
            sumOfHours=0;
            ClassRoom currentClassRoom=new ClassRoom(id,name);
            for(ETTStudy study:ettclass.getETTRequirements().getETTStudy()){
                if(study.getSubjectId()>schoolDB.getSubjectsList().size() || study.getSubjectId()<1){
                    throw new ClassWithIllegalSubjectException(id,name,study.getSubjectId(),schoolDB.getSubjectsList().size());
                }
                sumOfHours+=study.getHours();
                currentClassRoom.addSubjectToClass(schoolDB.getSubjectsCollection().getSubjectById(study.getSubjectId()), study.getHours());
            }
            if(sumOfHours>schoolDB.getNumberOfHours()*schoolDB.getNumberOfDays()){
                throw new ClassPassedHoursLimitException(id,name,sumOfHours,schoolDB.getNumberOfHours()*schoolDB.getNumberOfDays());
            }
            schoolDB.getClassRoomsCollection().addToClassRoomCollection(currentClassRoom);

        }
        schoolDB.getClassRoomsCollection().sortClassRoomsById();
        areClassRoomsArrangedInSequentialOrder();
    }

    public void areClassRoomsArrangedInSequentialOrder() {
        for (int i = 0; i < schoolDB.getClassRoomsList().size(); i++) {
            if (schoolDB.getClassRoomsList().get(i).getId() != i + 1) {
                throw new ClassesIdNotSequentialException();
            }
        }
    }

    public void createRulesCollection() {
        for (ETTRule rule : descriptor.getETTTimeTable().getETTRules().getETTRule()) {
            RuleId currentRuleId = Enum.valueOf(RuleId.class, rule.getETTRuleId());
            Rule.RuleType currentRuleType = Enum.valueOf(Rule.RuleType.class, rule.getType().toUpperCase());
            Rule currentRule = new Rule(currentRuleType, currentRuleId);
            ////////////////////////////////////////////////////////////////////////////////////////
            if (rule.getETTConfiguration()!=null) {
                String configuration = rule.getETTConfiguration();
                int indexOf = configuration.lastIndexOf("=");
                String configValue = configuration.substring(indexOf + 1);
                currentRule.getRuleId().evaluateConfiguration(configValue);
//              String configuration=rule.getETTConfiguration().get(0);///////?????
//              String[] elements=configuration.split(",");
//              for(String currentElement: elements){
//                  String[] currStr=currentElement.split("=");
//                  for(RuleId ruleId: RuleId.values()){
//                      String methodName="set"+currStr[0];/////// must give name starts with "set"!!!!!!
//                      try {
//                          Method methodConfig=ruleId.getClass().getMethod(methodName, currStr[1].getClass());
//                          methodConfig.invoke(ruleId,currStr[1]);
//                      }
//                      catch (Exception e){
//
//                      }
            }
            schoolDB.getRulesCollection().addToRuleCollection(currentRule);
        }
        ////////////////////////////////////////////////////////////////////////////////////////////////

        schoolDB.getRulesCollection().setHardRulesWeight(descriptor.getETTTimeTable().getETTRules().getHardRulesWeight());
        isARuleAppearsTwice();
    }



    public void isARuleAppearsTwice(){
        Map<RuleId, Boolean> rulesMap=new HashMap<>();
        for(RuleId ruleId: RuleId.values()){
            rulesMap.put(ruleId,false);
        }

        for(Rule rule: schoolDB.getRulesList()){
            if(rulesMap.get(rule.getRuleId())==true){
                throw new RuleAppearsTwiceException(rule.getRuleId());
            }
            else{
                rulesMap.put(rule.getRuleId(),true);
            }
        }
    }
}
