import Axios from "axios";
import tokenExists from "./tokenExists"
import axios from "axios"
import getToken from "./getToken";

async function isAuthenticated(role) {

    if (tokenExists()) {
        var route = "https://localhost:8443/user/validate?role=" + role
        const response = await axios.post(route, {
            token: getToken("token")
        })
        if (response.status == 200) {
            return true
        }
    }

    return false
}

export default isAuthenticated;