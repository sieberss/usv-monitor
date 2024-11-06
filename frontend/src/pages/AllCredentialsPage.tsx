import CredentialsList from "../components/CredentialsList.tsx";
import {Credentials} from "../types/credentials.ts";

type OverviewProps = {
    setMenuItem: (item:string) => void,
    credentialsList: Credentials[]
}

export default function AllCredentialsPage(props:Readonly<OverviewProps>) {
    props.setMenuItem("credentials")
    return (
        <CredentialsList credentialsList={props.credentialsList}/>
    )
}