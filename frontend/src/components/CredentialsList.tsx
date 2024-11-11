import CredentialsCard from "./CredentialsCard.tsx";
import {Credentials} from "../types/credentials.ts";
import './CredentialsList.css';

type CredentialsListProps = {
    credentialsList: Credentials[],
    monitoring: boolean
}

export default function CredentialsList(props: Readonly<CredentialsListProps>) {
    return (
        <ul className={"credentialslist"}>
            {props.credentialsList
                .filter(credentials => credentials.global)  // List only lobal credentials, local ones are edited in server details
                .map(credentials =>
                    <CredentialsCard key={credentials.id} credentials={credentials}/>
                )}
            {props.monitoring // don't show add button in monitoring mode
                || <CredentialsCard credentials={{id: "new", user: "", password: "", global: true}}/>}
        </ul>
    )
}