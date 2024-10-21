import {useEffect, useState} from 'react'
import './App.css'
import axios from "axios";
import {Usv} from "./types/usv.ts";
import {Route, Routes} from "react-router-dom";
import AllUsvsPage from "./pages/AllUsvsPage.tsx";
import UsvPage from "./pages/UsvPage.tsx";

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
             <Route path={"/"} element={<AllUsvsPage usvs={usvs} monitoring={monitoring}/>} />
             <Route path={"/usvdetails/:id"} element={<UsvPage/>} />
         </Routes>
     </>
  )
}

export default App
