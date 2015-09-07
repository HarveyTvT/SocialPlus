package controllers;

import models.Token;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import play.cache.Cache;
import play.data.DynamicForm;
import utils.ConstUtil;
import utils.DbUtil;
import models.User;
import org.mongodb.morphia.Datastore;
import play.*;
import play.data.Form;
import play.mvc.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.EncryptUtil;
import utils.MailUtil;
import views.html.*;
import static utils.ConstUtil.SHA1_SALT;
import static utils.ConstUtil.renrenExpireMax;
import static utils.ConstUtil.weiboExipreMax;

/**
 * Created by harvey on 15-9-2.
 */
public class UserAction extends Controller{

    private  Datastore datastore = DbUtil.getDataStore();


    public UserAction(){
    }


    @Security.Authenticated(Secured.class)
    public Result index(String id){
        Iterable <User> it = datastore.createQuery(User.class).fetch();
        while(it.iterator().hasNext()){
            User user = it.iterator().next();

            if(user.getId().equals(id)){
                return ok(index.render(user));
            }
        }
       return badRequest("Internal Error");
    }
    //登录验证
    public  Result authenticate(){
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String email = dynamicForm.get("email");
        String password = dynamicForm.get("password");

        Iterable <User> it = datastore.createQuery(User.class).fetch();

        while(it.iterator().hasNext()){
            User user = it.iterator().next();
            if(user.getEmail().equals(email)
                    &&user.getPassword().equals(
                        EncryptUtil.SHA1(EncryptUtil.SHA1(password)+SHA1_SALT))
                    ){
                session().clear();
                session("email", email);
                Cache.set("user_" + email, user, 300);
                return redirect(routes.UserAction.index(user.getId()));
            }
        }
            return forbidden("invalid password");
    }
    //渲染注册页面
    public Result register(){
        return ok(register.render());
    }
    //发送邮箱验证邮件
    public  Result sendValidationMail(String id,String email) {
        User user = datastore.createQuery(User.class).filter("email", email).asList().get(0);
        //TODO 校验码的自动生成和验证

        if(!user.isValidated()) {
            String valideCode = EncryptUtil.SHA1(EncryptUtil.SHA1(id)+SHA1_SALT);
            MailUtil.send(email,"SicialPlus邮箱验证邮件",validmail.render(id, email, valideCode).toString());
            return ok("验证邮件发送成功");
        }else{
            return ok("邮箱已经激活");
        }
    }
    //发送设置密码确认邮件
    public Result sendPswMail(){
        String email = Form.form().bindFromRequest().get("email");
        List<User> users = datastore.createQuery(User.class).filter("email", email).asList();
        if(users.isEmpty()){
            return ok("用户不存在");
        }else{
            User user = users.get(0);
            //TODO 校验码的自动生成
            String valideCode =EncryptUtil.SHA1(EncryptUtil.SHA1("mongo")+SHA1_SALT);
            MailUtil.send(email,"SocialPlus找回密码",pswmail.render(email,valideCode).toString());
            return ok("验证邮件发送成功，请到您的邮箱确认！");
        }
    }
    //处理验证链接
    public  Result processValidation(String id, String valideCode) {
        if(valideCode.equals(
                //TODO 校验码的校验
                EncryptUtil.SHA1(EncryptUtil.SHA1(id) + SHA1_SALT)
        )){
            final Query<User> query = datastore.createQuery(User.class)
                    .filter("_id",id);
            final UpdateOperations<User> updateOperation  = datastore.createUpdateOperations(User.class)
                    .set("validated",true);

            final UpdateResults results = datastore.update(query, updateOperation);

            return redirect(routes.UserAction.index(id));
        }else{
            return ok("邮箱认证失败！");
        }

    }
    //新建用户
    public  Result newUser() {
        //TODO 用户名,邮箱,等的正则校验（放到前端）?
        session("user", request().remoteAddress());
        DynamicForm dynamicForm = Form.form().bindFromRequest();

        String id = dynamicForm.get("id");
        String email = dynamicForm.get("email");
        String password = dynamicForm.get("password");
        String password2 = dynamicForm.get("password2");
        if(password.equals(password2)){
            if(!datastore.createQuery(User.class).filter("_id",id).asList().isEmpty()||
                    !datastore.createQuery(User.class).filter("email",email).asList().isEmpty()){
                return forbidden("用户名或邮箱已被使用");
            }
            User user = new User();
            user.setId(id);
            user.setEmail(email);
            user.setPassword(
                    EncryptUtil.SHA1(EncryptUtil.SHA1(password) + SHA1_SALT)
            );
            datastore.save(user);
            session("email", email);
            return redirect(routes.UserAction.index(id));
        }else{
            return forbidden("两次输入的密码不一致");
        }
    }
    //渲染登录页面
    public  Result login() {
        return ok(login.render());
    }
    //渲染输入邮箱页面
    public Result InputEmail(){
        return ok(inputemail.render());
    }
    //渲染输入新密码页面
    public Result InputPass(String email,String valideCode){
        if(valideCode.equals(
                //TODO 校验码的自动生成
                EncryptUtil.SHA1(EncryptUtil.SHA1("mongo")+SHA1_SALT)
            ))
            return ok(setpsw.render(email));
        else
            return ok("验证邮件已过期！");
    }
    //注销用户
    public Result exit(){
        session().clear();
        return redirect(routes.UserAction.login());
    }
    //更改密码
    public Result setPass(String email){
        String password = Form.form().bindFromRequest().get("password");
        String password2 = Form.form().bindFromRequest().get("password2");
        if(password.equals(password2)){
            final Query<User> query = datastore.createQuery(User.class)
                    .filter("email",email);
            final UpdateOperations<User> updateOperation  = datastore.createUpdateOperations(User.class)
                    .set("password",EncryptUtil.SHA1(EncryptUtil.SHA1(password)+SHA1_SALT));

            final UpdateResults results = datastore.update(query, updateOperation);

            return redirect(routes.UserAction.login());
        }else{
            return ok("两次输入密码不一致");
        }
    }
    //用户授权管理
    public Result authAdminRender(){
        String email = session("email");
        if (email == null){
            forbidden("Not login");
        }
        Token tokenList = datastore.createQuery(User.class).filter("email",email).asList().get(0).getToken();
        Map<String,String> weiboToken = tokenList.getWeibo();
        Map<String,String> renrenToken = tokenList.getRenren();
        int weiboExpire = weiboToken.containsKey("expire") ? Integer.parseInt(weiboToken.get("expire")) : 0;
        String weiboAuthed = System.currentTimeMillis() - weiboExpire < weiboExipreMax
                ? "是" : "否";
        int renrenExpire = renrenToken.containsKey("expire") ? Integer.parseInt(renrenToken.get("expire")) : 0;
        String renrenAuthed = System.currentTimeMillis() - renrenExpire < renrenExpireMax
                ? "是" : "否";

        return ok(userAdmin.render(ConstUtil.weiboAuthUrl,weiboAuthed,ConstUtil.renrenAuthUrl,renrenAuthed));
    }
}
