package utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import models.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by lizhuoli on 15/8/25.
 */
public class Database {
    final Morphia morphia = new Morphia();
    private Datastore datastore;

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
        myuser.setId("ailuoli");
        myuser.setEmail("ailuoli@qq.com ");
        myuser.setPassword("holy crash");
        datastore.save(myuser);
    }

    public Datastore getDatastore() {
        return datastore;
    }

// tell Morphia where to find your classes
// can be called multiple times with different packages or classes
// create the Datastore connecting to the default port on the local host
}