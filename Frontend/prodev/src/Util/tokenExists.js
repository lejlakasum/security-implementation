const { default: getToken } = require("./getToken");

function tokenExists() {
    var token = getToken("token")
    if (token != null & token != "") {
        return true
    }
    return false;
}

export default tokenExists;