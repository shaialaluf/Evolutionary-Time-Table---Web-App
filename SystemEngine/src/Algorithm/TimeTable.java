package Algorithm;

import SchoolTimeTable.Rule;
import SchoolTimeTable.RulesCollection;
import SchoolTimeTable.RuleId;
import SchoolTimeTable.SchoolDB;
import java.util.*;
import java.util.stream.Collectors;

public class TimeTable extends Solution implements FitnessCalculator {
    private List<TimeTableCell> timeTableCells;
    private Map<Rule, Float> ruleGrades;
    private List<TimeTableCell>[][] timeTableCellsMatrix;
    private SchoolDB schoolDB;
    private Random rand = new Random();//random fifth- how many?what about illegal fifth-set?
    private int maxNumberOfFifths;

    public TimeTable(int days, int hours, SchoolDB schoolDB) {// only for first generation!!!!!!!!!!!!!!!!!!!!!!!!!!
        timeTableCells = new ArrayList<>();
        timeTableCellsMatrix = new List[days][hours];
        ruleGrades = new HashMap<>();
        this.schoolDB = schoolDB;
        maxNumberOfFifths = schoolDB.getNumberOfDays() * schoolDB.getNumberOfHours() * Math.max(schoolDB.getClassRoomsList().size(),schoolDB.getTeacherList().size());
        //maxNumberOfFifths = schoolDB.getNumberOfDays() * schoolDB.getNumberOfHours() * schoolDB.getClassRoomsList().size() * schoolDB.getSubjectsList().size() * schoolDB.getTeacherList().size();
        createRandomFifthsList();// only for first generation!!!!!!!!!!!!!!!!!!!!!!!!!!
        initTimeTableMatrix(days, hours);
        createTimeTableMatrixFromList();
    }

    public int getMaxNumberOfFifths() {
        return maxNumberOfFifths;
    }

    public TimeTable(SchoolDB schoolDB) { // FOR SECOND GENETATION AND SO ON!!!!!!!!!
        timeTableCells = new ArrayList<>();
        timeTableCellsMatrix = new List[schoolDB.getNumberOfDays()][schoolDB.getNumberOfHours()];
        ruleGrades = new HashMap<>();
        this.schoolDB = schoolDB;
        maxNumberOfFifths = schoolDB.getNumberOfDays() * schoolDB.getNumberOfHours() * Math.max(schoolDB.getClassRoomsList().size(),schoolDB.getTeacherList().size());
        //maxNumberOfFifths = schoolDB.getNumberOfDays() * schoolDB.getNumberOfHours() * schoolDB.getClassRoomsList().size() * schoolDB.getSubjectsList().size() * schoolDB.getTeacherList().size();
        initTimeTableMatrix(schoolDB.getNumberOfDays(), schoolDB.getNumberOfHours());
    }

    private void createRandomFifthsList() {
        int randomNumberOfFifths = rand.nextInt(maxNumberOfFifths) + 1;
        for (int i = 0; i < randomNumberOfFifths; i++) {
            timeTableCells.add(new TimeTableCell(schoolDB));
        }
    }

    public void createTimeTableMatrixFromListWithoutSort() {
        for (TimeTableCell cell : timeTableCells) {
            //ruleGrades = new HashMap<>();
            int day = cell.getDay();
            int hour = cell.getHour();
            timeTableCellsMatrix[day - 1][hour - 1].add(cell);
        }
    }

    public void createTimeTableMatrixFromList() {
        for (TimeTableCell cell : timeTableCells) {
            //ruleGrades = new HashMap<>();
            int day = cell.getDay();
            int hour = cell.getHour();
            timeTableCellsMatrix[day - 1][hour - 1].add(cell);
        }

        for (int i = 0; i < getDays(); i++) {
            for (int j = 0; j < getHours(); j++) {
                sortCellInMatrixByCTS(timeTableCellsMatrix[i][j]);
            }
        }
    }

    private void sortCellInMatrixByCTS(List<TimeTableCell> cellList) {
        int currPlaceInSort = 0;
        int currIndex;
        for (int c = 1; c <= schoolDB.getClassRoomsList().size(); c++) {
            for (int t = 1; t <= schoolDB.getTeacherList().size(); t++) {
                for (int s = 1; s <= schoolDB.getSubjectsList().size(); s++) {
                    currIndex = 0;
                    for (TimeTableCell currCell : cellList) {
                        if (c == currCell.getClassRoom() && t == currCell.getTeacher() && s == currCell.getSubject()) {
                            Collections.swap(cellList, currPlaceInSort, currIndex);
                            currPlaceInSort++;
                        }
                        currIndex++;
                    }
                }

            }
        }
    }

    public List<TimeTableCell> createSortedListByDaysHours() {
        List<TimeTableCell> sortedList = new ArrayList<>();
        for (int i = 0; i < getDays(); i++) {
            for (int j = 0; j < getHours(); j++) {
                for (TimeTableCell cell : timeTableCellsMatrix[i][j]) {
                    TimeTableCell copiedCell = new TimeTableCell(cell.getDay(), cell.getHour(), cell.getClassRoom(), cell.getTeacher(), cell.getSubject());
                    sortedList.add(copiedCell);
                    // sortedList.add(cell);
                }
            }
        }

        return sortedList;
    }

    private void initTimeTableMatrix(int days, int hours) {
        for (int i = 0; i < days; i++) {
            for (int j = 0; j < hours; j++) {
                timeTableCellsMatrix[i][j] = new ArrayList<>();
            }
        }
    }

    //    public void addFifthToList(TimeTableCell cellToAdd) {
//        timeTableCells.add(cellToAdd);
//    }
    public void addFifthToList(TimeTableCell cellToAdd) {
        TimeTableCell newCellToAdd = new TimeTableCell(cellToAdd.getDay(), cellToAdd.getHour(), cellToAdd.getClassRoom(), cellToAdd.getTeacher(), cellToAdd.getSubject());
        timeTableCells.add(newCellToAdd);
        //timeTableCells.add(cellToAdd);
    }

    public void addFifthToList() {
        TimeTableCell newCellToAdd = new TimeTableCell(schoolDB);
        int day = newCellToAdd.getDay() - 1;
        int hour = newCellToAdd.getHour() - 1;
        timeTableCells.add(newCellToAdd);
        timeTableCellsMatrix[day][hour].add(newCellToAdd);
        sortCellInMatrixByCTS(timeTableCellsMatrix[day][hour]);


//            int day=newCellToAdd.getDay(); ////////////////// more efficient
//            int hour=newCellToAdd.getHour();
//            for(TimeTableCell cell:timeTableCellsMatrix[day][hour]){
//                if(cell.getClassRoom()<newCellToAdd.getClassRoom())
//            }

    }

    public void removeFifthFromBeginning() {
        TimeTableCell toDelete = timeTableCells.remove(0);
        timeTableCellsMatrix[toDelete.getDay() - 1][toDelete.getHour() - 1].remove(toDelete);
        //// check if delete from matrix also
    }


    @Override
    public void calcFitnessGrade() {
        RulesCollection rules = schoolDB.getRulesCollection();
        int HardWeight = schoolDB.getRulesCollection().getHardRulesWeight();
        float softWeight = 100 - HardWeight;
        for (Rule rule : rules.getRules()) {
            float grade = rule.getRuleId().calculateGrade(this, schoolDB);
            ruleGrades.put(rule, grade);
        }
        this.setFitness(calcWeightedFitnessGrade(rules, HardWeight, softWeight));
    }

    public Map<Rule, Float> getRuleGrades() {
        return ruleGrades;
    }

    private float calcWeightedFitnessGrade(RulesCollection rules, float HardWeight, float softWeight) {
        float sumHardRules = 0;
        float sumSoftRules = 0;
        for (Rule rule : ruleGrades.keySet()) {
            if (rule.getType() == Rule.RuleType.HARD) {
                sumHardRules += ruleGrades.get(rule);
            } else {
                sumSoftRules += ruleGrades.get(rule);
            }
        }

        this.setAvgHardRules(sumHardRules / rules.getHardRuleCounter());
        this.setAvgSoftRules(sumSoftRules / rules.getSoftRuleCounter());
        return this.getAvgHardRules() * (HardWeight / 100) + this.getAvgSoftRules() * (softWeight / 100);

        //  90 * 0.6  +  80 * 0.4  =
//        this.setAvgHardRules((sumHardRules * HardWeight) / ((100) * rules.getHardRuleCounter()));
//        this.setAvgSoftRules((sumSoftRules * softWeight) / ((100) * rules.getSoftRuleCounter()));
//        return (this.getAvgSoftRules() + this.getAvgHardRules());
    }

    public List<TimeTableCell> getTimeTableCells() {
        return timeTableCells;
    }

    public List<TimeTableCell>[][] getTimeTableCellsMatrix() {
        return timeTableCellsMatrix;
    }

    public int getDays() {
        return schoolDB.getNumberOfDays();
    }

    public int getHours() {
        return schoolDB.getNumberOfHours();
    }

    public SchoolDB getSchoolDB() {
        return schoolDB;
    }

    public List<TimeTableCell>[][] getTimeTableByClass(int classId) {
        int days = schoolDB.getNumberOfDays();
        int hours = schoolDB.getNumberOfHours();
        List<TimeTableCell>[][] timeTableByClass = new List[days][hours];
        for (int i = 0; i < days; i++) {
            for (int j = 0; j < hours; j++) {
                timeTableByClass[i][j] = timeTableCellsMatrix[i][j].stream().filter(t -> t.getClassRoom() == classId).collect(Collectors.toList());
            }
        }

        return timeTableByClass;
    }


    public List<TimeTableCell>[][] getTimeTableByTeacher(int teacherId) {
        int days = schoolDB.getNumberOfDays();
        int hours = schoolDB.getNumberOfHours();
        List<TimeTableCell>[][] timeTableByTeacher = new List[days][hours];
        for (int i = 0; i < days; i++) {
            for (int j = 0; j < hours; j++) {
                timeTableByTeacher[i][j] = timeTableCellsMatrix[i][j].stream().filter(t -> t.getTeacher() == teacherId).collect(Collectors.toList());
            }
        }

        return timeTableByTeacher;
    }

    public List<List<TimeTableCell>[][]> getTimeTableOfAllClasses(){
        int days = schoolDB.getNumberOfDays();
        int hours = schoolDB.getNumberOfHours();
        int classes=schoolDB.getClassRoomsList().size();
        List<List<TimeTableCell>[][]> classesTimeTables=new ArrayList<>(classes);
        for(int c=0; c<classes;c++){
            classesTimeTables.add(new List[days][hours]);

        }

        for(int c=0; c<classes;c++){
            for(int d=0; d<days; d++){
                for(int h=0; h<hours; h++){
                    classesTimeTables.get(c)[d][h]=new ArrayList<>();
                }
            }
        }

        for(TimeTableCell cell: timeTableCells){
            int hour=cell.getHour()-1;
            int day=cell.getDay()-1;
            int classRoom=cell.getClassRoom()-1;
            (classesTimeTables.get(classRoom)[day][hour]).add(cell);
        }
        return classesTimeTables;
    }

    public int getMaxStudyPerHour(List<TimeTableCell>[][] timeTable) {
        int days = schoolDB.getNumberOfDays();
        int hours = schoolDB.getNumberOfHours();
        int max = 0;
        for (int i = 0; i < days; i++) {
            for (int j = 0; j < hours; j++) {
                max = Math.max(timeTable[i][j].size(), max);
            }
        }

        return max;
    }

    public class TimeTableCell {
        private int day;
        private int hour;
        private int classRoom;
        private int teacher;
        private int subject;

        public TimeTableCell(int day, int hour, int classRoom, int teacher, int subject) {
            this.day = day;
            this.hour = hour;
            this.classRoom = classRoom;
            this.teacher = teacher;
            this.subject = subject;
        }

        @Override
        public String toString() {
            return "TimeTableCell{" +
                    "day=" + day +
                    ", hour=" + hour +
                    ", classRoom=" + classRoom +
                    ", teacher=" + teacher +
                    ", subject=" + subject +
                    '}' + System.lineSeparator();
        }

        public TimeTableCell(SchoolDB school) {
            this.day = rand.nextInt(school.getNumberOfDays()) + 1;
            this.hour = rand.nextInt(school.getNumberOfHours()) + 1;
            this.classRoom = rand.nextInt(school.getClassRoomsList().size()) + 1;
            this.subject = rand.nextInt(school.getSubjectsList().size()) + 1;
            this.teacher = rand.nextInt(school.getTeacherList().size()) + 1;
        }

        public int convertCellToInt(int subjectsAmount, int classesAmount, int teachersAmount, int hoursAmount, int daysAmount) {
            return (subject - 1 +
                    (teacher - 1) * subjectsAmount +
                    (classRoom - 1) * subjectsAmount * teachersAmount +
                    (hour - 1) * teachersAmount * subjectsAmount * classesAmount +
                    (day - 1) * hoursAmount * teachersAmount * subjectsAmount * classesAmount);

        }

        public int convertCellToIntByTeacher(int subjectsAmount, int classesAmount, int hoursAmount, int daysAmount) {
            return (subject - 1 +
                    (classRoom - 1) * subjectsAmount +
                    (hour - 1) * subjectsAmount * classesAmount +
                    (day - 1) * hoursAmount * subjectsAmount * classesAmount);

        }

        public int convertCellToIntByClass(int subjectsAmount, int teachersAmount, int hoursAmount, int daysAmount) {
            return (subject - 1 +
                    (teacher - 1) * subjectsAmount +
                    (hour - 1) * teachersAmount * subjectsAmount +
                    (day - 1) * hoursAmount * teachersAmount * subjectsAmount);

        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getClassRoom() {
            return classRoom;
        }

        public void setClassRoom(int classRoom) {
            this.classRoom = classRoom;
        }

        public int getTeacher() {
            return teacher;
        }

        public void setTeacher(int teacher) {
            this.teacher = teacher;
        }

        public int getSubject() {
            return subject;
        }

        public void setSubject(int subject) {
            this.subject = subject;
        }
    }

}



