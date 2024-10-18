import {useEffect, useState} from 'react'
import './App.css'
import axios from "axios";
import {Usv} from "./types/usv.ts";
import {Route, Routes} from "react-router-dom";
import UsvOverview from "./pages/UsvOverview.tsx";
import UsvDetailPage from "./pages/UsvDetailPage.tsx";

function App() {
  const [usvs, setUsvs] = useState<Usv[]>([])
  const [monitoring, setMonitoring] = useState<boolean>(false)

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
  }, [])


    return (

     <>
         <Routes>
             <Route path={"/"} element={<UsvOverview usvs={usvs} monitoring={monitoring}/>} />
             <Route path={"/usvdetails/:id"} element={<UsvDetailPage/>} />
         </Routes>
     </>
  )
}

export default App
