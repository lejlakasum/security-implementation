import React, { Component } from 'react'
import axios from 'axios'
import Dropdown from 'react-dropdown';
import getBaseUrl from '../Util/getBaseUrl';

export class Korisnici extends Component {
    constructor(props) {
        super(props)
        this.state = {
            Korisnici: [],
            Header: [
                { Username: "", Email: "", uloga: "", obrisati: false }
            ],
            options: ["admin", "hr"],
            temp: '',
            email: '',
            username: '',
            uloga: '',
            password: '',
            tipUloge: '',
            id: ''
        };
    }

    handleChange = (e, index) => {
        this.state.id = this.state.Korisnici[index].id;;
    }

    componentWillMount() {
        axios.get(getBaseUrl() + '/user/all')
            .then(res => {
                const Korisnici = res.data;
                this.setState({ Korisnici });
            })
    }

    obrisiKorisnika = () => {
        axios.delete(getBaseUrl() + `/user/${this.state.id}`)
            .then(res => {
                var TEMP = [...this.state.Korisnici];
                for (var i = 0; i < TEMP.length; i++) {
                    if (TEMP[i].id === this.state.id) TEMP.splice(i, 1);
                }
                this.setState({ Korisnici: TEMP })
                alert("Uspješno obrisan korisnik!");
            })
    }

    kreirajKorisnika = () => {

        var idUloge = 1
        if (this.state.tipUloge == "hr") {
            idUloge = 2
        }

        if (this.state.username.length < 2 || this.state.username.length > 30 || !this.state.username || this.state.username.trim().length == 0) {
            alert("Unesite ispravan username")
            return
        }

        var regEmail = /^(([^<>()\\.,;:\s@"]+(\.[^<>()\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1, 3}\.[0-9]{1, 3}\.[0-9]{1, 3}\.[0-9]{1, 3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/g
        if (!regEmail.test(this.state.email)) {
            alert("Unesite ispravan email")
            return
        }

        var regPass = /^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\d)(?=.*[!{}<>@^*()_=;:'\-#$%&? "]).*$/g
        if (!regPass.test(this.state.password)) {
            alert("Password mora sadrzavati minimalno 8 karaktera, veliko i malo slovo, broj i znak")
            return
        }
        if (this.state.tipUloge == "") {
            alert("Odaberite ulogu")
            return
        }
        axios.post(getBaseUrl() + '/user/register', {
            username: this.state.username,
            email: this.state.email,
            password: this.state.password,
            role: {
                id: idUloge,
                name: ""
            }
        }).then(response => {
            if (response.status === 201 || response.status === 200) {
                alert("Korisnik uspješno registrovan!")
                window.location.href = "/admin/korisnici"
            }
        }).catch(err => {
            alert("Username i email moraju biti jedinstveni")
        })
    }

    handleChangeUloga = (selectedOption) => {
        if (selectedOption) {
            this.setState({ tipUloge: selectedOption.value })
            this.setState({ temp: selectedOption });
        }
    }

    prikazKorisnika() {
        return this.state.Korisnici.map((korisnik, index) => {
            const { email, username, role, obrisati } = korisnik
            return (
                <tr key={username}>
                    <td>{username}</td>
                    <td>{email}</td>
                    <td>{role.name}</td>
                    <td>{obrisati}
                        <div className="brisanje">
                            <label>
                                <input type="checkbox"
                                    brisati={this.state.checked}
                                    onChange={e => this.handleChange(e, index)}
                                />
                            </label>
                        </div>
                    </td>
                </tr>
            )
        })
    }

    headerTabele() {
        let header = Object.keys(this.state.Header[0])
        return header.map((key, index) => {
            return <th key={index}>{key.toUpperCase()}</th>
        })
    }

    unosNovog = (e) => {
        this.setState({
            [e.target.name]: e.target.value
        })
    }

    render() {
        return (
            <div>
                <h2 id='title'>Postojeći korisnici</h2>
                <div className="glavniDIV">
                    <table id='korisnici'>
                        <tbody>
                            <tr>{this.headerTabele()}</tr>
                            {this.prikazKorisnika()}
                        </tbody>
                    </table>
                    <div className="footer">
                        <button type="submit" className="btnObrisiKorisnika" onClick={this.obrisiKorisnika}>
                            Obriši korisnika
                    </button>
                    </div>
                </div>
                <div className="formaKorisnici">
                    <h2>Unos korisnika</h2>
                    <div className="form-grupaKorisnici">
                        <label htmlFor="username">Username:</label>
                        <input type="text"
                            name="username"
                            value={this.state.username}
                            onChange={e => this.unosNovog(e)} />
                    </div>
                    <div className="form-grupaKorisnici">
                        <label htmlFor="username">Email:</label>
                        <input type="text"
                            name="email"
                            value={this.state.email}
                            onChange={e => this.unosNovog(e)} />
                    </div>
                    <div className="form-grupaKorisnici">
                        <label htmlFor="password">Password:</label>
                        <input type="password"
                            name="password"
                            value={this.state.password}
                            onChange={e => this.unosNovog(e)} />
                    </div>
                    <div className="form-grupaKorisnici">
                        <label htmlFor="uloga">Uloga:</label>
                        <Dropdown options={this.state.options}
                            value={this.state.temp}
                            onChange={(e) => {
                                this.handleChangeUloga(e);
                            }}
                            placeholder="Odaberite ponuđeni tip uloge"
                        />
                    </div>
                    <button className="btnDodajKorisnika" onClick={this.kreirajKorisnika}>
                        Dodavanje novog korisnika
                </button>
                </div>
            </div>
        )
    }
}

