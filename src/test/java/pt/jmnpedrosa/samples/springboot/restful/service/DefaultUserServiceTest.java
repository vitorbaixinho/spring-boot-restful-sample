package pt.jmnpedrosa.samples.springboot.restful.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pt.jmnpedrosa.samples.springboot.restful.error.UserAlreadyExistsException;
import pt.jmnpedrosa.samples.springboot.restful.error.UserException;
import pt.jmnpedrosa.samples.springboot.restful.error.UserNotFoundException;
import pt.jmnpedrosa.samples.springboot.restful.model.User;

@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultUserServiceTest {

  @Mock
  private Set<User> userSet;

  @InjectMocks
  private UserService userService = new DefaultUserService();

  @Test
  public void test_getAllUsers() throws Exception {
    User user1 = new User("userName1");
    User user2 = new User("userName2");
    when(userSet.toArray()).thenReturn(new Object[]{user1, user2});
    List<User> result = userService.getAllUsers();
    assertNotNull(result);
    assertEquals(2, result.size());
  }

  @Test
  public void test_getUser_OK() throws Exception {
    User user = new User("userName");
    user.setEmail("email");
    user.setFirstName("firstName");
    user.setLastName("lastName");
    user.setTelephone("telephone");
    user.setAddress("address");
    user.setCountry("country");
    when(userSet.stream()).then(i -> Stream.of(user));

    User result = userService.getUser("userName");
    assertEquals(user, result);
  }

  @Test(expected = UserException.class)
  public void test_getUser_MissingInput() throws Exception {
    userService.getUser(null);
  }

  @Test(expected = UserNotFoundException.class)
  public void test_getUser_UserNotFound() throws Exception {
    userService.getUser("userName");
  }

  @Test
  public void test_createUser_OK() throws Exception {
    User user = new User("userName");
    user.setEmail("email@email.com");
    user.setFirstName("firstName");
    user.setLastName("lastName");
    user.setTelephone("123456789");
    user.setAddress("address");
    user.setCountry("country");

    User result = userService.createUser(user);
    assertEquals(user, result);
  }

  @Test(expected = UserAlreadyExistsException.class)
  public void test_createUser_AlreadyExists() throws Exception {
    User user = new User("userName");
    user.setEmail("email@email.com");
    user.setFirstName("firstName");
    user.setLastName("lastName");
    user.setTelephone("123456789");
    user.setAddress("address");
    user.setCountry("country");
    when(userSet.stream()).then(i -> Stream.of(user));

    userService.createUser(user);
  }

  @Test
  public void test_updateUser_OK() throws Exception {
    User user = new User("userName");
    user.setEmail("email@email.com");
    user.setFirstName("firstName");
    user.setLastName("lastName");
    user.setTelephone("123456789");
    user.setAddress("address");
    user.setCountry("country");
    when(userSet.stream()).then(i -> Stream.of(user));

    User result = userService.updateUser(user);
    assertEquals(user, result);
  }

  @Test(expected = UserNotFoundException.class)
  public void test_updateUser_UserNotFound() throws Exception {
    User user = new User("userName");
    user.setEmail("email@email.com");
    user.setFirstName("firstName");
    user.setLastName("lastName");
    user.setTelephone("123456789");
    user.setAddress("address");
    user.setCountry("country");

    userService.updateUser(user);
  }

  @Test
  public void test_deleteUser_OK() throws Exception {
    User user = new User("userName");
    user.setEmail("email@email.com");
    user.setFirstName("firstName");
    user.setLastName("lastName");
    user.setTelephone("123456789");
    user.setAddress("address");
    user.setCountry("country");
    when(userSet.stream()).then(i -> Stream.of(user));

    User result = userService.deleteUser("userName");
    assertEquals(user, result);
  }

  @Test(expected = UserNotFoundException.class)
  public void test_deleteUser_UserNotFound() throws Exception {
    userService.deleteUser("userName");
  }

}
