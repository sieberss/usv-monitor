import {useEffect, useState} from "react";
import axios from "axios";
import {Usv} from "../types/usv.ts";
import UsvDetails from "../components/UsvDetails.tsx";
import {useParams} from "react-router-dom";

export default function SingleUsvPage() {
    const params = useParams()
    const id: string | undefined = params.id
    const [usv, setUsv] = useState<Usv>({id: "", name: "", address: "", community: ""})

    useEffect(() => {
        if (id!=="new") {
            axios.get('/api/usv/' + id)
                .then(response => {
                    setUsv(response.data)
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        }
    }, [id])


return (
    <>
        <UsvDetails usv={usv}/>
    </>
)

}