package SchoolTimeTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ClassRoomCollection {
    private List<ClassRoom> classRooms;

    public ClassRoomCollection() {
        classRooms = new ArrayList<>();
    }

    public void addToClassRoomCollection(ClassRoom classRoom) {
        classRooms.add(classRoom);
    }

    public List<ClassRoom> getClassRooms() {
        return classRooms;
    }
    @Override
    public String toString() {
        String classRoomsStr="List of all ClassRooms: ";
        for(ClassRoom classRoom: classRooms){
            String currMsg=System.lineSeparator()+classRoom.toString();
            classRoomsStr+=currMsg;
        }
        return classRoomsStr;
    }

    public ClassRoom getClassRoomById(int id){
        return classRooms.stream().filter(t->t.getId()==id).findFirst().get();
    }


    public void sortClassRoomsById() {
        Collections.sort(classRooms, new Comparator<ClassRoom>() {
            public int compare(ClassRoom c1, ClassRoom c2) {
                return c1.getId() - c2.getId();
            }
        });
    }
}
