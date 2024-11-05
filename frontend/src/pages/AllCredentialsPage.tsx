import CredentialsList from "../components/CredentialsList.tsx";
import {Credentials} from "../types/credentials.ts";

type OverviewProps = {
    credentialsList: Credentials[],
    monitoring: boolean,
    username: string
}

export default function AllCredentialsPage(props:Readonly<OverviewProps>){
    return(
        <>
            <h1>List of global users</h1>
            <CredentialsList credentialsList={props.credentialsList} monitoring={props.monitoring}/>
        </>
    )
}
