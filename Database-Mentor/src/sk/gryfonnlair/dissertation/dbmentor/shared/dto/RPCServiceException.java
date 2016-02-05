package sk.gryfonnlair.dissertation.dbmentor.shared.dto;

/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 12.02.14
 * Time: 09:54
 * <br/>
 * Excetion kt vyhadzujem v RPC komunikacii na oboznamenie clienta s chybou na strane servera
 */
public class RPCServiceException extends Exception {

    public RPCServiceException() {
    }

    public RPCServiceException(String message) {
        super(message);
    }

    public RPCServiceException(Throwable throwable) {
        super(throwable);
    }
}
