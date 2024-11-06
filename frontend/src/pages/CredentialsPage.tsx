import {useEffect, useState} from "react";
import axios from "axios";
import {useParams} from "react-router-dom";
import CredentialsContentDisplayAndEditing from "../components/CredentialsContentDisplayAndEditing.tsx";
import {Credentials} from "../types/credentials.ts";

type Props = {
    setMenuItem: (item:string) => void,
    credentialsUpdate : () => void,
    username: string
}
export default function CredentialsPage(props:Readonly<Props>) {
    const params = useParams()
    const id: string | undefined = params.id
    const [credentials, setCredentials] = useState<Credentials>({id: "new", user: "", password: "", global: true})
    props.setMenuItem("credentials")
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
    <div className={"credentials"}>
        <CredentialsContentDisplayAndEditing credentials={credentials} credentialsUpdate={props.credentialsUpdate}/>
    </div>
)

}