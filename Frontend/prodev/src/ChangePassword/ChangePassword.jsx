import React, { useState } from "react";
import getBaseUrl from "../Util/getBaseUrl";
import getUserFromToken from "../Util/getUserFromToken";
import axios from 'axios'

const ChangePassword = ({ ...props }) => {

    const [oldPass, setOldPass] = useState("")
    const [newPass, setNewPass] = useState("")
    const [newPassRetype, setNewPassRetype] = useState("")

    function changeClick() {

        if (newPass != newPassRetype) {
            alert("Novi passwordi se ne poklapaju")
            return
        }

        var regPass = /^.*(?=.{8,})(?=.*[a-zA-Z])(?=.*\d)(?=.*[!{}<>@^*()_=;:'\-#$%&? "]).*$/g
        if (!regPass.test(newPass)) {
            alert("Password mora sadrzavati minimalno 8 karaktera, veliko i malo slovo, broj i znak")
            return
        }

        var route = getBaseUrl() + '/user/change-password'
        var request = {
            oldPassword: oldPass,
            newPassword: newPass,
            email: getUserFromToken().email
        }

        axios.post(route, request)
            .then(response => {
                if (response.status == 200) {
                    alert("Uspjesno promijenjen password. Prijavite se ponovo!")
                    document.cookie = "token=; path=/; max-age=-9999999;"
                    window.location.href = "/"
                }
                else if (response.status == 404) {
                    alert("Niste logovani")
                }
                else if (response.status == 400) {
                    alert("Pogresan stari password")
                }
            })
            .catch(error => {
                alert("Neispravan stari password")
            })
    }

    return (
        <div className="change-password">
            <input type="password" placeholder="Old password" onChange={(e) => setOldPass(e.target.value)} />
            <input type="password" placeholder="New password" onChange={(e) => setNewPass(e.target.value)} />
            <input type="password" placeholder="Retype new password" onChange={(e) => setNewPassRetype(e.target.value)} />
            <button onClick={() => changeClick()}>Change</button>
        </div>
    );
}

export default ChangePassword;