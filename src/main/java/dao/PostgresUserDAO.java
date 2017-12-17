package dao;

import java.util.List;

import entities.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class PostgresUserDAO implements UserDAO {
    private DataSource source;
    private PreparedStatement preparedStatement;


    public PostgresUserDAO(DataSource source) {
        this.source = source;
    }

    @Override
    public boolean createNew(User user) {
        try (Connection connection = source.getConnection()) {
            String queryInsertNew = "INSERT INTO users (login,email,phone,password,status) VALUES(?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(queryInsertNew);
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getStatus());
            preparedStatement.execute();
            //we can add log about successfully creating
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public Optional<User> getUserByLogin(String login) {
        User user;
        try (Connection connection = source.getConnection()) {
            String selectQuereByLogin = "SELECT * FROM users WHERE login = ?";
            preparedStatement = connection.prepareStatement(selectQuereByLogin);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = parserResultSet(resultSet);
            if (users.size() < 1) {
                user = null;
            } else {
                user = users.get(0);
            }
            return Optional.of(user);
        } catch (SQLException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean updateUser(User newUser, User oldUser) {
        try (Connection connection = source.getConnection()) {
            String updateQuery = "UPDATE users SET login=?," +
                    "email=?, phone=?,password=?,status=? WHERE id=?";
            preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setString(1, newUser.getLogin());
            preparedStatement.setString(2, newUser.getEmail());
            preparedStatement.setString(3, newUser.getPhone());
            preparedStatement.setString(4, newUser.getPassword());
            preparedStatement.setString(5, newUser.getStatus());
            preparedStatement.setInt(6, oldUser.getId());
            preparedStatement.execute();
            //info in log4j
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUserByLogin(String login) {
        try (Connection connection = source.getConnection()) {
            String deleteQueryByLogin = "DELETE FROM users WHERE login = ?";
            preparedStatement = connection.prepareStatement(deleteQueryByLogin);
            preparedStatement.setString(1, login);
            preparedStatement.execute();
            //info in log4j
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> parserResultSet(ResultSet resultSet) {
        List<User> users = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String login = resultSet.getString("login");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String password = resultSet.getString("password");
                String status = resultSet.getString("status");
                int id = resultSet.getInt("id");
                User user = new User();
                user.setId(id);
                user.setEmail(email);
                user.setPhone(phone);
                user.setPassword(password);
                user.setStatus(status);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
}
