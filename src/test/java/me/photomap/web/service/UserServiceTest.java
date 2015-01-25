package me.photomap.web.service;

import me.photomap.web.data.repo.UserRepo;
import me.photomap.web.data.repo.model.Session;
import me.photomap.web.data.repo.model.User;
import me.photomap.web.service.exceptions.LoginFailedException;
import me.photomap.web.service.exceptions.ResourceNotFoundException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;


public class UserServiceTest extends  IntegrateBase{




        private static final String userName = "testuser";
        private static final String userEmail = "testuser@test.com";
        private static final String userPass = "MySuperSecurePass";

        private User registered = null;

        private List<User> testUsers = new LinkedList<User>();

        @Autowired UserService  userService;
        @Autowired
        UserRepo userRepo;

        @Test
        public void testOk(){
                Assert.assertNotNull(userService);

        }

        @Test
        public void testRegisterUserOk() throws Exception{
                User u = new User();
                u.setUserName(userName);
                u.setPassword(userPass);
                u.setEmail(userEmail);
                registered = userService.registerUser(u);
                testUsers.add(registered);
                Assert.assertNotNull(registered);
                Assert.assertSame(registered.getEmail(),u.getEmail());

        }

        @Test
        public void testUserLoginOK()throws Exception{
                User u = new User();
                u.setUserName(userName);
                u.setPassword(userPass);
                u.setEmail(userEmail);
                u= userService.registerUser(u);
                System.out.println(u.getPassword());
                testUsers.add(u);
                u.setPassword(userPass);
                Session sess = userService.loginUser(u);
                Assert.assertNotNull(sess);
        }

        @Test(expected = LoginFailedException.class)
        public void testUserLoginNotOK()throws Exception{
                User u = new User();
                u.setUserName(userName);
                u.setPassword(userPass);
                u.setEmail(userEmail);
                u= userService.registerUser(u);
                System.out.println(u.getPassword());
                testUsers.add(u);
                u.setPassword(userPass+"sdsd");
                Session sess = userService.loginUser(u);
                Assert.assertNotNull(sess);
        }

        @Test(expected = ResourceNotFoundException.class)
        public void testUserLoginNotFound()throws Exception{
                User u = new User();
                u.setUserName("notfound");
                u.setPassword(userPass);
                u.setEmail(userEmail);
                u.setPassword(userPass);
                Session sess = userService.loginUser(u);
        }

        @Test
        public void testVerifyUserSession ()throws Exception{
                User u = new User();
                u.setUserName(userName+"verify");
                u.setPassword(userPass);
                u.setEmail(userEmail);
                u= userService.registerUser(u);
                testUsers.add(u);
                u.setPassword(userPass);
                Session sess = userService.loginUser(u);
                Assert.assertNotNull(sess);
                u =  userService.verifyUserAndSession(u.getId().toString(), sess.getSessionId());
                Assert.assertNotNull(u);
                Assert.assertEquals(u.getEmail(), userEmail);

                userService.logoutUser(u);
                u =  userService.verifyUserAndSession(u.getEmail(), sess.getSessionId());
                Assert.assertNull(u);
        }

        @After
        public void tearDown(){
                for(User u : testUsers){
                        userRepo.delete(u);
                }
        }

}
