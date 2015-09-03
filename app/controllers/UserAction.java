package controllers;

import java.util.List;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import utils.DbUtil;
import models.User;
import org.mongodb.morphia.Datastore;
import play.*;
import play.data.Form;
import play.mvc.*;

import utils.MailUtil;
import views.html.*;

import static play.mvc.Results.*;

/**
 * Created by harvey on 15-9-2.
 */
public class UserAction extends Controller{

    private final Datastore datastore = DbUtil.getDataStore();

    public UserAction(){
    }

    //登录
    public Result login(){
        User user = Form.form(User.class).bindFromRequest().get();
        List<User> temp = datastore.createQuery(User.class)
                    .filter("email ==", user.getEmail())
                    .filter("password ==",user.getPassword()).asList();
        if(temp!=null){
            return ok(socialplus.render(user));
        }else{
            return ok(index.render(Form.form(User.class)));
        }
    }

    //发送验证邮件
    public Result sendValidationMail(String email){
        //TODO 验证码的随机生成与有效期机制
        String valideCode = "phantom";
        MailUtil.send(email, validmail.render(email, valideCode).toString());
        return ok("验证邮件发送成功");
    }

    //处理邮箱验证
    public Result processValidation(String email,String valideCode){
        if(valideCode.equals("phantom")){
            final Query<User> query = datastore.createQuery(User.class)
                    .filter("email ==",email);
            final UpdateOperations<User> updateOperation  = datastore.createUpdateOperations(User.class)
                    .set("validated",true);

            final UpdateResults results = datastore.update(query, updateOperation);

            return ok(socialplus.render(query.asList().get(0)));
        }else{
            return ok("邮箱认证失败！");
        }

    }

    //注册
    public Result newUser(){
        session("user", request().remoteAddress());
        User user = Form.form(User.class).bindFromRequest().get();
        //TODO 合法性检查
        datastore.save(user);
        return redirect(routes.UserAction.login());
    }

}
