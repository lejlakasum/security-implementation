import React from "react";
import loginSlika from "../logo.png";
import { withRouter, Redirect } from 'react-router-dom';
import { browserHistory, useHistory } from 'react-router';
import axios from 'axios'
import getUserFromToken from "../Util/getUserFromToken"
import getBaseUrl from "../Util/getBaseUrl";

export class Login extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: ''
        };
    }

    handleChange = (e) => {
        this.setState({
            [e.target.name]: e.target.value
        })
    }

    login = () => {
        if (this.state.username === '' && this.state.password === '') alert('Molimo unesite vaše korisničke podatke!');
        else if (this.state.username === '') alert('Molimo unesite vaše korisničko ime!');
        else if (this.state.password === '') alert('Molimo unesite vašu lozinku!');
        else {
            axios.post(getBaseUrl() + '/user/login', {
                password: this.state.password,
                username: this.state.username
            }).then(res => {
                document.cookie = "token=" + res.data.token + "; path=/; max-age=3600;"
                var user = getUserFromToken()
                if (user.role == "admin") {
                    window.location.href = "/admin"
                }
                else if (user.role == "hr") {
                    window.location.href = "/knowledge"
                }
            })
                .catch(err => {
                    console.log(err)
                    alert("Neispravni podaci")
                })
        }
    }

    render() {
        return (<div className="base-container" ref={this.props.containerRef}>
            <div className="stil">Prijava na račun</div>
            <div className="content">
                <div className="image">
                    <img src={loginSlika} />
                </div>
                <div className="form">
                    <div className="form-group">
                        <label htmlFor="username">Email</label>
                        <input type="text" name="username"
                            value={this.state.username}
                            onChange={e => this.handleChange(e)}
                        />
                    </div>
                    <div className="form-group">
                        <label htmlFor="password">Lozinka</label>
                        <input type="password" name="password"
                            value={this.state.password}
                            onChange={e => this.handleChange(e)}
                        />
                    </div>
                </div>
            </div>
            <div className="footer">
                <button type="button" className="btnLogin" onClick={this.login}>
                    Login
                </button>
            </div>
        </div>
        );
    }
}

