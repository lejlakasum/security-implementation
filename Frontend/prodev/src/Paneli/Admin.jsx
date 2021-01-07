import React from "react";
import { withRouter, Redirect } from 'react-router-dom';

import { Login, Register } from "../Login/index";
import { Korisnici, Tabela } from "../Korisnici/index";
import { Uposlenici } from "../Uposlenici/index"
import AdminNavbar from '../Navigation/AdminNavbar'
import { BrowserRouter as Router, Route, Switch } from "react-router-dom"
import './style.scss';

class Admin extends React.Component {

    odjava = () => {
        document.cookie = "token=; path=/; max-age=-9999999;"
    }

    render() {
        return (
            <Router>
                <div>
                    <AdminNavbar />
                    <Route path="/admin/korisnici" component={Korisnici} />
                    <Route path="/admin/uposlenici" component={Uposlenici} />
                    <Route path="/" exact component={Login} />
                </div>
                <a href="/" className="odjavaLink" onClick={this.odjava}>Odjava</a>
            </Router>
        )
    }
}

export default Admin