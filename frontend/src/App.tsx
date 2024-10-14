import {useEffect, useState} from 'react'
import './App.css'
import axios from "axios";

function App() {
  const [data, setData] = useState<string>("")
  const fetchData = () => {
    axios.get('/api/usv-monitor')
        .then(response => {
          setData(response.data);
        })
        .catch(error => {
          console.error('Error fetching data:', error);
        });
  };

  useEffect(() => {
    fetchData();
  }, [])

    return (

     <h1>{data}
    </h1>
  )
}

export default App
