package selection;

import java.io.Serializable;

public abstract class SelectionImpel<T> implements Selection<T>, Serializable {
    private final SelectionTypes type;

    protected SelectionImpel(SelectionTypes type){
        this.type = type;
    }

    public SelectionTypes getType(){
        return type;
    }
}
