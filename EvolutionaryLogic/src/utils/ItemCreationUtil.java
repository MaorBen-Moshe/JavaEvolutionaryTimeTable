package utils;

import models.*;

import java.util.Map;

public class ItemCreationUtil {

    public static TimeTableItem createItem(TimeTableSystemDataSupplier supplier){
        int daySelected, hourSelected;
        Teacher teacherSelected;
        Subject subjectSelected;
        SchoolClass classSelected;

        daySelected = RandomUtils.nextIntInRange(0, supplier.getDays());
        hourSelected = RandomUtils.nextIntInRange(0, supplier.getHours());
        teacherSelected = getRandItem(supplier.getTeachers());
        subjectSelected = getRandItem(supplier.getSubjects());
        classSelected = getRandItem(supplier.getClasses());
        return new TimeTableItem(daySelected, hourSelected, classSelected, teacherSelected, subjectSelected);
    }

    public static <T extends SerialItem> T getRandItem(Map<Integer, T> collection){
        int randInt = RandomUtils.nextIntInRange(1, collection.size());
        return collection.get(randInt);
    }
}