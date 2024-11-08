import {Credentials} from "../types/credentials"
import "./CredentialsInfoline.css"

type Props = {
    selection: string,
    credentialsList: Credentials[]
}

export default function UpsInfoLine(props: Readonly<Props>) {
    const credentials: Credentials|undefined = props.credentialsList.find(entry => entry.id === props.selection)
    return (
        <>
        {credentials
            ? <p className={"credentials-infoline"}> <a href={"/credentials/" + credentials.id}> {credentials.user}  </a> </p>
            : <p> None selected </p>
        }
        </>
    )
}