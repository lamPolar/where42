import React, {useEffect, useState} from 'react';
import instance from "../AxiosApi";
import { Link } from "react-router-dom";
import { useMediaQuery } from 'react-responsive';
import './Main_Desktop.css';
import './Main_Mobile.css';
import MainProfile from './MainProfile';
import Groups from './Groups';
import Loading from "../Etc/Loading";

function Main() {
    const isMobile = useMediaQuery({ query: '(max-width: 930px)'});
    const isDesktop = useMediaQuery({ query: '(min-width: 931px)'});

    const [memberInfo, setMemberInfo] = useState(null);
    const [groupInfo, setGroupInfo] = useState(null);
    const [friendInfo, setFriendInfo] = useState(null);
    useEffect(() => {
        //memberinfo
        instance.get('member/member').then((response)=>{
            setMemberInfo(response.data);
        })
        //groupinfo
        instance.get('member/group').then((response)=>{
            setGroupInfo(response.data);
        })
        //groupfriendinfo
        instance.get('member/friend').then((response)=>{
            setFriendInfo(response.data);
        })
    }, []);

    function Common() {
        localStorage.setItem('name', memberInfo.name);
        return (
            <div id="Wrapper">
                <Link to="/Search" state={memberInfo.name}>
                    <button id="Search"></button>
                </Link>
                <Link to="/Main">
                    <div id="Logo">
                        <img src="img/logo_simple.svg" alt="logo"></img>
                        {isMobile && <p>42서울 친구 자리 찾기 서비스</p>}
                    </div>
                </Link>
                <div id="MyProfile">
                    <MainProfile key={memberInfo.id} info={memberInfo} me={1}/>
                </div>
                <Groups groupInfo={groupInfo} friendInfo={friendInfo}/>
            </div>
        )
    }

    const MainContent=()=>{
        return (
            <>
                {isMobile &&  <div id="Mobile"><Common/></div>}
                {isDesktop && <div id="Desktop"><Common/></div>}
            </>
        )
    }
    return (
        <div id="Main">
            {memberInfo && groupInfo && friendInfo? <MainContent/> : <Loading/>}
        </div>
    )
}

export default Main;