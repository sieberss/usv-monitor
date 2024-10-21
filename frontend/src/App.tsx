import {useEffect, useState} from 'react'
import './App.css'
import axios from "axios";
import {Usv} from "./types/usv.ts";
import {Route, Routes} from "react-router-dom";
import AllUsvsPage from "./pages/AllUsvsPage.tsx";
import UsvPage from "./pages/UsvPage.tsx";

function App() {

    const [monitoring, setMonitoring] = useState<boolean>(false)

    const [usvs, setUsvs] = useState<Usv[]>([])
    const [usvUpdates, setUsvUpdates] = useState<number>(0)         // keeps track of crud operations in other components
    const usvUpdateOccured = () => setUsvUpdates(usvUpdates + 1)    // passed to components that do crud operations

    const getAllUsvs = () => {
        axios.get('/api/usv')
            .then(response => {
                setUsvs(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    };

    useEffect(() => {
        getAllUsvs();
    }, [usvUpdates])


    return (

        <>
            <Routes>
                <Route path={"/"} element={<AllUsvsPage usvs={usvs} monitoring={monitoring}/>}/>
                <Route path={"/usvdetails/:id"} element={<UsvPage usvUpdate={usvUpdateOccured}/>} />
            </Routes>
        </>
    )
}

export default App
