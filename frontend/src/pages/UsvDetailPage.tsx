import {useNavigate, useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {Usv} from "../types/usv.ts";
import UsvDetails from "../components/UsvDetails.tsx";

export default  function UsvDetailPage(){
    const navigate = useNavigate()
    const params = useParams()
    const id: string|undefined = params.id
    const [usv, setUsv] = useState<Usv>()

    useEffect(() => {
        axios.get('/api/usv/' + id)
            .then(response => {
                setUsv(response.data)
                console.log(response.data)
                console.log(usv);
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    }, [id])


    return (
        <>
            {usv
                ? <UsvDetails usv={usv}/>
                : navigate("/")}
        </>
    )

}