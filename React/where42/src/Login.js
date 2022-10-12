import { useState } from 'react';
import { useMediaQuery } from 'react-responsive';
import './Login.css'
import './Login_Mobile.css'
import './Login_Tablet.css'
import './Login_Desktop.css'

function Login() {
    const isMobile = useMediaQuery({ query: '(max-width: 480px)'})
    const isTablet = useMediaQuery({ query: '(min-width: 481px) and (max-width: 1023px)'})
    const isDesktop = useMediaQuery({ query: '(min-width: 1024px)'})
    const [modal, setModal] = useState(0);

    function Common() {
        function clickDown() {
            const button = document.getElementById('Login-button');
            button.style = "background-image: url('img/login_button_click.svg'); background-size: contain";
        }
        function clickUp() {
            const button = document.getElementById('Login-button');
            button.style = "background-image: url('img/login_button.svg')";
            window.location.href = 'https://profile.intra.42.fr/users/sojoo';
        }
       
        return (
            <div id="Common">
                <div id="Main">
                    <button id="Wiki" onClick={()=>{setModal(1)}} ></button>
                    <div id="Logo">
                        <img src="img/logo.svg" alt="logo"></img>
                    </div>
                   <div id="Character">
                        <img src="img/character.svg" alt="character"></img>
                    </div>
                    <button id="Login-button" onMouseDown={clickDown} onMouseUp={clickUp}></button>
                </div>
            </div>
        )
    }
    
    function Modal() {
        return (
            <div id="Modal">
                <div id="ModalHeader"></div>
                <button id="ModalCancel" onClick={()=>{setModal(0)}}></button>
                <div id="ModalContent">내용을 쓰면 될것같아요.</div>
            </div>
        )
    }

    return (
        <div className="Login">
            {modal === 1 ? <Modal/> : null}
            {isMobile && <div id="Mobile"><Common/></div>}
            {isTablet && <div id="Tablet"><Common/></div>}
            {isDesktop && <div id="Desktop"><Common/></div>}
        </div>
    );
}

export default Login;