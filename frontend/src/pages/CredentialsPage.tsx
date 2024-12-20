import {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import CredentialsContentDisplayAndEditing from "../components/CredentialsContentDisplayAndEditing.tsx";
import {Credentials} from "../types/credentials.ts";

type Props = {
    credentialsUpdate : () => void,
    monitoring:boolean
}

export default function CredentialsPage(props:Readonly<Props>) {
    const params = useParams()
    const id: string | undefined = params.id
    const [credentials, setCredentials] = useState<Credentials>({id: "new", user: "", password: "", global: true})
    useEffect(() => {
        if (id!=="new") {
            axios.get('/api/credentials/' + id)
                .then(response => {
                    setCredentials(response.data)
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        }
    }, [id])

return (
    <CredentialsContentDisplayAndEditing credentials={credentials} credentialsUpdate={props.credentialsUpdate} monitoring={props.monitoring}/>
)

}