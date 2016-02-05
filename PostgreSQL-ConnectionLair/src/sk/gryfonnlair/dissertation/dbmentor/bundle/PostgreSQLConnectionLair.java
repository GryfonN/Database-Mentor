package sk.gryfonnlair.dissertation.dbmentor.bundle;

import sk.gryfonnlair.dissertation.dbmentor.api.MentorConnectionLair;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/16/14
 * Time: 5:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class PostgreSQLConnectionLair extends MentorConnectionLair {


    public PostgreSQLConnectionLair(String db, String user, String pass, String url) {
        super(db, user, pass, url);
    }

    /**
     * @param connectionProperties v tvare "property=value"
     * @return connection
     * @throws ClassNotFoundException
     * @throws java.sql.SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Override
    public Connection createConnection(String... connectionProperties) throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException {
        String driver = "org.postgresql.Driver";
        StringBuffer sb = new StringBuffer("jdbc:postgresql://").append(getUrl()).append('/').append(getDb());
        if (connectionProperties != null && connectionProperties.length > 0) {
            sb.append('?');
            for (int i = 0; i < connectionProperties.length; i++) {
                if (i != 0) {
                    sb.append(';');
                }
                sb.append(connectionProperties[i]);
            }

        }
        Class.forName(driver).newInstance();
        System.out.println("PostgreSQLConnectionLair.createConnection > PostgreSQL Class.forName DONE !");
        return DriverManager.getConnection(sb.toString(), getUser(), getPass());
    }

    @Override
    public String[] getAllProceduresNames() throws Exception {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getProcedureDetailInfo(String procedureName) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getProcedureCode(String procedureName) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<ProcedureArgInfo> getProcedureArgumentsInfo(String procedureName) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ProcedureCallResult callProcedure(String procedureName, ProcedureArgInfo[] args) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] getAllFunctionsNames() throws Exception {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Map<String, String> getFunctionDetailInfo(String functionName) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getFunctionCode(String functionName) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<FunctionArgInfo> getFunctionArgumentsInfo(String functionName) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String callFunction(String functionName, FunctionArgInfo[] args) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DebuggerRule[] getDebuggerActiveRules() {
        return new DebuggerRule[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public QuickCodeCallResult executeQuickCode(String sqlCode) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
