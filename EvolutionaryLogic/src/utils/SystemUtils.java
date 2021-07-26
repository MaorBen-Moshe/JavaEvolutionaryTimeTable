package utils;

import evolutinary.TimeTableEvolutionarySystemImpel;
import models.SchoolClass;
import models.Subject;
import models.Teacher;

import java.util.Map;

public class SystemUtils {
    private static SystemUtils instance;
    private TimeTableEvolutionarySystemImpel system;
    private static Object lock;

    private SystemUtils(){
        system = null;
        lock = new Object();
    }

    public static SystemUtils getInstance(){
        if(instance == null){
            synchronized (lock){
                if(instance == null){
                    instance = new SystemUtils();
                }
            }
        }

        return instance;
    }

    public void setSystem(TimeTableEvolutionarySystemImpel system){
        this.system = system;
    }

    public Map<Integer, Teacher> getTeachers(){
        return system.getTeachers();
    }

    public Map<Integer, Subject> getSubjects(){
        return system.getSubjects();
    }

    public Map<Integer, SchoolClass> getClasses(){
        return system.getClasses();
    }

    public int getDays() {
        return system.getDays();
    }

    public int geHours(){
        return system.getHours();
    }
}
