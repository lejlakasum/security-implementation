import tokenExists from "./tokenExists"

function isAuthenticated() {

    return tokenExists()
}

export default isAuthenticated;