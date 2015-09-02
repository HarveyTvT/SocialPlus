package utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import models.User;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by lizhuoli on 15/8/25.
 */
public class DbUtil {

    final static Morphia morphia = new Morphia();
    private static  Datastore datastore;

    public DbUtil(){
        initDB();
    }

    protected static  Datastore initDB(){
        morphia.mapPackage("org.mongodb.morphia.example");
        MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/socialplus");
        datastore = morphia.createDatastore(new MongoClient(uri), "socialplus");
        datastore.ensureIndexes();
        return datastore;
    }

    public static Datastore getDataStore(){
        return datastore;
    }

    public static void main(String[] args){
        Datastore datastore = DbUtil.initDB();
        User myuser = new User();
        myuser.setId("ailuoli2");
        myuser.setEmail("ailuoli2@qq.com ");
        myuser.setPassword("holy@@@@ crash");
        datastore.save(myuser);
    }


// tell Morphia where to find your classes
// can be called multiple times with different packages or classes
// create the Datastore connecting to the default port on the local host
}