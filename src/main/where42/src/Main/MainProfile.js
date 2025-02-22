import React from "react";
import {Link} from "react-router-dom";

const MainProfile = (props) => {
    const info = props.info;
    const locate = CombineLocate(info.locate, info.inOrOut);
    let meOrNot = null;
    let msg = "";
    if (info.msg) {
        msg = JSON.parse(info.msg).msg;
    }

    if (props.me) {
        meOrNot = ( 
        <div id="SettingWrapper">
            <Link to="/Setting">
                <button id="Setting"> </button>
            </Link>
        </div>
        )
    }
    
    return (
        <div className="Profile">
            <div className="Photo">
                 <img src={info.img} alt="user-face"></img>
            </div>
            <div className="Info">
                <div className="Name">{info.name}</div>
                <div className="Locate">{locate}</div>
                <div className="Msg">{msg}</div>
            </div>
            {meOrNot}
        </div>
    );
};

export function CombineLocate(locate, inOutState) {
    let position = "";
    if (inOutState === 2)
        position = "자리 정보 없음";
    else if (inOutState === 0)
        position = "퇴근";
    else
    {
        //inoutstate가 1이고 planet이 0인 경우 없음? : 확인 필요
        if (locate.planet === 1)
            position = "개포 ";
        else if (locate.planet === 2)
            position = "서초 ";

        if ((locate.planet === 1 && locate.floor === 0) || (locate.planet === 2 && locate.cluster === 0))
            position += "클러스터 내 ";
        else if (locate.floor === 6) {
            if (locate.spot === "오픈스튜디오")
                position += "지하 1층 ";
            else
                position += "옥상 "
        }
        else if (locate.floor > 0 && locate.floor < 6)
            position += locate.floor.toString() + "층 ";
        else if (locate.cluster >= 7 && locate.cluster <= 10)
            position += locate.cluster.toString() + "클 ";

        if (locate.spot !== null)
            position += locate.spot;
    }
    return position;
}

export default MainProfile;