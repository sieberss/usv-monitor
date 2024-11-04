import {Link} from "react-router-dom";
import './NavBar.css'
import axios from "axios";

type Props = {
    username: string,
    setUsername: (username:string) => void
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
        <ul>
            <li>
                <Link to={"/"}
                      className={"link"}> UPS </Link></li>
            <li>
                <Link to={"/server"} className={"link"}> Server </Link>
            </li>
            <li>
                <Link to={"/credentials"} className={"link"}> Credentials </Link>
            </li>
            {props.username !== "anonymousUser"
                && <button onClick={logout}> Logout </button>
            }
        </ul>
    )

}