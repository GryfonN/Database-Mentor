package sk.gryfonnlair.dissertation.dbmentor.server.services;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import sk.gryfonnlair.dissertation.dbmentor.api.MentorConnectionLair;
import sk.gryfonnlair.dissertation.dbmentor.api.gwtdto.QuickCodeCallResult;
import sk.gryfonnlair.dissertation.dbmentor.server.ServerUtil;
import sk.gryfonnlair.dissertation.dbmentor.server.SessionKeys;
import sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException;
import sk.gryfonnlair.dissertation.dbmentor.shared.services.QuickService;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 3/11/14
 * Time: 11:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class QuickServiceImpl extends RemoteServiceServlet implements QuickService {

    /**
     * Vykona SQL kt je v stringu
     *
     * @param sqlStatement
     * @return QuickCodeCallResult
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    @Override
    public QuickCodeCallResult callSQLStatement(String sqlStatement) throws RPCServiceException {
        MentorConnectionLair mentorConnectionLair = ServerUtil.getMCLFromRequest(this.getThreadLocalRequest());
        try {
            return mentorConnectionLair.executeQuickCode(sqlStatement);
        } catch (Exception e) {
            throw new RPCServiceException(e.getMessage());
        }
    }

    /**
     * Pole stringov pre qucik code do karty
     *
     * @return String[] alebo null
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    @Override
    public String[] getSQLSyntax() throws RPCServiceException {
        return new String[]{};
    }

    /**
     * Vrati posledny uploadnuty script prostrednictvom serveltu z session
     *
     * @return String sql kod
     * @throws sk.gryfonnlair.dissertation.dbmentor.shared.dto.RPCServiceException
     *
     */
    @Override
    public String getUploadedSQLScript() throws RPCServiceException {
        Object sqlCodeObject = this.getThreadLocalRequest().getSession().getAttribute(SessionKeys.SESSION_ATTRIBUTE_KEY_USER_QUICKCODE_UPLOAD_FILE);
        if (sqlCodeObject instanceof String) {
            String sqlCodeString = (String) sqlCodeObject;
            if (sqlCodeString.isEmpty()) {
                throw new RPCServiceException("No text in memory on server side !");
            }
            return sqlCodeString;
        } else {
            throw new RPCServiceException("No String stored in session");
        }
    }
}