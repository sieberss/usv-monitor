import {Link} from "react-router-dom";
import './NavBar.css'
import axios from "axios";

type Props = {
    username: string,
    setUsername: (username:string) => void,
    selectedItem: string
}

export default function NavBar(props:Readonly<Props>) {
    function logout(): void {
        axios.get("/api/login/logout")
            .then(r => {
                console.log(r.data)
                props.setUsername("anonymousUser")
            })
            .catch(error => console.error(error))
    }

    return (
        <ul className={"navbar"}>
            <li className={props.selectedItem === "ups" ? "active" : "inactive"}>
                <Link to={"/"}
                      className={"link"}> UPS </Link></li>
            <li className={props.selectedItem === "server" ? "active" : "inactive"}>
                <Link to={"/server"} className={"link"}> Server </Link>
            </li>
            <li className={props.selectedItem === "credentials" ? "active" : "inactive"}>
                <Link to={"/credentials"} className={"link"}> User </Link>
            </li>
            <li className={props.selectedItem === "login" ? "active" : "inactive"}>
                <Link to={"/login"} className={"link"} onClick={logout}>
                {props.username === "anonymousUser" ? "Login" : "Logout"}
            </Link></li>
        </ul>
    )

}