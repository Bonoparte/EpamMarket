package services;

import dao.PostgresUserDAO;
import dao.UserDAO;
import entities.User;

import java.util.List;

public class AdminService {

    //--------------------------------SINGLETON------------------------------------------

    private static AdminService instance = null;

    synchronized public static AdminService getInstance() {
        if (instance == null)
            instance = new AdminService(PostgresUserDAO.getInstance());
        return instance;
    }

    private static AdminService testInstance;

    synchronized public static AdminService getTestInstance() {
        if (testInstance == null)
            testInstance = new AdminService(PostgresUserDAO.getTestInstance());
        return testInstance;
    }

    private AdminService(UserDAO userDAO) {
        this.userDao = userDAO;
    }

    //--------------------------------------------------------------------------

    private UserDAO userDao;

    synchronized public List<User> getUserList() {
        List<User> users = userDao.getAllUsers();
        users.sort(null);
        return users;
    }

}

