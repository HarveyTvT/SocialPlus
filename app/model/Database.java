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
    public final Datastore datastore;

    public Database(){
        morphia.mapPackage("org.mongodb.morphia.example");
        MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/morphia_example");
        Datastore myDatastore = morphia.createDatastore(new MongoClient(uri), "morphia_example");
        myDatastore.ensureIndexes();
        datastore = myDatastore;
    }

    public Datastore getDatastore() {
        return datastore;
    }
    // tell Morphia where to find your classes
// can be called multiple times with different packages or classes
// create the Datastore connecting to the default port on the local host
}