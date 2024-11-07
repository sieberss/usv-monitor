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

function App() {

    const [monitoring, setMonitoring] = useState<boolean>(false)
    const [selectedMenuItem, setSelectedMenuItem] = useState<string>("login")
    const [username, setUsername] = useState<string>("")
    const [statusMap, setStatusMap] = useState<Map<string, Status>>

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

    /** UPS status in monitoring mode *******/


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
            <Navbar monitoring={monitoring} setMonitoring={setMonitoring} selectedItem={selectedMenuItem} username={username} setUsername={setUsername}/>
            <Routes>
               {!username || username === "anonymousUser"
                    &&
                         <Route path={"/login"} element={<LoginPage setMenuItem={setSelectedMenuItem} appUserExists={appUserExists}
                                                           credentialsUpdateOccured={credentialsUpdateOccured}
                                                           setUsername={setUsername} adminUser={adminUser}/>}/>
                }
                <Route element={<ProtectedRoute username={username}/>}>
                    <Route path={"/"} element={<AllUpsesPage setMenuItem={setSelectedMenuItem} upses={upses} servers={servers} monitoring={monitoring}/>}/>
                    <Route path={"/server"} element={<AllServersPage setMenuItem={setSelectedMenuItem} servers={servers} upses={upses} credentialsList={credentialsList} monitoring={monitoring} />}/>
                    <Route path={"/credentials"} element={<AllCredentialsPage setMenuItem={setSelectedMenuItem} credentialsList={credentialsList}/>}/>
                    <Route path={"/ups/:id"} element={<UpsPage setMenuItem={setSelectedMenuItem} upsUpdate={upsUpdateOccured} servers={servers} monitoring={monitoring}/>}/>
                    <Route path={"/server/:id"} element={<ServerPage setMenuItem={setSelectedMenuItem} upses={upses} credentialsList={credentialsList} serverUpdate={serverUpdateOccured} />}/>
                    <Route path={"/credentials/:id"} element={<CredentialsPage setMenuItem={setSelectedMenuItem} credentialsUpdate={credentialsUpdateOccured}/>} />
                </Route>
            </Routes>
        </>
    )
}

export default App
