package utils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UserManager {

    private final Set<User> usersSet;

    public UserManager() {
        usersSet = new HashSet<>();
    }

    public synchronized void addUser(User user) {
        usersSet.add(user);
    }

    public synchronized void removeUser(User user) {
        usersSet.remove(user);
    }

    public synchronized Set<User> getUsers() {
        return Collections.unmodifiableSet(usersSet);
    }

    public boolean isUserExists(String username) {
        for(User user : usersSet){
            if(user.getName().equals(username)) return true;
        }

        return false;
    }

    public synchronized User getUserByName(String name) throws Exception{
        if(!isUserExists(name)){
            throw new IllegalArgumentException(name + " is not exist");
        }

        return usersSet.stream().filter(x -> x.getName().equals(name)).findFirst().orElseThrow(() -> new Exception("Failed to get " + name));
    }
}