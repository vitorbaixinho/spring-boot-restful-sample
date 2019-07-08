package pt.jmnpedrosa.samples.springboot.restful.service;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.jmnpedrosa.samples.springboot.restful.error.UserAlreadyExistsException;
import pt.jmnpedrosa.samples.springboot.restful.error.UserException;
import pt.jmnpedrosa.samples.springboot.restful.error.UserNotFoundException;
import pt.jmnpedrosa.samples.springboot.restful.model.User;

@Service
public class DefaultUserService implements UserService {

  private static final Logger LOG = LoggerFactory.getLogger(DefaultUserService.class);

  @Autowired
  private Set<User> userSet;

  @Override
  public User getUser(String userName) throws UserException {
    if (userName == null) {
      LOG.error("userName cannot be null.");
      throw new UserException("Missing input.");
    }

    User user = userSet.stream()
        .filter(u -> u.getUserName().equals(userName))
        .findFirst().orElse(null);

    if (user == null) {
      LOG.error("User [{}] not found.", userName);
      throw new UserNotFoundException();
    }

    LOG.info("Found user [{}]. Returning.", userName);
    return user;
  }

  @Override
  public User createUser(User user) throws UserException {
    User existingUser = null;
    try {
      existingUser = getUser(user.getUserName());
      if (existingUser != null) {
        LOG.error("Unable to create user [{}]. This username already exists.", user.getUserName());
        throw new UserAlreadyExistsException();
      }
    } catch (UserNotFoundException e) {
      LOG.info("User [{}] does not exist. Creating...", user.getUserName());
    }

    try {
      userSet.add(user);
    } catch (Exception e) {
      LOG.error("Error creating user [{}].", user.getUserName());
      throw new UserException("Unable to insert user.", e);
    }

    LOG.info("Added new user [{}].", user.getUserName());
    return user;
  }
}
