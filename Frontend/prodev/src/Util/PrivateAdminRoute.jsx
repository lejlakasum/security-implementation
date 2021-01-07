import React, { useEffect, useState } from "react"
import { Route, Redirect } from "react-router-dom"
import isAuthenticated from "./isAuthenticated"

function PrivateAdminRoute(props) {

    const [auth, setAuth] = useState(false)

    useEffect(() => {
        isAuthenticated("admin").then(response => {
            setAuth(response)
        })
    }, [auth])
    return (
        <Route render={() => {

            if (auth) {
                return (props.children);
            } else {
                return (
                    <div>
                        <div>Not authorized</div>
                        <button onClick={() => window.location.href = "/"}>Login</button>
                    </div>
                );
            }
        }} />
    );
};

export default PrivateAdminRoute;