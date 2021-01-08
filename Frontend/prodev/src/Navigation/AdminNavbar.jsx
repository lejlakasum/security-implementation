import React, { Component } from 'react'
import 'react-dropdown/style.css';
import "react-datepicker/dist/react-datepicker.css";
import { Link } from "react-router-dom"



class AdminNavbar extends React.Component {
    constructor(props) {
        super(props);
    }
    render() {
        return (
            <nav className="navbar">
                <ul>
                    <Link to="/admin/korisnici">
                        <li>Korisnici</li>
                    </Link>
                    <Link to="/admin/uposlenici">
                        <li>Uposlenici</li>
                    </Link>
                    <Link to="/admin/change-password">
                        <li>Promijeni password</li>
                    </Link>
                </ul>
            </nav>
        )
    }
}

export default AdminNavbar