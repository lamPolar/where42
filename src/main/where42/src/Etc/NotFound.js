import {Link} from "react-router-dom";
import './NotFound.css';

const NotFound=()=>{
    return (
        <div id={"NotFound"}>
            <div id={"NotFoundContent"}>404 : NotFound</div>
            <Link to={"/Login"} id="NotFoundCharacter" title="로그인 페이지로 이동"/>
        </div>
    )
}

export default NotFound;
