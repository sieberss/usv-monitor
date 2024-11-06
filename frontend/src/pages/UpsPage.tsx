import {useEffect, useState} from "react";
import axios from "axios";
import {Ups} from "../types/ups.ts";
import UpsContentDisplayAndEditing from "../components/UpsContentDisplayAndEditing.tsx";
import {useParams} from "react-router-dom";
import { Server } from "../types/server.ts";

type Props = {
    setMenuItem: (item:string) => void,
    upsUpdate : () => void,
    servers: Server[],
    username: string
}
export default function UpsPage(props:Readonly<Props>) {
    props.setMenuItem("ups")
    const params = useParams()
    const id: string | undefined = params.id
    const [ups, setUps] = useState<Ups>({id: "new", name: "", address: "", community: ""})

    useEffect(() => {
        if (id!=="new") {
            axios.get('/api/ups/' + id)
                .then(response => {
                    setUps(response.data)
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        }
    }, [id])

return (
    <div className={"ups"}>
        <UpsContentDisplayAndEditing ups={ups} upsUpdate={props.upsUpdate} servers={props.servers}/>
    </div>
)

}