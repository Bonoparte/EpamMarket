package db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
public class JdbcConnector {
    final Properties properties = new Properties();

    private void getConnection() {
        try (InputStream resourceAsStream = JdbcConnector.class
                .getResourceAsStream("resources\\jdbc.properties")) {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            log.debug(
                    "\nCUSTOM-DEBUG-IN-ThreadID = \n" + Thread.currentThread().getId() + ""
                    + " \n"
                    + "and ThreadName = " + Thread.currentThread().getName()
                    + "\nmessage is\nDropped down at private void getConnection() in "
                    + "JdbcConnector because of \n" + e
                            .getMessage() + " + " + getClass().getName());
        }

        try {
            Class.forName(properties.getProperty("driver"), true,
                          ComboPooledDataSource.class.getClassLoader());
        } catch (Exception e) {
            log.debug(
                    "\nCUSTOM-DEBUG-IN-ThreadID = \n" + Thread.currentThread().getId() + ""
                    + " \n"
                    + "and ThreadName = " + Thread.currentThread().getName()
                    + "\nmessage is\nDroped down at private void getConnection() in JdbcConnector"
                    + " because of \n"
                    + e.getMessage() + " + " + getClass().getName());
        }
    }
}
