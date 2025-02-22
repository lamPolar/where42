import {useNavigate} from "react-router";
import Loading from "./Loading";
import axios from 'axios';

function Oauth() {
    const nav = useNavigate();
    let code = new URL(window.location.href).searchParams.get("code");
    axios.post('/v1/auth/token', null, {params : {code: code}})
        .then((response) => {
            nav('/Main');
        }).catch((Error)=> {
            console.clear();
            if (Error.response.status === 401) {
                let data = Error.response.data.data;
                nav('/Agree', {state: data});
            }
            else {
                alert("다시 한 번 시도해 주세요.")
                nav('/Login');
            }
        });

    return (
        <Loading/>
    )
}

export default Oauth;