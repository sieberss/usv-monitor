import {useEffect, useState} from 'react'
import './App.css'
import axios from "axios";
import {Ups} from "./types/ups.ts";
import {Route, Routes} from "react-router-dom";
import AllUpsesPage from "./pages/AllUpsesPage.tsx";
import UpsPage from "./pages/UpsPage.tsx";

function App() {

    const [monitoring, setMonitoring] = useState<boolean>(false)

    const [upses, setUpss] = useState<Ups[]>([])
    const [upsUpdates, setUpsUpdates] = useState<number>(0)         // keeps track of crud operations in other components
    const upsUpdateOccured = () => setUpsUpdates(upsUpdates + 1)    // passed to components that do crud operations

    const getAllUpss = () => {
        axios.get('/api/ups')
            .then(response => {
                setUpss(response.data);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    };

    useEffect(() => {
        getAllUpss();
    }, [upsUpdates])


    return (

        <>
            <Routes>
                <Route path={"/"} element={<AllUpsesPage upses={upses} monitoring={monitoring}/>}/>
                <Route path={"/upsdetails/:id"} element={<UpsPage upsUpdate={upsUpdateOccured}/>} />
            </Routes>
        </>
    )
}

export default App
