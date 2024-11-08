import {Credentials} from "../types/credentials.ts";
import {FaPlus} from "react-icons/fa";

type UpsCardProps = {
    credentials: Credentials
}

export default function CredentialsCard(props: Readonly<UpsCardProps>) {
    return (
        <li className={"credentialscard"}>
            <a href={"/credentials/" + props.credentials.id}>
                {props.credentials.id !== "new"
                    ? <h3>{props.credentials.user}</h3>
                    : <h2><FaPlus/></h2>
                }
            </a>
        </li>
    )
}