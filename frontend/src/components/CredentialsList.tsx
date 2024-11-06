import CredentialsCard from "./CredentialsCard.tsx";
import {Credentials} from "../types/credentials.ts";
import './CredentialsList.css';

type CredentialsListProps = {
    credentialsList: Credentials[]
}

export default function CredentialsList(props: Readonly<CredentialsListProps>) {
    return (
        <ul className={"credentialslist"}>
            {props.credentialsList
                .filter(credentials => credentials.global)  // List only lobal credentials, local ones are edited in server details
                .map(credentials =>
                    <li className={"credentialscard"}>
                        <CredentialsCard credentials={credentials} key={credentials.id}/>
                    </li>
                )}
            <li className={"credentialscard"}>
                <CredentialsCard credentials={{id: "new", user: "", password: "", global: true}}/>
            </li>
        </ul>
    )
}