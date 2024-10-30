import CredentialsCard from "./CredentialsCard.tsx";
import {Credentials} from "../types/credentials.ts";

type CredentialsListProps = {
    credentialsList: Credentials[],
    monitoring: boolean
}

export default function CredentialsList(props:Readonly<CredentialsListProps>){
    return(
        <>
            {props.credentialsList
                .filter(credentials => credentials.global)  // List only lobal credentials, local ones are edited in server details
                .map(credentials => <CredentialsCard credentials={credentials} key={credentials.id}/>)}

            <CredentialsCard credentials={{id:"new", user:"", password:"", global:true}}/>

        </>
    )
}