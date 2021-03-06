package lego.shiro;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import lego.pojo.User;
import lego.service.UserService;

public class AuthRealm extends AuthorizingRealm{
	@Autowired
	private UserService userService;
	
	//权限认证
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection arg0) {
		List<String> pList = new ArrayList<String>();
		//获取用户的真实对象
		User user = (User) SecurityUtils.getSubject().getPrincipal();
		//判断用户的角色信息是否是管理员，如果是管理员，展现后台管理的按钮
		if(user.getPermission().getPermissionName().equals("管理员") || user.getPermission().getPermissionName().equals("超级管理员")){
			pList.add("后台管理");
		}
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.addStringPermissions(pList);
		return info;
	}

	//登录认证
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		//shiro安全中心调用realm查询用户的真实信息，传递token数据
		//需要为Shiro安全中心提供真实的用户数据  需要根据用户名查询user对象
		//1.转化为usernamePasswordToken
		UsernamePasswordToken loginToken = (UsernamePasswordToken) token;
		//2.获取用户名
		String username = loginToken.getUsername();
		//3.根据用户名查询数据时，要求用户名必须唯一
		User user = userService.findUserByUserName(username);
		//4.将查询到的用户真实信息返回给shiro安全中心
		/**
		 * 1.principal   表示用户的真实对象
		 * 2.credentials 校验密码时使用的  真实的密码
		 * 3.realmName   realm的名称
		 */
		AuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), this.getName());
		return info;
	}

}
