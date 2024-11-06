import CredentialsList from "../components/CredentialsList.tsx";
import {Credentials} from "../types/credentials.ts";

type OverviewProps = {
    setMenuItem: (item:string) => void,
    credentialsList: Credentials[],
    monitoring: boolean,
    username: string
}

export default function AllCredentialsPage(props:Readonly<OverviewProps>){
    props.setMenuItem("credentials")
    return(
        <div className={"credentials"}>
            <h1>Global users</h1>
            <CredentialsList credentialsList={props.credentialsList} monitoring={props.monitoring}/>
        </div>
    )
}
