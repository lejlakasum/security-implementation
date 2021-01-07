import React from "react";
import "./App.scss";
import { Login, Register } from "./Login/index";
import Admin from './Paneli/Admin'
import Knowledge from './Paneli/Knowledge'

import { BrowserRouter, Route, Switch, useHistory } from "react-router-dom"
import PrivateAdminRoute from "./Util/PrivateAdminRoute";
import PrivateHrRoute from "./Util/PrivateHrRoute";


function App() {

  return (
    <div>
      <BrowserRouter>
        <Switch>
          <Route path="/" exact><Login /></Route>
          <PrivateAdminRoute path="/admin"><Admin /></PrivateAdminRoute>
          <PrivateHrRoute path="/knowledge"><Knowledge /></PrivateHrRoute>
        </Switch>
      </BrowserRouter>
    </div>
  );
}

export default App;
