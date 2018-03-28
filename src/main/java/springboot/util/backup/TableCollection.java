package springboot.util.backup;

import java.util.ArrayList;

/**
 * @author tangj
 * @date 2018/1/23 21:02
 */
public class TableCollection extends ArrayList<Table>{
    /**
     * Sort tables according to constraints
     */
    public void sort(){
        for(int i = 0 ; i < size(); ){
            boolean corrupted = false;
            for(int j = i + 1; j < size(); j++){
                if(get(i).isReferenced(get(j))){
                    Table table = get(i);
                    remove(table);
                    add(table);
                    corrupted = true;
                    break;
                }
            }
            if(!corrupted){
                i++;
            }
        }
    }
}
