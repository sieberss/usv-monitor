import {Link} from "react-router-dom";
import './NavBar.css'
import axios from "axios";
import { MdOutlinePublishedWithChanges } from "react-icons/md";

type Props = {
    monitoring: boolean,
    setMonitoring: (on: boolean) => void,
    username: string,
    setUsername: (username:string) => void,
    selectedItem: string
}

export default function NavBar(props:Readonly<Props>) {
    const loggedIn: boolean = props.username !== "anonymousUser"
    function logout(): void {
        axios.get("/api/login/logout")
            .then(r => {
                console.log(r.data)
                props.setUsername("anonymousUser")
            })
            .catch(error => console.error(error))
    }
    function switchMonitoringMode(on: boolean): void {
        if (loggedIn) {
            props.setMonitoring(on)
        }
    }

    return (
        <ul className={"navbar"}>
            <li><p className={"monitoring"}> {props.monitoring ? "Monitoring ON" : "Monitoring OFF"}</p>
                &nbsp;
                <MdOutlinePublishedWithChanges onClick={() => switchMonitoringMode(!props.monitoring)}/>
            </li>
            <li className={props.selectedItem === "ups" ? "active" : "inactive"}>
                <Link to={"/"}
                      className={"link"}> UPSes </Link></li>
            <li className={props.selectedItem === "server" ? "active" : "inactive"}>
                <Link to={"/server"} className={"link"}> Servers </Link>
            </li>
            <li className={props.selectedItem === "credentials" ? "active" : "inactive"}>
                <Link to={"/credentials"} className={"link"}> global Users </Link>
            </li>
            <li className={props.selectedItem === "login" ? "active" : "inactive"}>
                <Link to={"/login"} className={"link"} onClick={logout}>
                    {loggedIn ? "Logout" : "Login"}
                </Link></li>

        </ul>
    )

}