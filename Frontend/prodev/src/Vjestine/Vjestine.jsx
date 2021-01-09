import React, { Component } from 'react'
import axios from 'axios'
import Dropdown from 'react-dropdown';
import 'react-dropdown/style.css';
import getBaseUrl from '../Util/getBaseUrl';

export class Vjestine extends Component {
    constructor(props) {
        super(props)
        this.state = {
            Vjestine: [
                { tip: "", vjestina: "" },
            ],
            Tipovivjestina: [], // Za popunjavanje tipova vjestina
            vjestine: [],
            tip: '',
            vjestina: '',
            temp: ''
        };
    }

    componentWillMount() {

        axios.get(getBaseUrl() + '/skills')
            .then(res => {
                var temp = [];
                for (var i = 0; i < res.data.length; i++) {
                    temp.push({ name: `${res.data[i].name}`, value: res.data[i].name, id: res.data[i].id, skillTypeId: res.data[i].skillType.id, skillTypeName: res.data[i].skillType.name });
                }
                this.setState({ vjestine: temp });
            })

        axios.get(getBaseUrl() + '/skill-types')
            .then(res => {
                var temp = [];
                for (var i = 0; i < res.data.length; i++) {
                    temp.push({ name: `${res.data[i].name}`, value: res.data[i].name, id: res.data[i].id });

                }
                this.setState({ Tipovivjestina: temp });
            })
    }


    handleChange = (e, index) => {
        this.state.id = this.state.vjestine[index].id;;
    }

    obrisiVjestinu = () => {
        axios.delete(getBaseUrl() + `/skills/${this.state.id}`)
            .then(res => {
                var TEMP = [...this.state.vjestine];
                for (var i = 0; i < TEMP.length; i++) {
                    if (TEMP[i].id === this.state.id) TEMP.splice(i, 1);
                }
                this.setState({ vjestine: TEMP })
                alert("Uspješno obrisana vještina!");
            })
    }

    handleChangeTipVjestine = (selectedOption) => {
        if (selectedOption) {
            this.setState({ tip: selectedOption.value });
            this.setState({ temp: selectedOption });
        }
    }

    kreirajVjestinu = () => {
        var idVjestine = -1;

        for (var i = 0; i < this.state.Tipovivjestina.length; i++) {
            if (this.state.Tipovivjestina[i].value === this.state.tip) idVjestine = this.state.Tipovivjestina[i].id;
        }

        if (this.state.tip == "") {
            alert("Odaberite tip vjestine")
            return
        }
        var reg = /^[A-Za-z0-9\s\-]*$/g
        if (!reg.test(this.state.vjestina) || !this.state.vjestina || this.state.vjestina.trim().length == 0) {
            alert("Unesite ime vjestine")
            return
        }

        axios.post(getBaseUrl() + '/skills', {
            name: this.state.vjestina,
            skillType: {
                id: idVjestine,
                name: this.state.tip
            }
        })
            .then(response => {
                var TEMP = [...this.state.vjestine];
                const temp = {
                    id: response.data.id,
                    name: this.state.vjestina,
                    skillTypeId: idVjestine,
                    skillTypeName: this.state.tip,
                    obrisati: false
                }
                TEMP.push(temp);
                this.setState({ vjestine: TEMP })
                alert("Vještina uspješno kreirana!")
            })
    }

    prikazVjestine() {
        return this.state.vjestine.map((vjestina, index) => {
            const { name, skillTypeId, skillTypeName } = vjestina
            const obrisati = false
            return (
                <tr key={name}>
                    <td>{skillTypeName}</td>
                    <td>{name}</td>
                </tr>
            )
        })
    }

    headerTabele() {
        let header = Object.keys(this.state.Vjestine[0])
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
                <h2 id='title'>Postojeće vještine</h2>
                <div className="glavniDIV">
                    <table id='vjestine'>
                        <tbody>
                            <tr>{this.headerTabele()}</tr>
                            {this.prikazVjestine()}
                        </tbody>
                    </table>
                    <div className="footer">
                        <button type="button" className="btnObrisiVjestinu" onClick={this.obrisiVjestinu}>
                            Obriši vještinu
                </button>
                    </div>

                </div>
                <div className="formaVjestine">
                    <h2 id='title'>Dodavanje vještine</h2>
                    <div className="form-grupa">
                        <label htmlFor="username">Tip vještine:</label>
                        <Dropdown options={this.state.Tipovivjestina}
                            value={this.state.temp}
                            onChange={(e) => {
                                this.handleChangeTipVjestine(e);
                            }}
                            placeholder="Odaberite ponuđeni tip vještine"
                        />
                    </div>
                    <div className="form-grupaVjestine">
                        <label htmlFor="username">Vještina:</label>
                        <input type="text"
                            name="vjestina"
                            value={this.state.vjestina}
                            onChange={e => this.unosNovog(e)} />
                    </div>
                    <button type="button" className="btnDodajVjestinu" onClick={this.kreirajVjestinu}>
                        Dodavanje nove vještine
                </button>
                </div>
            </div>
        )
    }
}

