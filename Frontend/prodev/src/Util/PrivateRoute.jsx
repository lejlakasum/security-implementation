import React from "react"
import { Route, Redirect } from "react-router-dom"
import isAuthenticated from "./isAuthenticated"

function PrivateRoute(props) {
    return (
        <Route render={() => {
            if (isAuthenticated()) {
                return (props.children);
            } else {
                return (
                    <Redirect to={{
                        pathname: "/", state: { from: props.location }
                    }} />
                );
            }
        }} />
    );
};

export default PrivateRoute;