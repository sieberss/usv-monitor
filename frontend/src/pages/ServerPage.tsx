import {useEffect, useState} from "react";
import axios from "axios";
import {Server} from "../types/server.ts";
import {useParams} from "react-router-dom";
import ServerContentDisplayAndEditing from "../components/ServerContentDisplayAndEditing.tsx";
import { Credentials } from "../types/credentials.ts";
import { Ups } from "../types/ups.ts";

type Props = {
    upses: Ups[],
    credentialsList: Credentials[],
    serverUpdate : () => void,
    getServerClassName: (server: Server) => string
}
export default function ServerPage(props:Readonly<Props>) {
    const params = useParams()
    const id: string | undefined = params.id
    const [server, setServer] = useState<Server>({id: "new", name: "", address: "", upsId: "", shutdownTime: 180, credentials:{id:"", user:"", password:"", global:false}})

    useEffect(() => {
        if (id!=="new") {
            axios.get('/api/server/' + id)
                .then(response => {
                    setServer(response.data)
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        }
    }, [id])

return (
    <ServerContentDisplayAndEditing server={server} serverUpdate={props.serverUpdate} upses={props.upses}
                                        credentialsList={props.credentialsList} getServerClassName={props.getServerClassName}/>
)

}