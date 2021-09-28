package utils.models;

import utils.infoModels.PropertyInfoObject;

import java.util.List;

public class UserInProblemObject {
    private final boolean userInProblem;
    private final List<PropertyInfoObject> properties;

    public UserInProblemObject(boolean inProblem, List<PropertyInfoObject> properties){
        this.userInProblem = inProblem;
        this.properties = properties;
    }

    public boolean isUserInProblem() {
        return userInProblem;
    }

    public List<PropertyInfoObject> getProperties() {
        return properties;
    }
}
