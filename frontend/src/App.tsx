import {useEffect, useState} from 'react'
import './App.css'
import axios from "axios";
import {Usv} from "./types/usv.ts";
import UsvList from "./components/UsvList.tsx";

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
         {monitoring ? <h3> Monitoring aktiv </h3> : <h1> kein Monitoring</h1>}
         <h1>Liste der USVen</h1>
         <UsvList usvs={usvs} monitoring={monitoring}/>
     </>
  )
}

export default App
