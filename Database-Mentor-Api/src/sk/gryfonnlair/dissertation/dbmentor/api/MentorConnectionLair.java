package sk.gryfonnlair.dissertation.dbmentor.api;

import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.*;
import sk.gryfonnlair.dissertation.dbmentor.api.rule.DatabaseMentorRule;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 1/8/14
 * Time: 11:30 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class MentorConnectionLair implements Serializable {

    public static final long serialVersionUID = 4711148011192901606L;
    private String db;
    private String user;
    private String pass;
    private String url;

    protected MentorConnectionLair(String db, String user, String pass, String url) {
        this.db = db;
        this.user = user;
        this.pass = pass;
        this.url = url;
    }

    /**
     * @param connectionProperties v tvare "property=value"
     * @return connection
     * @throws ClassNotFoundException
     * @throws java.sql.SQLException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public abstract Connection createConnection(String... connectionProperties) throws ClassNotFoundException, SQLException, IllegalAccessException, InstantiationException;

    //QUICK
    public abstract QuickCodeCallResult executeQuickCode(String sqlCode) throws Exception;

    //PROCEDURY
    public abstract String[] getAllProceduresNames() throws Exception;

    public abstract Map<String, String> getProcedureDetailInfo(String procedureName) throws Exception;

    public abstract String getProcedureCode(String procedureName) throws Exception;

    public abstract List<ProcedureArgInfo> getProcedureArgumentsInfo(String procedureName) throws Exception;

    public abstract ProcedureCallResult callProcedure(String procedureName, ProcedureArgInfo[] args) throws Exception;

    //FUNKCIE
    public abstract String[] getAllFunctionsNames() throws Exception;

    public abstract Map<String, String> getFunctionDetailInfo(String functionName) throws Exception;

    public abstract String getFunctionCode(String functionName) throws Exception;

    public abstract List<FunctionArgInfo> getFunctionArgumentsInfo(String functionName) throws Exception;

    public abstract String callFunction(String functionName, FunctionArgInfo[] args) throws Exception;

    //DEBUGGER

    /**
     * Podla implementacie ake mam pravidla v baliku, ulozit si pole interface DMRule a pytat si
     * DebuggerRuleInfo cez get
     *
     * @return pole pravidiel
     */
    public abstract DebuggerRuleInfo[] getDebuggerActiveRules();

    /**
     * Parsuje string podla pravidiel a podla ich poradia v list 0,1...
     *
     * @param sqlStatement sql statement to parse
     * @param chosenRules  pole pravidiel v poradi ako m sa maju aplikovat
     * @return Map<String,List<String>> < K= RULE.name, V= List<String> sqls k nemu>
     */
    public abstract Map<String, List<String>> parseDebuggerSQLStatement(String sqlStatement, DebuggerRuleInfo[] chosenRules) throws Exception;

    /**
     * Volam ju po kliknuti na rozdeleny select, ci ze na cast a ma spustit jednoducho select na db
     *
     * @param sqlStatement sql stmt
     * @return zaobalena MCLResultSetTable + cas za kt sa vykonal prizkaz
     */
    public abstract DebuggerRunResult runSimpleStatement(String sqlStatement) throws Exception;

    /**
     * Na zaklade tokenu mi vytvori ako factory pravidlo
     *
     * @param ruleToken token string
     * @return implementacia pravidla
     */
    public abstract DatabaseMentorRule createRule(String ruleToken) throws ClassNotFoundException;

    //UTIL
    protected final MCLResultSetTable getMCLResultSetTable(final ResultSet resultSet) throws SQLException {
        System.out.println("MentorConnectionLair.getMCLResultSetTable > citam RESULT SET");
        MCLResultSetTable mclTable = new MCLResultSetTable();

        ResultSetMetaData meta = resultSet.getMetaData();
        int columnCount = meta.getColumnCount();
        //pole klucov, inak equals ma false na hashmape
        String[] mapKeys = new String[columnCount];
        //tvorba listov do hasmapy
        for (int column = 1; column <= columnCount; column++) {
            //pole klucov, inak equals ma false na hashmape
            mapKeys[column - 1] = meta.getColumnName(column);
            mclTable.getTable().put(mapKeys[column - 1], new ArrayList<String>(0));
        }
        //plnenie Listov
        while (resultSet.next()) {
            for (int column = 1; column <= columnCount; column++) {
                mclTable.getTable().get(mapKeys[column - 1]).add(resultSet.getString(column));
            }
        }
        return mclTable;
    }

    protected String getDb() {
        return db;
    }

    protected String getUser() {
        return user;
    }

    protected String getPass() {
        return pass;
    }

    protected String getUrl() {
        return url;
    }
}
