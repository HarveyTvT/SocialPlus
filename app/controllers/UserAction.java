package controllers;

import models.Account.Token;
import models.Account.User;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.ConstUtil;
import utils.EncryptUtil;
import utils.MailUtil;
import views.html.*;

import java.util.Map;

import static utils.ConstUtil.*;

/**
 * Created by harvey on 15-9-2.
 */
public class UserAction extends Controller{

    public UserAction(){
    }


    @Security.Authenticated(Secured.class)
    public Result index(){
        String email = session("email");
        User user = User.getUser(email);
        String weiboAuthUrl = ConstUtil.weiboAuthUrl;
        if(user!=null)
            return ok(query.render(user,weiboAuthUrl));
        else
            return badRequest("Internal Error");
    }
    //登录验证
    public  Result authenticate(){
        DynamicForm dynamicForm = Form.form().bindFromRequest();
        String email = dynamicForm.get("email");
        String password = dynamicForm.get("password");
        password =  EncryptUtil.SHA1(EncryptUtil.SHA1(password)+SHA1_SALT);

        if(User.Authenticate(email,password)!=null){
            session().clear();
            session("email", email);
            return redirect(routes.UserAction.index());
        }else{
            return forbidden("invalid password");
        }
    }
    //渲染注册页面
    public Result register(){
        return ok(register.render());
    }
    //发送邮箱验证邮件
    public  Result sendValidationMail(String id,String email) {
        User user = User.getUser(email);
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
        User user = User.getUser(email);
        if(user==null){
            return ok("用户不存在");
        }else{
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
            User.validEmail(id);
            return redirect(routes.UserAction.index());
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
            password = EncryptUtil.SHA1(EncryptUtil.SHA1(password)+SHA1_SALT);
            if(!User.isAvailable(email, password)){
                return forbidden("用户名或邮箱已被使用");
            }
            User user = new User();
            user.setId(id);
            user.setEmail(email);
            user.setPassword(
                    password
            );
            User.saveUser(user);
            session("email", email);
            return redirect(routes.UserAction.index());
        }else{
            return forbidden("两次输入的密码不一致");
        }
    }
    //渲染登录页面
    public  Result login() {
        String weiboAuthUrl = ConstUtil.weiboAuthUrl;
        if (session("email") != null){
            return redirect(routes.UserAction.index());
        }
        else{
            return ok(login.render(weiboAuthUrl));
        }
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
            password = EncryptUtil.SHA1(EncryptUtil.SHA1(password)+SHA1_SALT);
            User.updatePsw(email,password);
            return redirect(routes.UserAction.login());
        }else{
            return ok("两次输入密码不一致");
        }
    }
    //用户授权管理
    @Security.Authenticated(Secured.class)
    public Result authAdminRender(){
        String email = session("email");
        Token tokenList = User.getUser(email).getToken();
        Map<String,String> weiboToken = tokenList.getWeibo();
        Map<String,String> renrenToken = tokenList.getRenren();
        Map<String,String> twitteToken = tokenList.getTwitter();

        Long weiboExpire = weiboToken.containsKey("expire") ? Long.parseLong(weiboToken.get("expire")) : 0;
        String weiboAuthed = System.currentTimeMillis() < weiboExpire
                ? "是" : "否";
        Long renrenExpire = renrenToken.containsKey("expire") ? Long.parseLong(renrenToken.get("expire")) : 0;
        String renrenAuthed = System.currentTimeMillis() < renrenExpire
                ? "是" : "否";
        String twitterAuthed = twitteToken.containsKey("token") ? "是" : "否";

        return ok(userAdmin.render(ConstUtil.weiboAuthUrl,weiboAuthed,ConstUtil.renrenAuthUrl,renrenAuthed,
                ConstUtil.twitterAuthUrl,twitterAuthed));
    }
}
