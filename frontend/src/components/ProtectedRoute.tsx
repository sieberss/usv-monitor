import axios from "axios";
import { useEffect } from "react";
import {Navigate, Outlet, useNavigate} from "react-router-dom";

type Props = {
    username: string
}

export default function ProtectedRoute(props: Readonly<Props>) {
    const loggedIn = props.username !== "anonymousUser"
    const navigate = useNavigate()
   /* const [username, setUsername] = useState<string>("anonymousUser")
    useEffect(() => {
        axios.get("api/login")
            .then(r => setUsername(r.data))
            .catch(error => console.log("error fetching Login data:", error))
    }, [])*/
    console.log(props.username)
console.log(loggedIn)

    return (
        <>user: {props.username}
        loggedIn ? <Outlet/> : {navigate("/login")}</>
    )
}