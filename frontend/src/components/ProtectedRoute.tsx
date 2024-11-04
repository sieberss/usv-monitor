import {Navigate, Outlet} from "react-router-dom";

type Props = {
    username: string
}

export default function ProtectedRoute(props: Readonly<Props>) {
    const loggedIn = props.username !== "anonymousUser"
    return (
        <>
            {loggedIn ? <Outlet/> : <Navigate to={"/login"}/>}
        </>
    )
}