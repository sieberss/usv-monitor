import {useEffect, useState} from "react";
import axios from "axios";
import {Usv} from "../types/usv.ts";
import UsvDetails from "../components/UsvDetails.tsx";
import {useParams} from "react-router-dom";

export default function UsvDetailPage() {
    const params = useParams()
    const id: string | undefined = params.id
    const [usv, setUsv] = useState<Usv>({id: "", name: "", address: "", community: ""})

    useEffect(() => {
        if (id) {
            axios.get('/api/usv/' + id)
                .then(response => {
                    setUsv(response.data)
                    console.log(response.data)
                    console.log(usv);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        }
    }, [id])


return (
    <>
        <UsvDetails usv={usv} isNew={!!id}/>
    </>
)

}