
package BL;

import Query.Query;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class QueryBL {
    private File file = new File("queries.ser");
    private List<Query> queries = new ArrayList<>();

    public QueryBL() {
        load();
    }
    
    private void load() {
        if(!file.exists()) {
            return;
        }
        

        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            while(true) {
                queries.add((Query) ois.readObject());
            }
        } catch(EOFException | FileNotFoundException e) {
            
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public void save() throws FileNotFoundException, IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            for (Query query : queries) {
                oos.writeObject(query);
            }
        }
    }
    
    public void addFavourite(Query q) {
        queries.add(q);
    }
    
    public void removeFromFavourites(Query q) {
        queries.add(q);
    }
    
    public boolean isFavourite(Query q) {
        return queries.contains(q);
    }

    public List<Query> getFavourites() {
        return queries;
    }
}
