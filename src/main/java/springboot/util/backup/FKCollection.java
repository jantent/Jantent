package springboot.util.backup;

import java.util.ArrayList;

/**
 * @author tangj
 * @date 2018/1/23 20:59
 */
public class FKCollection extends ArrayList<FK> {
    private static final long serialVersionUID = -972085209611643212L;

    public boolean isReferenced(Table referenceTable) {
        for (FK fk : this) {
            if (fk.getReferenceTable().equals(referenceTable)) {
                return true;
            }
        }
        return false;
    }
}
