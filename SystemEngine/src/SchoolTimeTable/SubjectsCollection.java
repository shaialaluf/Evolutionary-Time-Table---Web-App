package SchoolTimeTable;
import java.util.*;
import java.util.stream.Stream;

public class SubjectsCollection {
    private List<Subject> subjects;

    public SubjectsCollection() {
        subjects = new ArrayList<>();
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void addToSubjectCollection(Subject subject) {
        subjects.add(subject);
    }

    public void sortSubjectsById() {
        Collections.sort(subjects, new Comparator<Subject>() {
            public int compare(Subject s1, Subject s2) {
                return s1.getId() - s2.getId();
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubjectsCollection)) return false;
        SubjectsCollection that = (SubjectsCollection) o;
        return Objects.equals(subjects, that.subjects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(subjects);
    }

    public Subject getSubjectById(int id){
       return subjects.stream().filter(t->t.getId()==id).findFirst().get();
    }


    @Override
    public String toString() {
        String subjectsStr="List of all subjects: ";
        for(Subject currSubject: subjects){
            String currMsg=System.lineSeparator()+currSubject.toString();
            subjectsStr+=currMsg;
        }
        return subjectsStr;
    }
}
