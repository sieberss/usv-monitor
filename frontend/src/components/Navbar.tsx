import {Link} from "react-router-dom";
import './NavBar.css'
import axios from "axios";
import { MdOutlinePublishedWithChanges } from "react-icons/md";

type Props = {
    monitoring: boolean,
    setMonitoring: (on: boolean) => void,
    username: string,
    setUsername: (username:string) => void
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
            <li className={"monitoring"}> {props.monitoring ? "Monitoring ON" : "Monitoring OFF"}
            </li>
            <li> <MdOutlinePublishedWithChanges onClick={() => switchMonitoringMode(!props.monitoring)}/> </li>
            <li className={"ups"}>
                <Link to={"/"}
                      className={"link"}> UPSes </Link></li>
            <li className={"server"}>
                <Link to={"/server"} className={"link"}> Servers </Link>
            </li>
            <li className={"credentials"}>
                <Link to={"/credentials"} className={"link"}> Credentials </Link>
            </li>
            <li className={"logout"}>
                <Link to={"/login"} className={"link"} onClick={logout}>
                    {loggedIn ? "Logout" : "Login"}
                </Link></li>

        </ul>
    )

}