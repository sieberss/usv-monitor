import {Navigate, Outlet} from "react-router-dom";

type Props = {
    loggedIn:boolean
}

export default function ProtectedRoute(props: Readonly<Props>) {

    return (
        props.loggedIn ? <Outlet/> : <Navigate to={"/login"}/>
    )
}