import CredentialsList from "../components/CredentialsList.tsx";
import {Credentials} from "../types/credentials.ts";

type OverviewProps = {
    credentialsList: Credentials[]
}

export default function AllCredentialsPage(props:Readonly<OverviewProps>) {
    return (
        <CredentialsList credentialsList={props.credentialsList}/>
    )
}