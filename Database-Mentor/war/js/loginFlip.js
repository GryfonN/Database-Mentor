/**
 * Created with IntelliJ IDEA.
 * User: gryfonn
 * Date: 05.02.14
 * Time: 17:13
 * To change this template use File | Settings | File Templates.
 */
function flipToAdmin() {
    $('#userCard').hide();
    $('#adminCard').fadeIn(1000);
}

function flipToUser() {
    $('#userCard').fadeIn(1000);
    $('#adminCard').hide();
}
