import React from "react";
import "./App.scss";
import { Login, Register } from "./Login/index";
import Admin from './Paneli/Admin'
import Knowledge from './Paneli/Knowledge'

import { BrowserRouter as Router, Route, Switch } from "react-router-dom"
import PrivateRoute from "./Util/PrivateRoute";


function App() {
  return (
    <Router>
      <div className="App">
        <Route path="/" exact component={Login} />
        <Route path="/admin" component={Admin} />
        <Route path="/knowledge" component={Knowledge} />
      </div>
    </Router>
  );
}

export default App;
