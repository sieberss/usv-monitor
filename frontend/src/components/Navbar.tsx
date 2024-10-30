import {Link} from "react-router-dom";
import './NavBar.css'

export default function NavBar() {
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
        </ul>
    )

}