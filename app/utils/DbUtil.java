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
    }

    protected static  void initDB(){
        morphia.mapPackage("org.mongodb.morphia.example");
        MongoClientURI uri = new MongoClientURI("mongodb://localhost:27017/socialplus");
        datastore = morphia.createDatastore(new MongoClient(uri), "socialplus");
        datastore.ensureIndexes();
    }

    //singleton of datastore
    public static Datastore getDataStore(){
        if(datastore==null){
            initDB();
        }
        return datastore;
    }

//    public static void main(String[] args){
//        Datastore datastore = DbUtil.getDataStore();
//        if(datastore!=null) {
//            User myuser = new User();
//            myuser.setId("ailuoli3");
//            myuser.setEmail("ailuoli2@qq.com ");
//            myuser.setPassword("holy@@@@ crash");
//            datastore.save(myuser);
//        }else{
//            System.out.println("nothing");
//        }
//    }


// tell Morphia where to find your classes
// can be called multiple times with different packages or classes
// create the Datastore connecting to the default port on the local host
}