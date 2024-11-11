import {useEffect, useState} from 'react'
import './App.css'
import axios from "axios";
import {Ups} from "./types/ups.ts";
import {Route, Routes} from "react-router-dom";
import AllUpsesPage from "./pages/AllUpsesPage.tsx";
import UpsPage from "./pages/UpsPage.tsx";
import {Server} from "./types/server.ts";
import {Credentials} from "./types/credentials.ts";
import ProtectedRoute from "./components/ProtectedRoute.tsx";
import LoginPage from "./pages/LoginPage.tsx";
import AllCredentialsPage from "./pages/AllCredentialsPage.tsx";
import CredentialsPage from "./pages/CredentialsPage.tsx";
import ServerPage from './pages/ServerPage.tsx';
import AllServersPage from './pages/AllServersPage.tsx';
import Navbar from './components/Navbar.tsx';
import {Status} from "./types/status.ts";

function App() {

    const [monitoring, setMonitoring] = useState<boolean>(false)
    const [username, setUsername] = useState<string>("")
    const [statusMap, setStatusMap] = useState<Map<string, Status>>(new Map<string, Status>)

    useEffect(() => {
        axios.get("/api/login")
            .then((r) => setUsername(r.data))
            .catch(error => console.error(error))
    }, [])

    /**  UPS ********************************/

    const [upses, setUpses] = useState<Ups[]>([])
    const [upsUpdates, setUpsUpdates] = useState<number>(0)         // keeps track of crud operations in other components
    const upsUpdateOccured = () => setUpsUpdates(upsUpdates + 1)    // passed to components that do crud operations

    const getAllUpses = () => {
        axios.get('/api/ups')
            .then(response => {
                setUpses(response.data);
            })
            .catch(error => {
                console.error('getAllUpses failed:', error);
            });
    };

    useEffect(() => {
        getAllUpses();
    }, [upsUpdates])

    /** Status in monitoring mode *******/

    const getAllStatuses = () => {
        axios.get('/api/monitor')
            .then(response => {
                console.log("StatusMap / getAllStatuses: ", response.data.statusMap)
                setMonitoring(response.data.monitoring);
                const mapData = new Map<string,Status>(Object.entries(response.data.statusMap));
                setStatusMap(mapData);
            })
            .catch(error => console.error('getAllStatusFailed:', error))
    }

    const getUpsOrServerStatus = (id: string|undefined): Status|undefined => {
        console.log ("getUpsorServerStatus ID:" + id + " Map:" + statusMap)
        if (id === undefined) return undefined
        return statusMap?.get(id)
    }

    // ask for monitoring mode once
    useEffect(() => {
        getAllStatuses()
    }, []);

    // in monitoring mode get statuses every 5 seconds
    useEffect(() => {
        if (monitoring) {
            const timer = setInterval(() => {
                getAllStatuses();
            }, 5000)
            return () => clearInterval(timer)
        }
    }, [monitoring]);

    /**  Credentials ***********************/

    const [credentialsList, setCredentialsList] = useState<Credentials[]>([])               // holds only the global credentials
    const [credentialsUpdates, setCredentialsUpdates] = useState<number>(0)                 // keeps track of crud operations in other components
    const [appUserExists, setAppUserExists] = useState<boolean>(false)                      // app users are marked by this prefix
    const adminUser = "APP/admin"

    const credentialsUpdateOccured = () => setCredentialsUpdates(credentialsUpdates + 1)    // passed to components that do crud operations

    const getCredentialsList = () => {
        axios.get('/api/credentials')
            .then(response => {
                setCredentialsList(response.data
                    .filter( (credentials:Credentials) => credentials.global));
                setAppUserExists(response.data.some((c:Credentials) => c.user.startsWith("APP/") ))
            })
            .catch(error => {
                console.error('getCredentialsList failed:', error);
            });
    };

    useEffect(() => {
        getCredentialsList();
    }, [credentialsUpdates])

    /** Servers **********************/

    const [servers, setServers] = useState<Server[]>([])
    const [serverUpdates, setServerUpdates] = useState<number>(0)         // keeps track of crud operations in other components
    const serverUpdateOccured = () => setServerUpdates(serverUpdates + 1)    // passed to components that do crud operations

    const getAllServers = () => {
        axios.get('/api/server')
            .then(response => {
                setServers(response.data);
            })
            .catch(error => {
                console.error('getAllServers failed:', error);
            });
    };

    useEffect(() => {
        getAllServers();
    }, [serverUpdates])


    return (

        <>
            <Navbar monitoring={monitoring} setMonitoring={setMonitoring} username={username} setUsername={setUsername}/>
            <Routes>
               {!username || username === "anonymousUser"
                    &&
                         <Route path={"/login"} element={<LoginPage appUserExists={appUserExists}
                                                           credentialsUpdateOccured={credentialsUpdateOccured}
                                                           setUsername={setUsername} adminUser={adminUser}/>}/>
                }
                <Route element={<ProtectedRoute username={username}/>}>
                    <Route path={"/"} element={
                        <AllUpsesPage upses={upses} servers={servers} monitoring={monitoring} getUpsStatus={getUpsOrServerStatus}/>}/>
                    <Route path={"/server"} element={
                        <AllServersPage servers={servers} upses={upses} credentialsList={credentialsList} monitoring={monitoring} getServerStatus={getUpsOrServerStatus}/>}/>
                    <Route path={"/credentials"} element={
                        <AllCredentialsPage credentialsList={credentialsList} monitoring={monitoring}/>}/>
                    <Route path={"/ups/:id"} element={
                        <UpsPage upsUpdate={upsUpdateOccured} servers={servers} monitoring={monitoring} getUpsStatus={getUpsOrServerStatus}/>}/>
                    <Route path={"/server/:id"} element={
                        <ServerPage upses={upses} credentialsList={credentialsList} serverUpdate={serverUpdateOccured} monitoring={monitoring} getServerStatus={getUpsOrServerStatus} />}/>
                    <Route path={"/credentials/:id"} element={
                        <CredentialsPage credentialsUpdate={credentialsUpdateOccured} monitoring={monitoring}/>} />
                </Route>
            </Routes>
        </>
    )
}

export default App
