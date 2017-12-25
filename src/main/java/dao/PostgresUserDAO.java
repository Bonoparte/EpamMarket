package dao;

import db.DataSourceInit;
import entities.User;
import lombok.extern.log4j.Log4j2;
import lombok.val;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
public class PostgresUserDAO implements UserDAO {

    //--------------------------------SINGLETON------------------------------------------

    private static PostgresUserDAO instance = null;

    synchronized public static PostgresUserDAO getInstance() {
        if (instance == null)
            instance = new PostgresUserDAO(DataSourceInit.getPostgres());

        return instance;

    }

    private static PostgresUserDAO testInstance;

    synchronized public static PostgresUserDAO getTestInstance() {
        if (testInstance == null)
            testInstance = new PostgresUserDAO(DataSourceInit.getH2());

        return testInstance;
    }

    private PostgresUserDAO(DataSource dataSource) {
        this.DATA_SOURCE = dataSource;
    }

    //--------------------------------DATA-SOURCE---------------------------------------------

    private DataSource DATA_SOURCE;

    //--------------------------------QUERIES---------------------------------------------

    private static final String INSERT_QUERY_NEW =
            "INSERT INTO users (login,email,phone,password,status) VALUES(?,?,?,?,?)";
    private static final String SELECT_QUERY_BY_LOGIN =
            "SELECT * FROM users WHERE login = ?";
    private static final String SELECT_QUERY_BY_ID =
            "SELECT * FROM users WHERE user_id = ?";
    private static final String UPDATE_QUERY =
            "UPDATE users SET email=?, phone=?, password=?, status=? WHERE login=?";
    private static final String DELETE_QUERY_BY_LOGIN =
            "DELETE FROM users WHERE login = ?";
    private static final String DELETE_QUERY_BY_ID =
            "DELETE FROM users WHERE user_id = ?";
    private static final String GET_ALL_QUERY =
            "SELECT * FROM users";

    //--------------------------------DAO-METHODS---------------------------------------------

    @Override
    synchronized public boolean addUser(User user) {
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY_NEW)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getStatus());
            preparedStatement.execute();
            log.info("Successfully creating public boolean addUser(User user) in PostgresUserDAO");
            return true;
        } catch (SQLException e) {
            log.error("Dropped down " + this.getClass().getCanonicalName() + " because of \n" + e.getMessage());
            return false;
        }

    }

    @Override
    synchronized public Optional<User> getUserById(Integer id) {
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY_BY_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) return Optional.empty();
                val user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("status")
                );
                return Optional.ofNullable(user);
            }
        } catch (SQLException e) {
            log.error("Droped down " + this.getClass().getCanonicalName() + " because of \n" + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    synchronized public Optional<User> getUserByLogin(String login) {
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (!resultSet.next()) return Optional.empty();
                val user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("status")
                );
                return Optional.ofNullable(user);
            }
        } catch (SQLException e) {
            log.error("Dropped down " + this.getClass().getCanonicalName() + " because of \n" + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    synchronized public boolean updateUser(User newUser, User oldUser) {
        //FIXME do we need check if newUser or oldUser is null? - yes we do !
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY)) {
            preparedStatement.setString(1, newUser.getEmail());
            preparedStatement.setString(2, newUser.getPhone());
            preparedStatement.setString(3, newUser.getPassword());
            preparedStatement.setString(4, newUser.getStatus());
            preparedStatement.setString(5, newUser.getLogin());
            preparedStatement.execute();
            log.info("User successfully updated by updateUser method in PostgresUserDao !");
            return true;
        } catch (SQLException e) {
            log.error("Dropped down " + this.getClass().getCanonicalName() + " because of \n" + e.getMessage());
            return false;
        }
    }

    @Override
    synchronized public boolean deleteUserByLogin(String login) {
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY_BY_LOGIN)) {
            preparedStatement.setString(1, login);
            preparedStatement.execute();
            //TODO info in log4j
            log.info("User successfully delete by deleteUserByLogin method in PostgresUserDao !");
            return true;
        } catch (SQLException e) {
            log.error("Droped down " + this.getClass().getCanonicalName() + " because of \n" + e.getMessage());
            return false;
        }
    }

    @Override
    synchronized public boolean deleteUserById(Integer id) {
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY_BY_ID)) {
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            log.info("User successfully deleted by deleteUserById method in PostgresUserDao");
            return true;
        } catch (SQLException e) {
            log.error("Dropped down " + this.getClass().getCanonicalName() + " because of \n" + e.getMessage());
            return false;
        }
    }

    @Override
    synchronized public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection connection = DATA_SOURCE.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_QUERY);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                users.add(new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("login"),
                        resultSet.getString("password"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("status")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

}