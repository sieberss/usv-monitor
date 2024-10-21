import {useEffect, useState} from "react";
import axios from "axios";
import {Usv} from "../types/usv.ts";
import UsvContentDisplayAndEditing from "../components/UsvContentDisplayAndEditing.tsx";
import {useParams} from "react-router-dom";

export default function UsvPage() {
    const params = useParams()
    const id: string | undefined = params.id
    const [usv, setUsv] = useState<Usv>({id: "new", name: "", address: "", community: ""})
console.log(id)
    useEffect(() => {
        console.log(id!=="new")
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

console.log(usv)
return (
    <>
        <UsvContentDisplayAndEditing usv={usv}/>

    </>
)

}