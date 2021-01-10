import React, { Component } from 'react'
import axios from 'axios'
import DatePicker from "react-datepicker";
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import "react-datepicker/dist/react-datepicker.css";
import Modal from 'react-modal'
import getBaseUrl from '../Util/getBaseUrl'

export class Uposlenici extends Component {
    constructor(props) {
        super(props)
        this.state = {
            HeaderTabele: [{ Ime: '', Prezime: '', Datum_Rođenja: '', Datum_Zaposlenja: '', Detalji: '', brisati: false }],
            headerVjestine: [
                { tip: "", vjestina: "", level: "", datum: "" },
            ],
            headerEdukacije: [
                { Tema: "", Host: "", Vrijeme: "", Tip_Edukacije: "", Vještina: "" }
            ],
            headerCertifikati: [
                { Naziv: "", Vještina: "", Datum_Izdavanja: "", Datum_Isteka: "" }
            ],
            vjestine: [], // Za popunjavnje tipova vjestina
            uposlenici: [], // Za prikaz uposlenika prema vještinama
            sviUposlenici: [], // Za prikaz svih uposlenika
            temp2: '',
            tipVjestine: '', // Za odabrani tip vjestine
            idVjestine: '', // ID za odabrani tip vjestine
            ime: '',
            prezime: '',
            zaposlenje: new Date(),
            rodjenje: new Date(),
            edukacija: '',
            podaciOEdukaciji: [],
            startDate: new Date(),
            startDate2: new Date(),
            edukacije: [],
            temp: '',
            id: '',
            modalIsOpen: false,
            upVjestine: [],
            upEdukacije: [],
            upCertifikati: []
        };
    }

    componentWillMount() {
        axios.get(getBaseUrl() + '/skills')
            .then(res => {
                var temp = [];
                for (var i = 0; i < res.data.length; i++) {
                    temp.push({ name: `${res.data[i].name}`, value: res.data[i].name, id: res.data[i].id });
                }
                this.setState({ vjestine: temp });
            })

        axios.get(getBaseUrl() + '/employees')
            .then(res => {
                const sviUposlenici = res.data;
                this.setState({ sviUposlenici });
            })
    }

    handleChange = (e, index) => {
        this.state.id = this.state.sviUposlenici[index].id;;
    }

    handleChangeEdukacije = (selectedOption) => {
        if (selectedOption) {
            this.setState({ edukacija: selectedOption.value })
            this.setState({ temp: selectedOption });
        }
    }
    handleChangeDateZaposlenja = date => {
        this.setState({
            startDate: date,
            zaposlenje: date
        });
    }

    handleChangeDateRodjenja = date => {
        this.setState({
            startDate2: date,
            rodjenje: date
        });
    }

    kreirajUposlenika = () => {

        var reg = /^[A-Za-z0-9\s\-]*$/g
        if (!this.state.ime || this.state.ime.trim().length == 0 || !reg.test(this.state.ime)) {
            alert("Unesite ispravno ime")
            return
        }

        var reg2 = /^[A-Za-z0-9\s\-]*$/g
        if (!this.state.prezime || this.state.prezime.trim().length == 0 || !reg2.test(this.state.prezime)) {
            alert("Unesite ispravno prezime")
            return
        }
        if (this.state.rodjenje >= new Date()) {
            alert("Datum rodjenja mora biti u proslosti")
            return
        }
        if (this.state.zaposlenje >= new Date()) {
            alert("Datum zaposlenja mora biti u proslosti")
            return
        }

        axios.post(getBaseUrl() + '/employees', {
            firstName: this.state.ime,
            lastName: this.state.prezime,
            dateOfEmployment: this.state.zaposlenje,
            birthDate: this.state.rodjenje
        }).then(response => {
            if (response.status === 201 || response.status === 200) {
                alert("Uposlenik uspješno registrovan!")
                window.location.href = "/knowledge/uposlenici"
            }

        }).catch(err => {

        })

    }

    unosNovog = (e) => {
        this.setState({
            [e.target.name]: e.target.value
        })
    }

    // Za button sve
    prikaziSveUposlenike = () => {
        axios.get(getBaseUrl() + '/employees')
            .then(res => {
                const sviUposlenici = res.data;
                this.setState({ sviUposlenici });
            })

    }


    // Za pretraživanje prema vještinama
    prikaziUposlenike = () => {
        document.getElementById("lista").innerHTML = ""
        for (var i = 0; i < this.state.vjestine.length; i++) {
            if (this.state.vjestine[i].value === this.state.tipVjestine) {
                this.state.idVjestine = this.state.vjestine[i].id;
            }
        }

        axios.get(getBaseUrl() + `/skills/${this.state.idVjestine}/employees`)
            .then(res => {
                var temp = [];
                const sviUposlenici = res.data;
                this.setState({ sviUposlenici });
                for (var i = 0; i < res.data.length; i++) {
                    temp.push({ ime: `${res.data[i].firstName}`, prezime: res.data[i].lastName, value: res.data[i].name, id: res.data[i].id });
                }

                this.state.uposlenici = temp.map((number) => {
                    const { ime, prezime } = number;
                    return (<li key={ime.toString() + " " + prezime.toString()}>{ime}</li>);
                })
            })

        /* return this.state.uposlenici.map((uposlenik) => {
             const {key} = uposlenik  
             return(document.getElementById("lista").innerHTML += '<ul>' + key + '</ul>')
          })*/
    }

    // Za prikaz liste uposlenika
    prikazUposlenika() {
        return this.state.sviUposlenici.map((tipov, index) => {
            const { id, firstName, lastName, birthDate, dateOfEmployment, educations } = tipov
            var temaEdukacije = ''
            const brisati = false

            return (
                <tr key={firstName + " " + lastName}>
                    <td>{firstName}</td>
                    <td>{lastName}</td>
                    <td>{birthDate.split('T')[0]}</td>
                    <td>{dateOfEmployment.split('T')[0]}</td>
                    <td><button onClick={e => this.showDetails(id)} > Detalji</button></td>
                    <td>{brisati}
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

    handleChangeVjestine = (selectedOption) => {
        if (selectedOption) {
            this.setState({ tipVjestine: selectedOption.value });
            this.setState({ temp2: selectedOption });
        }
    }

    headerTabele() {
        let header = Object.keys(this.state.HeaderTabele[0])
        return header.map((key, index) => {
            return <th key={index}>{key.toUpperCase()}</th>
        })
    }

    obrisiUposlenika = () => {
        axios.delete(getBaseUrl() + `/employees/${this.state.id}`)
            .then(res => {
                var TEMP = [...this.state.sviUposlenici];
                for (var i = 0; i < TEMP.length; i++) {
                    if (TEMP[i].obrisati) TEMP.splice(i, 1);
                }
                alert("Uspješno obrisan uposlenik!");
                this.setState({ sviUposlenici: TEMP })
                window.location.href = "/knowledge/uposlenici"
            }).catch(err => {
                console.log(err.response.data.errors)
            })
    }

    //DETALJI

    showDetails(id) {
        this.setState({ modalIsOpen: true })

        var url = getBaseUrl() + '/employees/' + id + '/skills'
        axios.get(url)
            .then(res => {
                const temp = res.data;
                this.setState({ upVjestine: temp });
            })

        url = getBaseUrl() + '/certificate/all'
        axios.get(url)
            .then(res => {
                const temp = []
                for (var i = 0; i < res.data.length; i++) {
                    if (res.data[i].empolyeeOnCrtificate.id === id) {
                        temp.push(res.data[i])
                    }
                }

                this.setState({ upCertifikati: temp });
            })
    }

    headerVjestine() {
        let header = Object.keys(this.state.headerVjestine[0])
        return header.map((key, index) => {
            return <th key={index}>{key.toUpperCase()}</th>
        })
    }

    headerEdukacije() {
        let header = Object.keys(this.state.headerEdukacije[0])
        return header.map((key, index) => {
            return <th key={index}>{key.toUpperCase()}</th>
        })
    }

    headerCertifikati() {
        let header = Object.keys(this.state.headerCertifikati[0])
        return header.map((key, index) => {
            return <th key={index}>{key.toUpperCase()}</th>
        })
    }

    prikazVjestinaDetalji() {
        return this.state.upVjestine.map((vjestina, index) => {
            const { id, name, skill, skillLevel, dateCreated } = vjestina
            const obrisati = false
            return (
                <tr key={id}>
                    <td>{skill.skillType.name}</td>
                    <td>{skill.name}</td>
                    <td>{skillLevel}</td>
                    <td>{dateCreated.split('T')[0]}</td>
                </tr>
            )
        })
    }

    prikazEdukacijaDetalji() {
        return this.state.upEdukacije.map((edukacija, index) => {
            const { id, topic, host, dateTime, educationType, skill } = edukacija
            return (
                <tr key={id}>
                    <td>{topic}</td>
                    <td>{host}</td>
                    <td>{dateTime.split('T')[0]}</td>
                    <td>{educationType.name}</td>
                    <td>{skill.name}</td>
                </tr>
            )
        })
    }

    prikazCertifikataDetalji() {
        return this.state.upCertifikati.map((korisnik, index) => {
            const { id, name, skillOnCetrificate, dateOfIssue, expireDate } = korisnik
            return (
                <tr key={id}>
                    <td>{name}</td>
                    <td>{skillOnCetrificate.name}</td>
                    <td>{dateOfIssue.split('T')[0]}</td>
                    <td>{expireDate.split('T')[0]}</td>
                </tr>
            )
        })
    }

    render() {
        return (
            <div>
                <div className="formaUposleniciVjestine">
                    <Dropdown className="dropdownPretrazi" options={this.state.vjestine}
                        value={this.state.temp2}
                        onChange={(e) => {
                            this.handleChangeVjestine(e);
                        }}
                        placeholder="Odaberite tip vještine za pretraživanje"
                    />
                    <button type="button" className="btnPretrazi" onClick={this.prikaziUposlenike}>
                        Pretraži uposlenike prema vještini
                    </button>
                    <button type="button" className="btnPretrazi" onClick={this.prikaziSveUposlenike}>
                        Prikaz svih uposlenika
                    </button>
                    <div id="lista"></div>
                </div>
                <h2 id='title'>Uposlenici</h2>
                <div className="glavniDIV">
                    <table id='tipovi'>
                        <tbody>
                            <tr>{this.headerTabele()}</tr>
                            {this.prikazUposlenika()}
                        </tbody>
                    </table>
                    <div className="footerUposlenici">
                        <button type="button" className="btnObrisiUposlenika" onClick={this.obrisiUposlenika}>
                            Obriši uposlenika
                    </button>
                    </div>
                </div>
                <div className="formaUposlenici">
                    <h2>Unos uposlenika</h2>
                    <div className="form-grupaUposlenici">
                        <label htmlFor="ime">Ime uposlenika:</label>
                        <input type="text"
                            name="ime"
                            value={this.state.ime}
                            onChange={e => this.unosNovog(e)} />
                    </div>
                    <div className="form-grupaUposlenici">
                        <label htmlFor="prezime">Prezime uposlenika:</label>
                        <input type="text"
                            name="prezime"
                            value={this.state.prezime}
                            onChange={e => this.unosNovog(e)} />
                    </div>
                    <div className="form-grupaUposlenici">
                        <label htmlFor="rodjenje">Datum rođenja:</label>
                        <DatePicker
                            name="rodjenje"
                            selected={this.state.startDate2}
                            onChange={this.handleChangeDateRodjenja}
                            showTimeSelect
                            timeFormat="HH:mm"
                            timeCaption="Vrijeme"
                            timeIntervals={60}
                            dateFormat="dd/MM/yyyy, h:mm"
                        />
                    </div>
                    <div className="form-grupaUposlenici">
                        <label htmlFor="zaposlenje">Datum zaposlenja:</label>
                        <DatePicker
                            name="zaposlenje"
                            selected={this.state.startDate}
                            onChange={this.handleChangeDateZaposlenja}
                            showTimeSelect
                            timeFormat="HH:mm"
                            timeCaption="Vrijeme"
                            timeIntervals={60}
                            dateFormat="dd/MM/yyyy, h:mm"
                        />
                    </div>
                    <button type="button" className="btnDodajUposlenika" onClick={this.kreirajUposlenika}>
                        Dodavanje novog uposlenika
                </button>
                </div>
                <Modal isOpen={this.state.modalIsOpen}>
                    <div>
                        <button onClick={() => this.setState({ modalIsOpen: false })}>Zatvori</button>
                    </div>

                    <table id='tipovi'>
                        <h2>Vještine</h2>
                        <tbody>
                            <tr>{this.headerVjestine()}</tr>
                            {this.prikazVjestinaDetalji()}
                        </tbody>
                    </table>

                    <table id='tipovi'>
                        <h2>Certifikati</h2>
                        <tbody>
                            <tr>{this.headerCertifikati()}</tr>
                            {this.prikazCertifikataDetalji()}
                        </tbody>
                    </table>
                </Modal>
            </div >
        )
    }
}