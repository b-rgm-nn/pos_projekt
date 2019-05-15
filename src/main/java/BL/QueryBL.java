
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

/**
 * Handle the Queries that have been saved
 * Singleton, because it's used in two GUIs and should be consistent
 * @author Matthias
 */
public class QueryBL {
    private static QueryBL instance;
    private File file = new File("queries.ser");
    private List<Query> queries = new ArrayList<>();

    private QueryBL() {
        load();
    }

    /**
     * get singleton instance of BL
     * @return the one and only instance of this class
     */
    public synchronized static QueryBL getInstance() {
        if(instance == null) {
            instance = new QueryBL();
        }
        return instance;
    }

    /**
     * load existing queries from queries.ser and append them to the list
     */
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
    
    /**
     * write all saved queries to queries.ser
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public void save() throws FileNotFoundException, IOException {
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            for (Query query : queries) {
                oos.writeObject(query);
            }
        }
    }
    
    /**
     * save a new query
     * @param q the query to be saved
     */
    public void addFavourite(Query q) {
        queries.add(q);
    }
    
    /**
     * remove a query from the saved queries
     * @param q 
     */
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
