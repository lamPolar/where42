import {Routes, Route} from "react-router-dom";
import Home from "./Etc/Home";
import Login from "./Login/Login";
import Agree from "./Agree/Agree";
import Main from "./Main/Main";
import Search from "./Search/Search";
import Setting from "./Setting/Setting";
import NotFound from "./Etc/NotFound";
import Oauth from "./Etc/Oauth";
import PublicRoute from "./PublicRoute";
import PrivateRoute from "./PrivateRoute";

function App() {
    if (process.env.NODE_ENV === "production") {
        console.log = function no_console() {};
        console.warn = function no_console() {};
        console.error = function no_console() {};
        console.debug = function no_console() {};
    }

    return (
        <div className={'App'}>
            <Routes>
                <Route path={"/"} element={<Home/>}/>
                <Route path={"/Login"} element={<Login/>}/>
                <Route path={"/v1/auth/callback"} element={<Oauth/>}/>
                <Route path={"/*"} element={<NotFound/>}/>
                <Route path={"/Main"} element={<PrivateRoute><Main/></PrivateRoute>}/>
                <Route path={"/Search"} element={<PrivateRoute><Search/></PrivateRoute>}/>
                <Route path={"/Setting/*"} element={<PrivateRoute><Setting/></PrivateRoute>}/>
                <Route path={"/Agree"} element={<PublicRoute><Agree/></PublicRoute>}/>

            </Routes>
        </div>
    );
}

export default App;
