import React, { Component } from 'react'
import axios from 'axios'
import Dropdown from 'react-dropdown';
import DatePicker from "react-datepicker";
import getBaseUrl from '../Util/getBaseUrl';

export class Certifikati extends Component {
    constructor(props) {
        super(props)
        this.state = {
            Certifikati: [],
            Header: [
                { Ime_Certifikata: "", Vještina: "", Datum_Izdavanja: "", Datum_Isteka: "", Uposlenik: "", obrisati: false }
            ],
            options: [],
            uposlenici: [],
            uposlenikCertifikata: [], // Za dobavljanje korisnika za kojeg je certifikat
            vjestinaCertifikata: [], // Za dobavljanje vjestine za koju je certfikat
            ime: '',
            izdavanje: new Date(),
            istek: new Date(),
            vjestina: '',
            uposlenik: '',
            temp: '',
            temp2: '',
            id: '',
            startDate: new Date()
        };
    }

    handleChange = (e, index) => {
        this.state.id = this.state.Certifikati[index].id;;
    }

    componentWillMount() {
        axios.get(getBaseUrl() + '/certificate/all')
            .then(res => {
                const Certifikati = res.data;
                this.setState({ Certifikati });
            })

        axios.get(getBaseUrl() + '/employees')
            .then(res => {
                var temp = [];
                const uposlenikCertifikata = res.data;
                for (var i = 0; i < res.data.length; i++) {
                    temp.push({ name: `${res.data[i].firstName}`, value: res.data[i].firstName + " " + res.data[i].lastName, id: res.data[i].id });

                }
                this.setState({ uposlenikCertifikata });
                this.setState({ uposlenici: temp });
            })
        axios.get(getBaseUrl() + '/skills')
            .then(res => {
                var temp = [];
                const vjestinaCertifikata = res.data;
                for (var i = 0; i < res.data.length; i++) {
                    temp.push({ name: `${res.data[i].name}`, value: res.data[i].name, id: res.data[i].id });

                }
                this.setState({ vjestinaCertifikata });
                this.setState({ options: temp });
            })
    }

    handleChangeDate = date => {
        this.setState({
            startDate: date,
            izdavanje: date
        });
    }

    handleChangeDateIstek = date => {
        this.setState({
            startDate: date,
            istek: date
        });
    }

    obrisiCertifikat = () => {
        axios.delete(getBaseUrl() + `/certificate/${this.state.id}`)
            .then(res => {
                var TEMP = [...this.state.Certifikati];
                for (var i = 0; i < TEMP.length; i++) {
                    if (TEMP[i].id === this.state.id) TEMP.splice(i, 1);
                }
                this.setState({ Certifikati: TEMP })
                alert("Uspješno obrisan certifikat!");
            })
    }

    kreirajCertifikat = () => {
        var idUposlenika = ''
        var idVjestine = ''
        for (var i = 0; i < this.state.options.length; i++) {
            if (this.state.options[i].value === this.state.vjestina) idVjestine = this.state.options[i].id;
        }
        for (var i = 0; i < this.state.uposlenici.length; i++) {
            if (this.state.uposlenici[i].value === this.state.uposlenik) idUposlenika = this.state.uposlenici[i].id;
        }

        var reg = /^[A-Za-z0-9\s\-]*$/g
        if (!this.state.ime || this.state.ime.trim().length == 0 || !reg.test(this.state.ime)) {
            alert("Unesite ispravno ime")
            return
        }
        if (!this.state.izdavanje || this.state.izdavanje == "") {
            alert("Odaberite datum izdavanja")
            return
        }
        if (!this.state.istek || this.state.istek == "") {
            alert("Odaberite datum isteka")
            return
        }
        if (this.state.izdavanje >= this.state.istek) {
            alert("Datum isteka mora biti nakon datuma izdavanja")
            return
        }

        if (this.state.uposlenik == "") {
            alert("Odaberite uposlenika")
            return
        }

        if (this.state.vjestina == "") {
            alert("Odaberite vjestinu")
            return
        }

        axios.post(getBaseUrl() + '/certificate', {
            dateOfIssue: this.state.izdavanje,
            empolyeeOnCrtificate: this.state.uposlenikCertifikata[idUposlenika - 1],
            expireDate: this.state.istek,
            name: this.state.ime,
            skillOnCetrificate: this.state.vjestinaCertifikata[idVjestine - 1]

        })
            .then(response => {
                alert("Certifikat uspješno registrovan!")
                window.location.href = "/knowledge/certifikati"
            })
            .catch(err => {
                console.log(err.response.data.message)
            })


    }

    handleChangeUposlenik = (selectedOption) => {
        if (selectedOption) {
            this.setState({ uposlenik: selectedOption.value })
            this.setState({ temp: selectedOption });
        }
    }

    handleChangeVjestina = (selectedOption) => {
        if (selectedOption) {
            this.setState({ vjestina: selectedOption.value })
            this.setState({ temp2: selectedOption });
        }
    }

    prikazCertifikata() {
        return this.state.Certifikati.map((korisnik, index) => {
            const { name, empolyeeOnCrtificate, skillOnCetrificate, dateOfIssue, expireDate, obrisati } = korisnik
            return (
                <tr key={name}>
                    <td>{name}</td>
                    <td>{skillOnCetrificate.name}</td>
                    <td>{dateOfIssue}</td>
                    <td>{expireDate}</td>
                    <td>{empolyeeOnCrtificate.firstName + " " + empolyeeOnCrtificate.lastName}</td>
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
                <h2 id='title'>Postojeći certifikati</h2>
                <div className="glavniDIV">
                    <table id='certifikati'>
                        <tbody>
                            <tr>{this.headerTabele()}</tr>
                            {this.prikazCertifikata()}
                        </tbody>
                    </table>
                    <div className="footer">
                        <button type="button" className="btnObrisiCertifikat" onClick={this.obrisiCertifikat}>
                            Obriši certifikat
                </button>
                    </div>
                </div>
                <div className="formaCert">
                    <h2>Unos certifikata</h2>
                    <div className="form-grupaCert">
                        <label htmlFor="ime">Ime:</label>
                        <input type="text"
                            name="ime"
                            value={this.state.ime}
                            onChange={e => this.unosNovog(e)} />
                    </div>
                    <div className="form-grupaCert">
                        <label htmlFor="izdavanje">Datum izdavanja:</label>
                        <DatePicker
                            name="izdavanje"
                            selected={this.state.izdavanje}
                            onChange={this.handleChangeDate}
                            showTimeSelect
                            dateFormat="Pp"
                        />
                    </div>
                    <div className="form-grupaCert">
                        <label htmlFor="istek">Datum isteka:</label>
                        <DatePicker
                            name="istek"
                            selected={this.state.istek}
                            onChange={this.handleChangeDateIstek}
                            showTimeSelect
                            dateFormat="Pp"
                        />
                    </div>
                    <div className="form-grupaCert">
                        <label htmlFor="uposlenik">Uposlenik:</label>
                        <Dropdown options={this.state.uposlenici}
                            value={this.state.temp}
                            onChange={(e) => {
                                this.handleChangeUposlenik(e);
                            }}
                            placeholder="Odaberite uposlenika za kojeg je certifikat"
                        />
                    </div>
                    <div className="form-grupaCert">
                        <label htmlFor="vjestina">Vještina:</label>
                        <Dropdown options={this.state.options}
                            value={this.state.temp2}
                            onChange={(e) => {
                                this.handleChangeVjestina(e);
                            }}
                            placeholder="Odaberite vještinu za koju je certifikat"
                        />
                    </div>
                    <button type="button" className="btnDodajCertifikat" onClick={this.kreirajCertifikat}>
                        Dodavanje novog certifikata
                </button>
                </div>
            </div>
        )
    }
}

