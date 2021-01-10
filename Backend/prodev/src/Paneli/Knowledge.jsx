import React from "react";
import { withRouter, Redirect } from 'react-router-dom';

import { Login, Register } from "../Login/index";
import { Vjestine } from "../Vjestine/index";
import { Uposlenici } from "../Uposlenici/index"
import { KnowledgeNavbar } from "../Navigation/index"
import { Certifikati } from "../Certifikati/index"
import { BrowserRouter as Router, Route, Switch } from "react-router-dom"
import { DodavanjeVjestine } from "../DodavanjeVjestine/index"
import './style.scss';
import ChangePassword from "../ChangePassword/ChangePassword";
import axios from "axios"
import getUserFromToken from "../Util/getUserFromToken";
import getBaseUrl from "../Util/getBaseUrl";

class Knowledge extends React.Component {

    odjava = () => {
        document.cookie = "token=; path=/; max-age=-9999999;"
    }

    componentWillMount() {

        var route = getBaseUrl() + '/user/' + getUserFromToken().email
        axios.get(route)
            .then(response => {
                console.log(response)
                if (response.status == 401) {
                    alert("nedozvoljena radnja")
                }
                else if (response.status == 200) {
                    if (response.data.defaultPassword) {
                        alert("Vaš trenutni password je unaprijed zadan. Promijenite password!")
                    }
                }
            })
    }

    render() {
        return (
            <Router>
                <div>
                    <KnowledgeNavbar />
                    <Route path="/knowledge/vjestine" component={Vjestine} />
                    <Route path="/knowledge/dodavanje-vjestine" component={DodavanjeVjestine} />
                    <Route path="/knowledge/certifikati" component={Certifikati} />
                    <Route path="/knowledge/uposlenici" component={Uposlenici} />
                    <Route path="/knowledge/change-password" component={ChangePassword} />
                    <Route path="/" exact component={Login} />
                </div>
                <a href="/" className="odjavaLink" onClick={this.odjava}>Odjava</a>
            </Router>
        )
    }
}

export default Knowledge