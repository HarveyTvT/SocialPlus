package utils;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import net.sf.ehcache.config.ConfigurationFactory;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by lizhuoli on 15/8/25.
 */
public class DbUtil {

    private static String mongodbURI;
    final static Morphia morphia = new Morphia();
    private static  Datastore datastore;

    public DbUtil(){
    }

    protected static void initDB(){
        Config config = ConfigFactory.load();
        mongodbURI = config.getString("app.mongodb.uri");
        morphia.mapPackage("org.mongodb.morphia.example");
        MongoClientURI uri = new MongoClientURI(mongodbURI);
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
}