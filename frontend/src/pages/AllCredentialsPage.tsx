import CredentialsList from "../components/CredentialsList.tsx";
import {Credentials} from "../types/credentials.ts";

type OverviewProps = {
    credentialsList: Credentials[],
    monitoring: boolean
}

export default function AllCredentialsPage(props:Readonly<OverviewProps>) {
    return (
        <CredentialsList credentialsList={props.credentialsList} monitoring={props.monitoring}/>
    )
}