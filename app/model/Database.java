package model;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by lizhuoli on 15/8/25.
 */
public class Database {
    final Morphia morphia = new Morphia();

    protected Datastore initDB(){
        morphia.mapPackage("org.mongodb.morphia.example");
        MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/morphia_example");
        final Datastore datastore = morphia.createDatastore(new MongoClient(uri), "morphia_example");
        datastore.ensureIndexes();
        return datastore;
    }

    public static void main(String[] args){
        Database mydb = new Database();
        Datastore datastore = mydb.initDB();
        User myuser = new User();
        myuser.setName("lizhuoli");
        myuser.setGender(1);
        myuser.setLocation(100);
        datastore.save(myuser);
    }

// tell Morphia where to find your classes
// can be called multiple times with different packages or classes
// create the Datastore connecting to the default port on the local host
}